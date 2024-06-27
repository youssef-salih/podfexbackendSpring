package com.cosmomedia.podfex.controllers;

import com.cosmomedia.podfex.entities.Balance;
import com.cosmomedia.podfex.entities.Message;
import com.cosmomedia.podfex.service.balence.BalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class BalanceControllers {
    private final BalanceService balanceCrud;

    @QueryMapping
    public Balance balanceUser() {
        return balanceCrud.balanceUser();
    }

    @MutationMapping
    public Message addBalance(@Argument(name = "amount") Double amount) {
        return balanceCrud.addBalance(amount);
    }

    @MutationMapping
    public Message confirmBalance(@Argument(name = "no") String No) {
        return balanceCrud.confirmBalance(No);
    }
}
