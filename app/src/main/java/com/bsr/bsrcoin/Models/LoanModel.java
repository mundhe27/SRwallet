package com.bsr.bsrcoin.Models;

import com.bsr.bsrcoin.MysqlConst.Constants;

public class LoanModel {
    private final String id;
    private final String loan_id;
    private final String type;
    private final String duration;
    private final String amount;
    private final String rate;
    private final String agent;
    private final String status;
    private final String update;
    private final String reqinr;
    private final String image;

    public LoanModel(String id, String loan_id, String type, String duration, String amount, String rate, String agent, String status, String update, String reqinr, String image) {
        this.id = id;
        this.loan_id = loan_id;
        this.type = type;
        this.duration = duration;
        this.amount = amount;
        this.rate = rate;
        this.agent = agent;
        this.status = status;
        this.update = update;
        this.reqinr = reqinr;
        this.image = image;
    }

    public String getReqinr() {
        return reqinr;
    }

    public String getImage() {
        return image;
    }

    public String getId() {
        return id;
    }

    public String getLoan_id() {
        return loan_id;
    }

    public String getType() {
        return type;
    }

    public String getDuration() {
        return duration;
    }

    public String getAmount() {
        return amount;
    }

    public String getRate() {
        return rate;
    }

    public String getAgent() {
        return agent;
    }

    public String getStatus() {
        return status;
    }

    public String getUpdate() {
        return update;
    }
}
