package com.cosmomedia.location.service.order;

import com.cosmomedia.location.entities.Orders;
import com.cosmomedia.location.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderCrud {
    Page<Orders> getOrdersList(Pageable pageable);
}
