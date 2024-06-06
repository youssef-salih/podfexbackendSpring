package com.cosmomedia.location.service.order;

import com.cosmomedia.location.dto.OneResponse;
import com.cosmomedia.location.dto.OrdersDto;
import com.cosmomedia.location.entities.Client;
import com.cosmomedia.location.entities.Message;
import com.cosmomedia.location.entities.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface OrderCrud {
    Page<OrdersDto> getOrdersList(Pageable pageable);


    OneResponse<OrdersDto> getOneOrder(String orderNo);

    List<OrdersDto> getOrdersByType(String type);

    Message addOrder(Orders order);

    Message updateOrderPersonnel(String orderNo, Long personnelId);

    Message linkOrders(String orderNo, String orderNo2);

    Message unlinkOrders(String orderNo, String orderNo2);

    Message confirmOrder(String orderNo, Integer quantity, Client client);
}
