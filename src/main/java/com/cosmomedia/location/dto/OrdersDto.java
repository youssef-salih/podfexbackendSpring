package com.cosmomedia.location.dto;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.cosmomedia.location.entities.Orders}
 */
@Value
public class OrdersDto implements Serializable {
    Long id;
    Double amount;
}