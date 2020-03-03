package com.paytm.wallet.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
public class TransactionDetails {

    @JsonProperty(value = "amount")
    @NotNull(message = "Amount should not be null")
    @Positive(message = "Amount should be positive")
    private Double amount;

}
