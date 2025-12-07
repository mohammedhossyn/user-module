package com.usermodule.dto.system;

import lombok.Builder;

@Builder
public record FormResponseDTO(
        String controlType,
        String controlName,
        String inputType,
        String label,
        String value,
        String placeHolder,
        Integer sort,
        Integer useApi,
        String policy) {
}
