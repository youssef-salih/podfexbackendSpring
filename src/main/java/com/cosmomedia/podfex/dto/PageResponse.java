package com.cosmomedia.podfex.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
@Builder
public class PageResponse<T> {
    private boolean success;
    private Page<T> page;
    private String message;
}
