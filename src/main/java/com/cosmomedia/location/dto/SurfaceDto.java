package com.cosmomedia.location.dto;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.cosmomedia.location.entities.Surface}
 */
@Value
public class SurfaceDto implements Serializable {
    Long id;
    String front;
    String back;
}