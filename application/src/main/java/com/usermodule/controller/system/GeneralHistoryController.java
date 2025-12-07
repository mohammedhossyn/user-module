package com.usermodule.controller.system;

import com.usermodule.dto.common.ApiResponseDTO;
import com.usermodule.dto.common.PaginationRequestDTO;
import com.usermodule.dto.system.GeneralHistoryRequestDTO;
import com.usermodule.inspector.ApiResponseInspector;
import com.usermodule.service.system.GeneralHistoryService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@EnableMethodSecurity
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/generalHistory")
public class GeneralHistoryController {

    private final GeneralHistoryService generalHistoryService;
    private final ApiResponseInspector apiResponseInspector;

    @PostMapping("/search")
    @PreAuthorize("hasAuthority('Manager') or hasAuthority('General History Management') or hasAuthority('Search General History')")
    public ApiResponseDTO search(@NonNull PaginationRequestDTO paginationRequestDTO,
                                 @RequestBody GeneralHistoryRequestDTO generalHistoryRequestDTO) {
        var list = generalHistoryService
                .search(paginationRequestDTO.getPageable(), generalHistoryRequestDTO);
        return apiResponseInspector.apiResponseBuilder(list, "");
    }
}
