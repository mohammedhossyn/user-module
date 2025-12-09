package com.usermodule.controller.user;

import com.usermodule.dto.common.ApiResponseDTO;
import com.usermodule.dto.common.PaginationRequestDTO;
import com.usermodule.dto.user.role.RoleAddRequestDTO;
import com.usermodule.dto.user.role.RoleSearchRequestDTO;
import com.usermodule.inspector.ApiResponseInspector;
import com.usermodule.service.user.role.RoleService;
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
@RequestMapping("/user/role")
public class RoleController {

    private final RoleService roleService;
    private final ApiResponseInspector apiResponseInspector;

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('Manager') or hasAuthority('Role Managment') or hasAuthority('Add Role')")
    public ApiResponseDTO add(@Valid @RequestBody RoleAddRequestDTO roleAddRequestDTO) {
        var permissionAddResponseDTO = roleService.add(roleAddRequestDTO);
        return apiResponseInspector.apiResponseBuilder(permissionAddResponseDTO, "",
                true);
    }

    @PostMapping("/search")
    @PreAuthorize("hasAuthority('Manager') or hasAuthority('Role Managment') or hasAuthority('Search Role')")
    public ApiResponseDTO search(@NonNull PaginationRequestDTO paginationRequestDTO,
                                 @RequestBody RoleSearchRequestDTO roleSearchRequestDTO) {
        var listDTO = roleService.search(paginationRequestDTO.getPageable(), roleSearchRequestDTO);
        return apiResponseInspector.apiResponseBuilder(listDTO, "",
                true);
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('Manager') or hasAuthority('Role Managment') or hasAuthority('Role List')")
    public ApiResponseDTO listGroup() {
        var listDTO = roleService.findAll();
        return apiResponseInspector.apiResponseBuilder(listDTO, "",
                true);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('Manager') or hasAuthority('Role Managment') or hasAuthority('Update Role')")
    public ApiResponseDTO userUpdate(@NonNull @PathVariable Long id, @Valid @RequestBody RoleAddRequestDTO roleAddRequestDTO) {
        var groupResponseDTO = roleService.update(id, roleAddRequestDTO);
        return apiResponseInspector.apiResponseBuilder(groupResponseDTO, "",
                true);
    }
}
