package controller;

import model.AccountModel;
import model.TransactionModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class BankController {
    private List<AccountModel> accounts;
    private static String filePath = "bankaccount.ser";
    private static BankController instance = null;

    private BankController() {
        load();
    }

    public static synchronized BankController getInstance() {
        if (instance == null)
            instance = new BankController();

        return instance;
    }

    public synchronized void addAccount(AccountModel account) {
        accounts.add(account);
    }
    public synchronized void removeAccount(AccountModel account) {
        accounts.remove(account);
    }
    public synchronized AccountModel[] getAccounts() {
        return accounts.toArray(new AccountModel[accounts.size()]);
    }
    public synchronized AccountModel getAccountBy(String iban) {
        AccountModel result = null;

        for (int i = 0; result == null && i < accounts.size(); i++) {
            if (accounts.get(i).getIban() != null && accounts.get(i).getIban().equals(iban)) {
                result = accounts.get(i);
            }
        }
        return result;
    }
    public synchronized boolean addTransaction(String iban, double amount) {
        boolean result = false;
        AccountModel account = getAccountBy(iban);

        if (account != null) {
            result = true;
            account.addTransaction(new TransactionModel(LocalDate.now(), amount).toTransfer());
        }
        return result;
    }
    public synchronized AccountModel createAccount(String iban) {
        AccountModel account = getAccountBy(iban);

        if (account == null) {
            account = new AccountModel();

            account.setIban(iban);
            accounts.add(account);
            save();
        }
        else {
            account = null;
        }
        return account;
    }

    public synchronized void load() {
        accounts = serializer.FileSerializer.deserialize(filePath);
        if (accounts == null) {
            accounts = new ArrayList<>();

            AccountModel account = new AccountModel();

            account.setIban("DE27100777770209299700");
            account.addTransaction(new TransactionModel(LocalDate.now(), 100.00).toTransfer());
            account.addTransaction(new TransactionModel(LocalDate.now(), 237.75).toTransfer());
            accounts.add(account);

            account = new AccountModel();
            account.setIban("DE11520513735120710131");
            account.addTransaction(new TransactionModel(LocalDate.now(), 99.00).toTransfer());
            account.addTransaction(new TransactionModel(LocalDate.now(), 111.55).toTransfer());
            account.addTransaction(new TransactionModel(LocalDate.now(), 1234.00).toTransfer());
            account.addTransaction(new TransactionModel(LocalDate.now(), 545.85).toTransfer());
            accounts.add(account);

            account = new AccountModel();
            account.setIban("AT411100000237571500");
            account.addTransaction(new TransactionModel(LocalDate.now(), 199.00).toTransfer());
            account.addTransaction(new TransactionModel(LocalDate.now(), -99.00).toTransfer());
            accounts.add(account);
            save();
        }
    }

    public synchronized void save() {
        serializer.FileSerializer.serialize(filePath, accounts);
    }
}
