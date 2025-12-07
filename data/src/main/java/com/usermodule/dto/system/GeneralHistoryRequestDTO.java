package com.usermodule.dto.system;

import lombok.Builder;

@Builder

public record GeneralHistoryRequestDTO(
        String changedTable,
        Long tableId) {
}
