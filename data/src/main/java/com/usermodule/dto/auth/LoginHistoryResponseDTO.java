package com.usermodule.dto.auth;

import lombok.Builder;

import java.util.Date;

@Builder
public record LoginHistoryResponseDTO
        (Long loginHistoryId,
         String requestIp,
         String hostAddress,
         String hostName,
         String username,
         Date createdDate) {
}
