package com.cosmomedia.location.repositories;

import com.cosmomedia.location.entities.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionsRepository extends JpaRepository<Transactions,Long> {
}
