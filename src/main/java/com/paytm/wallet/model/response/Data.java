package com.paytm.wallet.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Data {

    private Double userBalance;
    private Double receiverBalance;

    public Data(double userBalance) {
        this.userBalance = userBalance;
    }

    public Data() {
    }

    public Double getUserBalance() {
        return userBalance;
    }

    public void setUserBalance(Double userBalance) {
        this.userBalance = userBalance;
    }

    public Double getReceiverBalance() {
        return receiverBalance;
    }

    public void setReceiverBalance(Double receiverBalance) {
        this.receiverBalance = receiverBalance;
    }
}
