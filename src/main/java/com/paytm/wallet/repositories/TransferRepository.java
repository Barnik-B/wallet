package com.paytm.wallet.repositories;

import com.paytm.wallet.entity.Transfers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferRepository extends JpaRepository<Transfers, Integer> {}