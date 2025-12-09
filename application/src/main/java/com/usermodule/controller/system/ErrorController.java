package com.usermodule.controller.system;

import com.usermodule.dto.common.ApiResponseDTO;
import com.usermodule.dto.common.PaginationRequestDTO;
import com.usermodule.dto.system.ErrorSearchRequestDTO;
import com.usermodule.inspector.ApiResponseInspector;
import com.usermodule.service.system.ErrorService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@EnableMethodSecurity
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/error")
public class ErrorController {
    private final ErrorService errorService;
    private final ApiResponseInspector apiResponseInspector;

    @PostMapping("/search")
    @PreAuthorize("hasAuthority('Manager') or hasAuthority('Error Management') or hasAuthority('Search Errors')")
    public ApiResponseDTO search(PaginationRequestDTO paginationRequestDTO,
                                 @RequestBody ErrorSearchRequestDTO errorSearchRequestDTO) {
        var listDTO = errorService.search(paginationRequestDTO
                .getPageable(), errorSearchRequestDTO);
        return apiResponseInspector.apiResponseBuilder(listDTO, "",
                true);
    }

    @GetMapping("/getError/{errorId}")
    @PreAuthorize("hasAuthority('Manager') or hasAuthority('Error Management') or hasAuthority('Search Error')")
    public ApiResponseDTO getError(@NonNull @PathVariable Long errorId) {
        var errorDetailResponseDTO = errorService.getError(errorId);
        return apiResponseInspector.apiResponseBuilder(errorDetailResponseDTO, "",
                true);
    }

}
