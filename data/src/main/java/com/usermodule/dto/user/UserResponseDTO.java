package com.usermodule.dto.user;

import lombok.Builder;

@Builder
public record UserResponseDTO
        (Long userId,
         String username,
         String mobileNumber,
         String email,
         String nationalId,
         String firstName,
         String lastName,
         String fullName,
         String status) {
}
