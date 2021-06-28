package socket;

import at.htlleonding.Application;
import common.contracts.Command;
import common.contracts.State;
import common.transfers.Account;
import controller.BankController;
import model.AccountModel;
import common.transfers.Transfer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

/**
 * This class accepts the requests from the clients, evaluates them and returns the corresponding answer.
 */
public class Server {
    private int port = 2222;

    public Server(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public void listen() throws IOException {
        ServerSocket server = new ServerSocket(port);

        while (Application.isActive()) {
            Socket socket = server.accept();
            Transfer transfer = null, result = null;
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            try {
                transfer = (Transfer)ois.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            if (transfer != null) {
                result = executeClientCommand(transfer);

                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

                oos.writeObject(result);
                oos.close();
            }
            ois.close();
            socket.close();
        }
    }
    private Transfer executeClientCommand(Transfer request) {
        if (request == null)
            throw new IllegalArgumentException();

        Transfer result = null;
        BankController bankCtrl = BankController.getInstance();

        if (request.getCommand() == Command.CHECK_IBAN) {
            if (util.IbanChecker.isValid(request.getIban())) {
                result = new Transfer(request.getCommand(), State.VALID_IBAN);
            }
            else {
                result = new Transfer(request.getCommand(), State.INVALID_IBAN);
            }
            result.setIban(request.getIban());
        }
        else if (request.getCommand() == Command.ADD_ACCOUNT) {
            if (util.IbanChecker.isValid(request.getIban()) == false) {
                result = new Transfer(request.getCommand(), State.INVALID_IBAN);
            }
            else {
                AccountModel account = bankCtrl.createAccount(request.getIban());

                if (account == null) {
                    result = new Transfer(request.getCommand(), State.INVALID_IBAN);
                }
                else {
                    result = new Transfer(request.getCommand(), State.OK);
                    result.setAccounts(new Account[] { account.toTransfer() });
                    bankCtrl.save();
                }
            }
        }
        else if (request.getCommand() == Command.GET_ACCOUNT) {
            if (util.IbanChecker.isValid(request.getIban()) == false) {
                result = new Transfer(request.getCommand(), State.INVALID_IBAN);
            }
            else {
                AccountModel account = bankCtrl.getAccountBy(request.getIban());

                if (account == null) {
                    result = new Transfer(request.getCommand(), State.INVALID_IBAN);
                }
                else {
                    result = new Transfer(request.getCommand(), State.OK);
                    result.setAccounts(new Account[] { account.toTransfer() });
                }
            }
            result.setIban(request.getIban());
        }
        else if (request.getCommand() == Command.ALL_ACCOUNTS) {
            List<Account> accountList = new LinkedList<>();

            result = new Transfer(request.getCommand(), State.OK);
            for (AccountModel am : bankCtrl.getAccounts()) {
                accountList.add(am.toTransfer());
            }
            result.setAccounts(accountList.toArray(new Account[accountList.size()]));
        }
        else if (request.getCommand() == Command.ADD_TRANSACTION) {
            if (util.IbanChecker.isValid(request.getIban()) == false) {
                result = new Transfer(request.getCommand(), State.INVALID_IBAN);
            }
            else {
                if (bankCtrl.addTransaction(request.getIban(), request.getAmount()) == false) {
                    result = new Transfer(request.getCommand(), State.UNKNOWN_ACCOUNT);
                }
                else {
                    result = new Transfer(request.getCommand(), State.OK);
                }
            }
            result.setIban(request.getIban());
        }
        else {
            result = new Transfer(request.getCommand(), State.UNKNOWN_COMMAND);
        }

        return result;
    }
}
