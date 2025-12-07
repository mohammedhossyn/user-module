package com.usermodule.dto.user;

import lombok.Builder;

@Builder
public record UserResponseDTO
        (Long userId,
         String username,
         String mobileNumber,
         String status) {
}
