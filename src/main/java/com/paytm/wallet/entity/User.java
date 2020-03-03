package com.paytm.wallet.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "balance", nullable = false)
    private double balance;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    Set<Transactions> transactions = new HashSet<>();

}
