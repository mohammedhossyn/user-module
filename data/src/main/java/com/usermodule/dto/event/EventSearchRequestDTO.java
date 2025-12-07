package com.usermodule.dto.event;

import lombok.Builder;

@Builder
public record EventSearchRequestDTO(
        Long userId,
        String type,
        String status,
        String createdDateFrom,
        String createdDateTo) {
}
