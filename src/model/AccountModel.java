package model;

import common.transfers.Account;
import common.transfers.Transaction;

public class AccountModel extends Account {

    public AccountModel() {
        if (transactions == null)
            transactions = new Transaction[0];
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public void addTransaction(Transaction transaction) {
        Transaction[] tmp = new Transaction[transactions.length + 1];

        for (int i = 0; i < transactions.length; i++) {
            tmp[i] = transactions[i];
        }
        tmp[tmp.length - 1] = transaction;
        transactions = tmp;
    }

    public Account toTransfer() {
        return new Account(this);
    }
}
