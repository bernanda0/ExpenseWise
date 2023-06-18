package com.client.expensewise.model;

public class Goal {
    private String ugid;
    private String uid;
    private Integer p_expense;
    private Integer goal_expense;
    private String start_period;
    private String end_period;
    private String status;

    public String getUgid() {
        return ugid;
    }

    public String getUid() {
        return uid;
    }

    public Integer getP_expense() {
        return p_expense;
    }

    public Integer getGoal_expense() {
        return goal_expense;
    }

    public String getStart_period() {
        return start_period;
    }

    public String getEnd_period() {
        return end_period;
    }

    public String getStatus() {
        return status;
    }
}
