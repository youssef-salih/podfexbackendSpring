package com.cosmomedia.location.controllers;

import com.cosmomedia.location.entities.Orders;
import com.cosmomedia.location.service.order.OrderCrud;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class OrdersControllers {
    private final OrderCrud orderCrud;

    @QueryMapping
    public Page<Orders> getOrdersList(@Argument(name = "page") int page, @Argument(name = "size") int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return orderCrud.getOrdersList(pageable);
    }
}
