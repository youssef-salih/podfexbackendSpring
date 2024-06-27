package com.cosmomedia.podfex.dto;

import com.cosmomedia.podfex.enums.Colors;
import com.cosmomedia.podfex.enums.Sizes;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItemDto {
    Sizes size;
    Integer quantity;
    Colors color;
}