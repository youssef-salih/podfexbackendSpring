package com.cosmomedia.podfex.dto;

import com.cosmomedia.podfex.enums.StatusUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusUserCountDto {
    private StatusUser status;
    private Long count;
}