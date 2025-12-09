package com.usermodule.controller.user;

import com.usermodule.dto.common.ApiResponseDTO;
import com.usermodule.dto.common.PaginationRequestDTO;
import com.usermodule.dto.user.permission.PermissionAddRequestDTO;
import com.usermodule.dto.user.permission.PermissionAddToRoleRequestDTO;
import com.usermodule.dto.user.permission.PermissionSearchRequestDTO;
import com.usermodule.inspector.ApiResponseInspector;
import com.usermodule.service.user.permission.PermissionService;
import jakarta.validation.Valid;
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
@RequestMapping("/user/permission")
public class PermissionController {

    private final PermissionService permissionService;
    private final ApiResponseInspector apiResponseInspector;

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('Manager') or hasAuthority('Permission Managemant') or hasAuthority('Add Permission')")
    public ApiResponseDTO add(@Valid @RequestBody PermissionAddRequestDTO permissionAddRequestDTO) {
        var permissionAddResponseDTO = permissionService.add(permissionAddRequestDTO);
        return apiResponseInspector.apiResponseBuilder(permissionAddResponseDTO, "",
                true);
    }

    @PostMapping("/add/role")
    @PreAuthorize("hasAuthority('Manager') or hasAuthority('Permission Managemant') or hasAuthority('Add Permission To Role')")
    public ApiResponseDTO addToRole(@Valid @RequestBody PermissionAddToRoleRequestDTO permissionAddToRoleRequestDTO) {
        var permissionAddResponseDTO = permissionService.addToRole(permissionAddToRoleRequestDTO);
        return apiResponseInspector.apiResponseBuilder(permissionAddResponseDTO, "",
                true);
    }

    @PostMapping("/search")
    @PreAuthorize("hasAuthority('Manager') or hasAuthority('Permission Managemant') or hasAuthority('Search Permission')")
    public ApiResponseDTO search(@NonNull PaginationRequestDTO paginationRequestDTO,
                                 @RequestBody PermissionSearchRequestDTO permissionSearchRequestDTO) {
        var listDTO = permissionService.search(paginationRequestDTO.getPageable(), permissionSearchRequestDTO);
        return apiResponseInspector.apiResponseBuilder(listDTO, "",
                true);
    }

    @GetMapping("/parents")
    @PreAuthorize("hasAuthority('Manager') or hasAuthority('Permission Managemant') or hasAuthority('Permission Parents')")
    public ApiResponseDTO getAllParents(@NonNull PaginationRequestDTO paginationRequestDTO) {
        var listDTO = permissionService.findAllParents(paginationRequestDTO.getPageable());
        return apiResponseInspector.apiResponseBuilder(listDTO, "",
                true);
    }

    @GetMapping("/listTree")
    @PreAuthorize("hasAuthority('Manager') or hasAuthority('Permission Managemant') or hasAuthority('Permission Tree List')")
    public ApiResponseDTO listTree() {
        var listDTO = permissionService.findAll();
        return apiResponseInspector.apiResponseBuilder(listDTO, "",
                true);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('Manager') or hasAuthority('Permission Managemant') or hasAuthority('Update Permission')")
    public ApiResponseDTO userUpdate(@NonNull @PathVariable Long id, @Valid @RequestBody PermissionAddRequestDTO permissionAddRequestDTO) {
        var permissionResponseDTO = permissionService.update(id, permissionAddRequestDTO);
        return apiResponseInspector.apiResponseBuilder(permissionResponseDTO, "",
                true);
    }
}