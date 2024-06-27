package com.cosmomedia.podfex.authentication;

import com.cosmomedia.podfex.dto.UsersDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private String token;
    private UsersDto user;
    private String error;
}
