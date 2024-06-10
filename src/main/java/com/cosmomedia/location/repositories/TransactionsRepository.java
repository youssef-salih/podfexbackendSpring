package com.cosmomedia.location.repositories;

import com.cosmomedia.location.entities.Transactions;
import com.cosmomedia.location.entities.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionsRepository extends JpaRepository<Transactions, Long> {
    Page<Transactions> findByUser(Users user, Pageable pageable);
}
