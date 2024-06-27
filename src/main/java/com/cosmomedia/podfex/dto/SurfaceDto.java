package com.cosmomedia.podfex.dto;

import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link com.cosmomedia.podfex.entities.Surface}
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class SurfaceDto implements Serializable {
    Long id;
    String front;
    String back;
}