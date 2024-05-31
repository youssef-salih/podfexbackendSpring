package com.cosmomedia.location.dto;

import lombok.Value;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link com.cosmomedia.location.entities.Product}
 */
@Value
public class ProductDto implements Serializable {
    Long id;
    String name;
    Double price;
    SurfaceDto surface;
    List<OrdersDto> orders;
}