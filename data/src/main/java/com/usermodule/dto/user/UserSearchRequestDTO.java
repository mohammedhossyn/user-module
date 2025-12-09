package com.usermodule.dto.user;

import lombok.Builder;

@Builder
public record UserSearchRequestDTO
        (String username,
         String mobileNumber,
         String email,
         String nationalId,
         String firstName,
         String lastName,
         String status) {
}
