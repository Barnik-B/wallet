package com.paytm.wallet.constants;

public enum TransactionType {
    ADD("add"), WITHDRAW("withdraw"), TRANSFER("transfer");

    private String value;

    TransactionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
