package com.client.expensewise.model;

public class ExpenseCategory {
    private String ec_name;
    private Float percentage;

    public String getEc_name() {
        return ec_name;
    }

    public Float getPercentage() {
        return percentage;
    }

    @Override
    public String toString() {
        return "ExpenseCategory{" +
                "ec_name='" + ec_name + '\'' +
                ", percentage=" + percentage +
                '}';
    }
}
