package com.usermodule.dto.system;

import lombok.Builder;

import java.util.Date;
@Builder

public record GeneralHistoryResponseDTO(
        Integer changeOrigin,
        Date changeDate,
        String changedBy,
        String tableField,
        String oldValue,
        String newValue) {
}

