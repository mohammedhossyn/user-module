package com.usermodule.dto.auth;

import lombok.Builder;

import java.util.Date;

@Builder
public record LoginResponseDTO
        (String token,
         Date expirationDate) {
}
