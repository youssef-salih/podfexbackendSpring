package com.cosmomedia.location.dto;

import jakarta.persistence.Entity;
import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link com.cosmomedia.location.entities.Surface}
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