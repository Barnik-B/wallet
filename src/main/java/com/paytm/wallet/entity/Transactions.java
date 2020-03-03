package com.paytm.wallet.entity;

import com.paytm.wallet.constants.PaymentType;
import com.paytm.wallet.constants.TransactionType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "transactions")
public class Transactions {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "amount", nullable = false)
    private double amount;

    // User user
    // Transaction txn on txn.user=user.id;
    // transaction on transaction.user_id=user.id
    @ManyToOne
    @JoinColumn(name ="user_id")
    private User user;

    @OneToOne(mappedBy = "senderTransaction", cascade = CascadeType.ALL)
    private Transfers senderTransfer;

    @OneToOne(mappedBy = "receiverTransaction", cascade = CascadeType.ALL)
    private Transfers receiverTransfer;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Column(name = "payment_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

}
