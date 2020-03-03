package com.paytm.wallet.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
public class TransferDetails {

    @JsonProperty(value = "amount")
    @NotNull
    @Positive
    private Double amount;

    @JsonProperty(value = "userId")
    @NotNull
    private Integer userId;
}
