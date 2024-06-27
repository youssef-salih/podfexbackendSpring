package com.cosmomedia.podfex.controllers;

import com.cosmomedia.podfex.dto.*;
import com.cosmomedia.podfex.entities.*;
import com.cosmomedia.podfex.enums.StatusAdmin;
import com.cosmomedia.podfex.enums.StatusUser;
import com.cosmomedia.podfex.service.order.OrderCrud;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrdersControllers {
    private final OrderCrud orderCrud;

    @QueryMapping
    public Page<OrdersDto> getOrdersList(@Argument(name = "page") int page, @Argument(name = "size") int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return orderCrud.getOrdersList(pageable);
    }

    @QueryMapping
    public Page<OrdersDto> getUsersOrdersList(@Argument(name = "page") int page, @Argument(name = "size") int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return orderCrud.getUsersOrdersList(pageable);
    }

    @QueryMapping
    public Page<OrdersDto> getAssignedOrdersList(@Argument(name = "page") int page, @Argument(name = "size") int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return orderCrud.getAssignedOrdersList(pageable);
    }

    @QueryMapping
    public Page<OrdersDto> getConfirmedOrdersList(@Argument(name = "page") int page, @Argument(name = "size") int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return orderCrud.getConfirmedOrdersList(pageable);
    }

    @QueryMapping
    public OneResponse<OrdersDto> getOneOrder(@Argument(name = "orderNo") String orderNo) {
        return orderCrud.getOneOrder(orderNo);
    }

    @QueryMapping
    public ListResponse<OrdersDto> getOrdersByType(@Argument(name = "type") String type, @Argument Long productId) {
        List<OrdersDto> orders = orderCrud.getOrdersByType(type, productId);
        return ListResponse.<OrdersDto>builder()
                .success(true)
                .list(orders)
                .message("Orders found")
                .build();
    }

    @MutationMapping
    public Message addOrder(
            @Argument(name = "orderNo") String orderNo,
            @Argument(name = "price") Double price,
            @Argument(name = "type") String type,
            @Argument(name = "productId") Long productId,
            @Argument(name = "preview") byte[] preview,
            @Argument(name = "images") List<byte[]> images
    ) {
        Orders order = Orders.builder()
                .orderNo(orderNo)
                .price(price)
                .statusUser(StatusUser.INQUEUE)
                .statusAdmin(StatusAdmin.UNVERIFIED)
                .type(type)
                .preview(preview)
                .images(images)
                .build();

        if (productId != null) {
            order.setProduct(new Product(productId));
        }

        return orderCrud.addOrder(order);
    }

    @MutationMapping
    public Message linkOrders(
            @Argument(name = "orderNo") String orderNo,
            @Argument(name = "orderNo2") String orderNo2
    ) {
        return orderCrud.linkOrders(orderNo, orderNo2);
    }

    ;

    @MutationMapping
    public Message unlinkOrders(
            @Argument(name = "orderNo") String orderNo,
            @Argument(name = "orderNo2") String orderNo2
    ) {
        return orderCrud.unlinkOrders(orderNo, orderNo2);
    }

    ;

    @MutationMapping
    public Message updateOrderPersonnel(
            @Argument(name = "orderNo") String orderNo,
            @Argument(name = "personnelId") Long personnelId
    ) {
        return orderCrud.updateOrderPersonnel(orderNo, personnelId);
    }

    @MutationMapping
    public Message confirmOrder(
            @Argument(name = "orderNo") String orderNo,
            @Argument(name = "quantity") Integer quantity,
            @Argument(name = "client") Client client,
            @Argument(name = "orderItems") List<OrderItemDto> orderItems
    ) {
        return orderCrud.confirmOrder(orderNo, quantity, client, orderItems);
    }


    @QueryMapping
    public int getCurrentUserOrdersCount() {
        return orderCrud.getCurrentUserOrdersCount();
    }

    @QueryMapping
    public int getConfirmedOrder() {
        return orderCrud.getConfirmedOrder();
    }

    @QueryMapping
    public int getAssignedOrder() {
        return orderCrud.getAssignedOrder();
    }


    @QueryMapping
    public int getCurrentUserOrdersByStatus(@Argument(name = "userStatus") StatusUser userStatus) {
        return orderCrud.getCurrentUserOrdersByStatus(userStatus);
    }
    @QueryMapping
    public int getAssignedOrdersByStatus(@Argument(name = "userStatus") StatusUser userStatus) {
        return orderCrud.getAssignedOrdersByStatus(userStatus);
    }
    @QueryMapping
    public int getConfirmedOrdersByStatus(@Argument(name = "userStatus") StatusUser userStatus) {
        return orderCrud.getConfirmedOrdersByStatus(userStatus);
    }
    @MutationMapping
    public Message validateOrder(
            @Argument(name = "orderNo") String orderNo,
            @Argument(name = "status") StatusAdmin status,
            @Argument(name = "personnel") Long personnel
    ) {
        return orderCrud.validateOrder(orderNo, status,personnel);
    }

    @MutationMapping
    public Message changeOrderStatus(
            @Argument(name="orderNo")String orderNo,
            @Argument(name="status")StatusUser status
    ){
        return orderCrud.changeOrderStatus(orderNo,status);
    }
}
