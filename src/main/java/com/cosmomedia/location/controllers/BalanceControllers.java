package com.cosmomedia.location.controllers;

import com.cosmomedia.location.dto.OneResponse;
import com.cosmomedia.location.entities.Balance;
import com.cosmomedia.location.service.balence.BalanceCRUD;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class BalanceControllers
{
    private BalanceCRUD balanceCrud;

}
