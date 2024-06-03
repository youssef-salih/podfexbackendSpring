package com.cosmomedia.location.dto;

import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link com.cosmomedia.location.entities.Product}
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {
    Long id;
    String name;
    Double price;
    String image;
    SurfaceDto surface;
}