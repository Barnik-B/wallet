package com.paytm.wallet.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "transfers")
public class Transfers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name ="transaction_id_sender")
    private Transactions senderTransaction;

    @OneToOne
    @JoinColumn(name ="transaction_id_receiver")
    private Transactions receiverTransaction;

}
