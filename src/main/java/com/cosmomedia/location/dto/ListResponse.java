package com.cosmomedia.location.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ListResponse<T> {
    private boolean success;
    private List<T> list;
    private String message;
}
