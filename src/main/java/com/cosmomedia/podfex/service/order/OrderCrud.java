package com.cosmomedia.podfex.service.order;

import com.cosmomedia.podfex.dto.OneResponse;
import com.cosmomedia.podfex.dto.OrderItemDto;
import com.cosmomedia.podfex.dto.OrdersDto;
import com.cosmomedia.podfex.entities.Client;
import com.cosmomedia.podfex.entities.Message;
import com.cosmomedia.podfex.entities.Orders;
import com.cosmomedia.podfex.enums.StatusAdmin;
import com.cosmomedia.podfex.enums.StatusUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderCrud {
    Page<OrdersDto> getOrdersList(Pageable pageable);

    Page<OrdersDto> getUsersOrdersList(Pageable pageable);


    OneResponse<OrdersDto> getOneOrder(String orderNo);

    List<OrdersDto> getOrdersByType(String type, Long productId);

    Message addOrder(Orders order);

    Message updateOrderPersonnel(String orderNo, Long personnelId);

    Message linkOrders(String orderNo, String orderNo2);

    Message unlinkOrders(String orderNo, String orderNo2);


    Page<OrdersDto> getConfirmedOrdersList(Pageable pageable);

    Page<OrdersDto> getAssignedOrdersList(Pageable pageable);

    int getCurrentUserOrdersCount();

    int getConfirmedOrder();

    int getAssignedOrder();

    int getCurrentUserOrdersByStatus(StatusUser statusUser);

    int getAssignedOrdersByStatus(StatusUser statusUser);

    int getConfirmedOrdersByStatus(StatusUser statusUser);

    Message confirmOrder(String orderNo, Integer quantity, Client client, List<OrderItemDto> orderItems);


    Message validateOrder(String orderNo, StatusAdmin status, Long PersonnelId);

    Message changeOrderStatus(String orderNo, StatusUser status);
}
