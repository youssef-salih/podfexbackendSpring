package com.cosmomedia.location.dto;

import com.cosmomedia.location.enums.Sizes;
import lombok.*;

import java.io.Serializable;
import java.util.List;

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
    List<String> sizes;
    Boolean active;
}