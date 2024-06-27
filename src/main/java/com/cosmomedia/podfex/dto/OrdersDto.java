package com.cosmomedia.podfex.dto;

import com.cosmomedia.podfex.entities.Client;
import com.cosmomedia.podfex.entities.OrderItem;
import com.cosmomedia.podfex.enums.StatusAdmin;
import com.cosmomedia.podfex.enums.StatusUser;
import lombok.*;

import java.util.List;

/**
 * DTO for {@link com.cosmomedia.podfex.entities.Orders}
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdersDto{
    Long id;
    String preview;
    String orderNo;
    Double price;
    StatusUser statusUser;
    StatusAdmin statusAdmin;
    Integer quantity;
    String type;
    Boolean confirmed;
    UsersDto personnel;
    ProductDto product;
    Client client;
    List<String> images;
    OrdersDto order;
    List<OrderItem> orderItems;
}