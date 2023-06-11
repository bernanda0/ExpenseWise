package com.client.expensewise.model;

public class Expense {
    private String eid;
    private String ecid;
    private String uid;
    private Integer amount;
    private String time;
    private String description;
    private Float percentage;
    private Integer snapshot_balance;

    public Float getPercentage() {
        return percentage;
    }

    public Integer getSnapshot_balance() {
        return snapshot_balance;
    }

    public String getEid() {
        return eid;
    }

    public String getEcid() {
        return ecid;
    }

    public String getUid() {
        return uid;
    }

    public Integer getAmount() {
        return amount;
    }

    public String getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }
}
