package com.cosmomedia.podfex.service.transactions;

import com.cosmomedia.podfex.entities.Message;
import com.cosmomedia.podfex.entities.Transactions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionsService {
    Page<Transactions> getTransactionList(Pageable pageable);
    Page<Transactions> getTransactionsForCurrentUser(Pageable pageable);

    Page<Transactions> getTransactionsNotconfirmed(Pageable pageable);

    Message confirmTransaction(String transactionNo);

    Message cancelTransaction(String transactionNo);
}
