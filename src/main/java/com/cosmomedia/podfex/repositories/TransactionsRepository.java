package com.cosmomedia.podfex.repositories;

import com.cosmomedia.podfex.entities.Transactions;
import com.cosmomedia.podfex.entities.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransactionsRepository extends JpaRepository<Transactions, Long> {
    Page<Transactions> findByUser(Users user, Pageable pageable);

    Optional<Transactions> findByTransactionNo(String transactionNo);

    Page<Transactions> findByConfirmedFalse(Pageable pageable);

    Page<Transactions> findByConfirmedTrue(Pageable pageable);

    Page<Transactions> findByUserAndConfirmedFalse(Users user, Pageable pageable);

}
