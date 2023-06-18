package com.client.expensewise.model.response;

import com.client.expensewise.model.Goal;

import java.util.ArrayList;

public class GoalResponse extends BaseResponse {
    private ArrayList<Goal> goal;

    public ArrayList<Goal> getGoal() {
        return goal;
    }

    @Override
    public String toString() {
        return "GoalResponse{" +
                "success=" + isSuccess() +
                ", message='" + getMessage() + '\'' +
                "goal=" + goal +
                '}';
    }
}
