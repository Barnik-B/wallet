package com.paytm.wallet.services;

import com.paytm.wallet.constants.PaymentType;
import com.paytm.wallet.constants.TransactionType;
import com.paytm.wallet.entity.Transactions;
import com.paytm.wallet.entity.Transfers;
import com.paytm.wallet.entity.User;
import com.paytm.wallet.exceptions.ValidationException;
import com.paytm.wallet.exceptions.NoResourceFoundException;
import com.paytm.wallet.model.request.TransactionDetails;
import com.paytm.wallet.model.request.TransferDetails;
import com.paytm.wallet.model.response.ApiResponse;
import com.paytm.wallet.model.response.Data;
import com.paytm.wallet.repositories.TransactionsRepository;
import com.paytm.wallet.repositories.TransferRepository;
import com.paytm.wallet.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Slf4j
public class WalletService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionsRepository transactionsRepository;

    @Autowired
    private TransferRepository transferRepository;

    public ApiResponse checkBalance(int userId) throws NoResourceFoundException {
        log.info("[checkBalance] userId:: {}", userId);
        User user = validateUser(userId);
        log.debug("[checkBalance] Balance fetched successfully for userId {}", userId);
        return populateBalanceResponse(userId, user.getBalance());
    }

    @Transactional(rollbackOn = {RuntimeException.class, Error.class})
    public ApiResponse addMoney(int userId, TransactionDetails details) throws NoResourceFoundException, ValidationException {
        double amount = details.getAmount();
        log.info("[addMoney] userId:: {}, amount:: {}", userId, amount);
        User user = validateUser(userId);
        Transactions transaction = new Transactions();
        transaction.setAmount(amount);
        transaction.setType(TransactionType.ADD);
        transaction.setPaymentType(PaymentType.CREDIT);
        transaction.setUser(user);
        double updatedBalance = user.getBalance() + amount;
        user.setBalance(updatedBalance);
        transactionsRepository.save(transaction);
        log.debug("[addMoney] Added money successfully for userId {}", userId);
        return populateTransactionResponse(updatedBalance, 0, TransactionType.ADD);
    }

    @Transactional(rollbackOn = {RuntimeException.class, Error.class})
    public ApiResponse withdrawMoney(int userId, TransactionDetails details) throws NoResourceFoundException, ValidationException {
        double amount = details.getAmount();
        log.info("[addMoney] userId:: {}, amount:: {}", userId, amount);
        User user = validateUser(userId);
        Transactions transaction = new Transactions();
        transaction.setAmount(amount);
        transaction.setType(TransactionType.WITHDRAW);
        transaction.setPaymentType(PaymentType.DEBIT);
        transaction.setUser(user);
        double updatedBalance = user.getBalance() - amount;
        if(amount < 0)
            throw new ValidationException("Insufficient balance.");
        user.setBalance(updatedBalance);
        transactionsRepository.save(transaction);
        log.debug("[withdrawMoney] Withdrew money successfully for userId {}", userId);
        return populateTransactionResponse(updatedBalance, 0, TransactionType.WITHDRAW);
    }

    @Transactional(rollbackOn = {RuntimeException.class, Error.class})
    public ApiResponse transferMoney(int userId, TransferDetails details) throws NoResourceFoundException, ValidationException {
        int receiverId = details.getUserId();
        double amount = details.getAmount();
        log.info("[transferMoney] userId::{}, amount:: {}, receiverId:: {}", userId, amount, receiverId);
        User sender = validateUser(userId);
        User receiver = validateUser(receiverId);
        double updatedSenderBalance = sender.getBalance() - amount;
        if(updatedSenderBalance < 0)
            throw new ValidationException("Insufficient balance.");
        Transactions senderTransaction = new Transactions();
        Transactions receiverTransaction = new Transactions();
        senderTransaction.setUser(sender);
        senderTransaction.setAmount(amount);
        senderTransaction.setType(TransactionType.TRANSFER);
        senderTransaction.setPaymentType(PaymentType.DEBIT);
        sender.setBalance(updatedSenderBalance);
        senderTransaction =  transactionsRepository.save(senderTransaction);
        double updatedReceiverBalance = receiver.getBalance() + amount;
        receiverTransaction.setUser(receiver);
        receiverTransaction.setAmount(amount);
        receiverTransaction.setType(TransactionType.TRANSFER);
        receiverTransaction.setPaymentType(PaymentType.CREDIT);
        receiver.setBalance(updatedReceiverBalance);
        receiverTransaction = transactionsRepository.save(receiverTransaction);
        Transfers transfer = new Transfers();
        transfer.setSenderTransaction(senderTransaction);
        transfer.setReceiverTransaction(receiverTransaction);
        transferRepository.save(transfer);
        log.debug("[transferMoney] Transferred money from senderId {} to receiverId {}", userId, receiverId);
        return populateTransactionResponse(updatedSenderBalance, updatedReceiverBalance, TransactionType.TRANSFER);
    }


    private User validateUser(int id) throws NoResourceFoundException {
        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isPresent()) {
            log.debug("[validateUser] UserId {} validated successfully", id);
            return userOptional.get();
        }
        throw new NoResourceFoundException("User is not present.");
    }

    private ApiResponse populateBalanceResponse(int userId, double userBalance) {
        ApiResponse apiResponse = new ApiResponse();
        Data data = new Data(userBalance);
        apiResponse.setData(data);
        return apiResponse;
    }

    private ApiResponse populateTransactionResponse(double userBalance, double receiverBalance, TransactionType transactionType) {
        ApiResponse apiResponse = new ApiResponse();
        Data data = new Data();
        data.setUserBalance(userBalance);
        if(transactionType == TransactionType.TRANSFER)
            data.setReceiverBalance(receiverBalance);
        apiResponse.setData(data);
        return apiResponse;
    }
}
