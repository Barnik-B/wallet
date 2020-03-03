package com.paytm.wallet.constants;

public enum PaymentType {
    CREDIT("credit"), DEBIT("debit");

    private String value;

    PaymentType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
