package com.cosmomedia.podfex.dto;

import lombok.*;

import java.util.List;

/**
 * DTO for {@link com.cosmomedia.podfex.entities.Product}
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
    List<String> sizes;
    List<String> colors;
    Boolean active;
}