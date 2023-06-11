package com.client.expensewise.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ParseResponse extends BaseResponse {
    @SerializedName("raw")
    @Expose
    private String raw;

    @SerializedName("gpt")
    @Expose
    private ParseData gpt;

    public String getRaw() {
        return raw;
    }

    public ParseData getGpt() {
        return gpt;
    }

    class ParseData {
        @SerializedName("date")
        @Expose
        private String date;
        @SerializedName("amount")
        @Expose
        private Integer amount;
        @SerializedName("description")
        @Expose
        private String description;

        public String getDate() {
            return date;
        }

        public Integer getAmount() {
            return amount;
        }

        public String getDescription() {
            return description;
        }
    }
}
