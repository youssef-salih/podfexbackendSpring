package com.cosmomedia.location.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OneResponse<T> {
    private boolean success;
    private T content;
    private String message;
}
