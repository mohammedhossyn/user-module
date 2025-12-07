package com.usermodule.dto.event;

import lombok.Builder;

import java.util.Date;

@Builder
public record EventResponseDTO(
        Long eventId,
        Long userId,
        String title,
        String message,
        String type,
        String status,
        Long referenceId,
        Date createdDate,
        String createdDateStr,
        String createdTimeStr) {
}