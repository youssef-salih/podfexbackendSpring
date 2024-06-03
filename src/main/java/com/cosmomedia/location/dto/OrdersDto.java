package com.cosmomedia.location.dto;

import com.cosmomedia.location.entities.Client;
import com.cosmomedia.location.entities.Orders;
import com.cosmomedia.location.enums.StatusAdmin;
import com.cosmomedia.location.enums.StatusUser;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link com.cosmomedia.location.entities.Orders}
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
    UsersDto personnel;
    ProductDto product;
    Client client;
    List<String> images;
    OrdersDto order;
}