package com.usermodule.dto.system;

import lombok.Builder;

import java.util.Date;

@Builder
public record OptionResponseDTO(String description,
                                String code,
                                String value,
                                Long optionId,
                                Date modifiedDate) {
}
