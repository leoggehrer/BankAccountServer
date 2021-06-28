package model;

import common.transfers.Transaction;

import java.time.LocalDate;

public class TransactionModel extends Transaction {

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public TransactionModel(LocalDate date, double amount) {
        this.date = date;
        this.amount = amount;
    }

    public TransactionModel(TransactionModel model) {
        if (model == null)
            throw new IllegalArgumentException("model");

        date = model.date;
        amount = model.amount;
    }

    public Transaction toTransfer() {
        return new Transaction(this);
    }
}
