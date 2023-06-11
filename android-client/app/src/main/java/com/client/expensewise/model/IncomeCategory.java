package com.client.expensewise.model;

public class IncomeCategory {
    private String ic_name;
    private Float percentage;

    public String getIc_name() {
        return ic_name;
    }

    public Float getPercentage() {
        return percentage;
    }

    @Override
    public String toString() {
        return "IncomeCategory{" +
                "ic_name='" + ic_name + '\'' +
                ", percentage=" + percentage +
                '}';
    }
}
