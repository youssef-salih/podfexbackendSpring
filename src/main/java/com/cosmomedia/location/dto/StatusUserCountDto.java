package com.cosmomedia.location.dto;

import com.cosmomedia.location.enums.StatusUser;
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