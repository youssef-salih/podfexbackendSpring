package com.cosmomedia.location.service.order;

import com.cosmomedia.location.entities.Orders;
import com.cosmomedia.location.repositories.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderCrudImpl implements OrderCrud{
    private final OrdersRepository ordersRepository;
    @Override
    public Page<Orders> getOrdersList(Pageable pageable) {
        return ordersRepository.findAll(pageable);
    }
}
