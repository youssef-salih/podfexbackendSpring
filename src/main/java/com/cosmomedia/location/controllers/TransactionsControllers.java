package com.cosmomedia.location.controllers;

import com.cosmomedia.location.dto.ProductDto;
import com.cosmomedia.location.entities.Transactions;
import com.cosmomedia.location.service.transactions.TransactionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.graphql.data.method.annotation.Argument;
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
}
