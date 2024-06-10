package com.cosmomedia.location.service.transactions;

import com.cosmomedia.location.entities.Transactions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionsService {
    Page<Transactions> getTransactionList(Pageable pageable);
    Page<Transactions> getTransactionsForCurrentUser(Pageable pageable);
}
