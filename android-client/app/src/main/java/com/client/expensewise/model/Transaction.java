package com.client.expensewise.model;

import java.util.Date;

public class Transaction {
    private String tid;
    private String t_category;
    private Integer t_amount;
    private String t_time;
    private String t_description;
    private Float t_percentage;
    private Integer t_snapshot_balance;
    private String t_expense_level;

    public String getTid() {
        return tid;
    }

    public String getT_category() {
        return t_category;
    }

    public Integer getT_amount() {
        return t_amount;
    }

    public String getT_date() {
        return t_time;
    }

    public String getT_description() {
        return t_description;
    }

    public Float getT_percentage() {
        return t_percentage;
    }

    public Integer getT_snapshot_balance() {
        return t_snapshot_balance;
    }

    public String getT_expense_level() {
        return t_expense_level;
    }
}
