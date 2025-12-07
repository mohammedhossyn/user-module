package com.usermodule.service.user.permission;

import com.usermodule.dto.common.PageResponseWithHeaderDTO;
import com.usermodule.dto.user.permission.PermissionAddRequestDTO;
import com.usermodule.dto.user.permission.PermissionAddToRoleRequestDTO;
import com.usermodule.dto.user.permission.PermissionResponseDTO;
import com.usermodule.dto.user.permission.PermissionSearchRequestDTO;
import com.usermodule.dto.user.validation.PermissionData;
import com.usermodule.mapper.user.permission.PermissionDTOMapper;
import com.usermodule.repository.user.PermissionRepository;
import com.usermodule.utils.PaginationUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PermissionService {

    private final PermissionRepository permissionRepository;
    private final PermissionDTOMapper permissionDTOMapper;
    private final PermissionMakeTreeService permissionMakeTreeService;
    private final PermissionCrudService permissionCrudService;
    private final PaginationUtil<PermissionResponseDTO> paginationUtil;

    public PermissionResponseDTO add(@NonNull PermissionAddRequestDTO permissionAddRequestDTO) {
        log.debug("PermissionService.add started");
        return permissionCrudService.add(permissionAddRequestDTO);
    }

    public PermissionResponseDTO addToRole(@NonNull PermissionAddToRoleRequestDTO permissionAddToRoleRequestDTO) {
        log.debug("PermissionService.addToRole started");
        return permissionCrudService.addToRole(permissionAddToRoleRequestDTO);
    }

    public Page<PermissionResponseDTO> findAll(@NonNull Pageable pageable) {
        log.debug("PermissionService.findAll pageable started");
        return permissionRepository.findAll(pageable).map(permissionDTOMapper);
    }

    public List<PermissionData> findAll() {
        log.debug("PermissionService.findAll started");
        return permissionMakeTreeService.findAllPermissionTree();
    }

    public PermissionResponseDTO update(@NonNull Long id, @NonNull PermissionAddRequestDTO permissionAddRequestDTO) {
        log.debug("PermissionService.update started");
        return permissionCrudService.update(id, permissionAddRequestDTO);
    }

    public PageResponseWithHeaderDTO<PermissionResponseDTO> findAllParents(Pageable pageable) {
        log.debug("PermissionService.findAllParents started");
        Page<PermissionResponseDTO> pages =
                permissionCrudService.findAllParents(pageable).map(permissionDTOMapper);
        return paginationUtil.getHeader(pages, PermissionResponseDTO.class);
    }

    public PageResponseWithHeaderDTO<PermissionResponseDTO> search(Pageable pageable,
                                                                   PermissionSearchRequestDTO permissionSearchRequestDTO) {
        log.debug("PermissionService.search started");
        Page<PermissionResponseDTO> pages =
                permissionCrudService.search(pageable, permissionSearchRequestDTO).map(permissionDTOMapper);
        return paginationUtil.getHeader(pages, PermissionResponseDTO.class);
    }
}
