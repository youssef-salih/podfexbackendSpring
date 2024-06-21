package com.cosmomedia.location.dto;

import com.cosmomedia.location.enums.Sizes;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;

@Data
@Builder
public class OrderItemDto {
    Sizes size;
    Integer quantity;
}