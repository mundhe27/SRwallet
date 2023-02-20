package com.bsr.bsrcoin.Models;

public class ChequeModel {
    private final String chequeId;
    private final String account;
    private final String date;
    private final String amount;
    public String status;

    public ChequeModel(String chequeId, String account, String date, String amount, String status) {
        this.chequeId = chequeId;
        this.account = account;
        this.date = date;
        this.amount = amount;
        this.status = status;
    }

    public String getChequeId() {
        return chequeId;
    }

    public String getAccount() {
        return account;
    }

    public String getDate() {
        return date;
    }

    public String getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }
}
