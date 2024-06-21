package com.cosmomedia.location.service.order;

import com.cosmomedia.location.dto.OneResponse;
import com.cosmomedia.location.dto.OrderItemDto;
import com.cosmomedia.location.dto.OrdersDto;
import com.cosmomedia.location.entities.Client;
import com.cosmomedia.location.entities.Message;
import com.cosmomedia.location.entities.OrderItem;
import com.cosmomedia.location.entities.Orders;
import com.cosmomedia.location.enums.Sizes;
import com.cosmomedia.location.enums.StatusUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface OrderCrud {
    Page<OrdersDto> getOrdersList(Pageable pageable);
    Page<OrdersDto> getUsersOrdersList(Pageable pageable);



    OneResponse<OrdersDto> getOneOrder(String orderNo);

    List<OrdersDto> getOrdersByType(String type,Long productId);

    Message addOrder(Orders order);

    Message updateOrderPersonnel(String orderNo, Long personnelId);

    Message linkOrders(String orderNo, String orderNo2);

    Message unlinkOrders(String orderNo, String orderNo2);


    Page<OrdersDto> getConfirmedOrdersList(Pageable pageable);

    Page<OrdersDto> getAssignedOrdersList(Pageable pageable);

    int getCurrentUserOrdersCount();
    int getCurrentUserOrdersByStatus(StatusUser statusUser);

    Message confirmOrder(String orderNo, Integer quantity, Client client, List<OrderItemDto> orderItems);
}
