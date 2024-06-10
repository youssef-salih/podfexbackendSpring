package com.cosmomedia.location.controllers;

import com.cosmomedia.location.entities.Balance;
import com.cosmomedia.location.entities.Message;
import com.cosmomedia.location.service.balence.BalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class BalanceControllers
{
    private final BalanceService balanceCrud;

    @QueryMapping
    public Balance balanceUser(){
        return balanceCrud.balanceUser();
    }

    @MutationMapping
    public Message addBalance(@Argument(name = "amount")Double amount){
        return  balanceCrud.addBalance(amount);
    }
}
