package com.cosmomedia.podfex.entities;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private boolean success;
    private String message;
}
