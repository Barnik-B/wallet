package com.paytm.wallet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paytm.wallet.exceptions.NoResourceFoundException;
import com.paytm.wallet.exceptions.ValidationException;
import com.paytm.wallet.model.request.TransactionDetails;
import com.paytm.wallet.model.request.TransferDetails;
import com.paytm.wallet.model.response.ApiResponse;
import com.paytm.wallet.services.WalletService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Validated
@RestController
@Slf4j
@RequestMapping("/api/v1/wallet")
@Api(value="Wallet", description="Operations pertaining to a wallet, such as checking balance, adding money, withdrawing money, transferring money to someone for a user.")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @Autowired
    private ObjectMapper objectMapper;

    @ApiOperation(value = "Returns the balance for an user.", response = ApiResponse.class)
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Fetched balance successfully."),
            @io.swagger.annotations.ApiResponse(code = 400, message = "User not found."),
            @io.swagger.annotations.ApiResponse(code = 403, message = "UserId is blank or missing in headers."),
            @io.swagger.annotations.ApiResponse(code = 404, message = "Bad request."),
    })
    @GetMapping("/balance")
    public ResponseEntity<ApiResponse> checkBalance() throws NoResourceFoundException {
        int userId = Integer.parseInt(MDC.get("userId"));
        return ResponseEntity.ok(walletService.checkBalance(userId));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addMoney(@ApiParam(value = "TransactionDetails object containing the amount to be added", required = true) @Valid @RequestBody TransactionDetails details) throws NoResourceFoundException, ValidationException {
        int userId = Integer.parseInt(MDC.get("userId"));
        return ResponseEntity.ok(walletService.addMoney(userId, details));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<ApiResponse> withdrawMoney(@Valid @RequestBody TransactionDetails details) throws NoResourceFoundException, ValidationException {
        int userId = Integer.parseInt(MDC.get("userId"));
        return ResponseEntity.ok(walletService.withdrawMoney(userId, details));
    }

    @PostMapping("/transfer")
    public ResponseEntity<ApiResponse> transferMoney(@Validated @RequestBody TransferDetails details) throws NoResourceFoundException, ValidationException {
        int userId = Integer.parseInt(MDC.get("userId"));
        return ResponseEntity.ok(walletService.transferMoney(userId, details));
    }

}
