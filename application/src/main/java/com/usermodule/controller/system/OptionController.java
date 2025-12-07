package com.usermodule.controller.system;

import com.usermodule.dto.common.ApiResponseDTO;
import com.usermodule.dto.common.PaginationRequestDTO;
import com.usermodule.dto.system.OptionRequestDTO;
import com.usermodule.inspector.ApiResponseInspector;
import com.usermodule.service.system.OptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@EnableMethodSecurity
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/option")
public class OptionController {
    private final OptionService optionService;
    private final ApiResponseInspector apiResponseInspector;


    @PostMapping("/search")
    @PreAuthorize("hasAuthority('Manager') or hasAuthority('Option Management') or hasAuthority('Option List')")
    public ApiResponseDTO search(PaginationRequestDTO paginationRequestDTO,
                               @RequestBody OptionRequestDTO optionRequestDTO) {
        var listDTO = optionService.search(paginationRequestDTO
                .getPageable(),optionRequestDTO);
        return apiResponseInspector.apiResponseBuilder(listDTO, "");
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('Manager') or hasAuthority('Option Management') or hasAuthority('Update Option')")
    public ApiResponseDTO update(@PathVariable ("id") Long id,
                                 @RequestBody OptionRequestDTO optionRequestDTO) {
        var listDTO = optionService.update(id, optionRequestDTO);
        return apiResponseInspector.apiResponseBuilder(listDTO, "");
    }

    @GetMapping("/reloadOption")
    @PreAuthorize("hasAuthority('Manager') or hasAuthority('Option Management') or hasAuthority('Reload Option')")
    public ApiResponseDTO reloadOption() {
        optionService.reloadOptionMap();
        return apiResponseInspector.apiResponseBuilder(null, "");
    }
}
