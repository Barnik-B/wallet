package com.paytm.wallet.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
@ApiModel(description = "Details pertaining to an user.")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "Auto-generated user id.")
    private int id;

    @Column(name = "balance", nullable = false)
    @ApiModelProperty(notes = "The user's balance.")
    private double balance;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @ApiModelProperty(notes = "Transactions of an user.")
    Set<Transactions> transactions = new HashSet<>();

}
