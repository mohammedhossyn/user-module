package com.usermodule.dto.system;

import com.usermodule.dto.common.ApiErrorResponseDTO;
import lombok.Builder;

@Builder
public record ErrorAddRequestDTO(ApiErrorResponseDTO apiErrorResponseDTO,
                                 Long userId,
                                 String username,
                                 String requestIp,
                                 String hostName,
                                 String hostAddress,
                                 Boolean isOpenTrans,
                                 String stackTrace) {
}
