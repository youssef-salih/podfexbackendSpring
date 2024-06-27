package com.cosmomedia.podfex.controllers;

import com.cosmomedia.podfex.entities.Message;
import com.cosmomedia.podfex.entities.Transactions;
import com.cosmomedia.podfex.service.transactions.TransactionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class TransactionsControllers {

    private final TransactionsService transactionsService;
    @QueryMapping
    public Page<Transactions> allTransactions(@Argument(name = "page") int page, @Argument(name = "size") int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return transactionsService.getTransactionList(pageable);
    }
    @QueryMapping
    public Page<Transactions> getTransactionsForCurrentUser(@Argument(name = "page") int page, @Argument(name = "size") int size){
        Pageable pageable = PageRequest.of(page - 1, size);
        return transactionsService.getTransactionsForCurrentUser(pageable);
    }
    @QueryMapping
    public Page<Transactions> getTransactionsNotconfirmed(@Argument(name = "page") int page, @Argument(name = "size") int size){
        Pageable pageable = PageRequest.of(page - 1, size);
        return transactionsService.getTransactionsNotconfirmed(pageable);
    }

    @MutationMapping
    public Message confirmTransaction(@Argument(name="transactionNo") String transactionNo){
        return transactionsService.confirmTransaction(transactionNo);
    }
    @MutationMapping
    public Message cancelTransaction(@Argument(name="transactionNo") String transactionNo){
        return transactionsService.cancelTransaction(transactionNo);
    }
}
