package com.usermodule.dto.user;

import lombok.Builder;

@Builder
public record UserSearchRequestDTO
        (String username,
         String mobileNumber,
         String status) {
}
