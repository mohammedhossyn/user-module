package com.usermodule.service.user.role;

import com.usermodule.dto.common.PageResponseWithHeaderDTO;
import com.usermodule.dto.user.permission.PermissionResponseDTO;
import com.usermodule.dto.user.role.RoleAddRequestDTO;
import com.usermodule.dto.user.role.RoleResponseDTO;
import com.usermodule.dto.user.role.RoleAddResponseDTO;
import com.usermodule.dto.user.role.RoleSearchRequestDTO;
import com.usermodule.exception.BusinessException;
import com.usermodule.mapper.user.permission.PermissionDTOMapper;
import com.usermodule.mapper.user.role.RoleDTOMapper;
import com.usermodule.model.user.PermissionEntity;
import com.usermodule.model.user.RoleEntity;
import com.usermodule.repository.user.PermissionRepository;
import com.usermodule.repository.user.RoleRepository;
import com.usermodule.utils.PaginationUtil;
import com.usermodule.utils.TransactionUtil;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleCrudService roleCrudService;
    private final PermissionRepository permissionRepository;
    private final TransactionUtil transactionUtil;
    private final PermissionDTOMapper permissionDTOMapper;
    private final RoleDTOMapper roleDTOMapper;
    private final PaginationUtil<RoleResponseDTO> paginationUtil;

    public RoleResponseDTO add(@NonNull RoleAddRequestDTO roleAddRequestDTO) {
        log.debug("RoleService.add started");
        try {
            transactionUtil.openTransaction();
            if (!roleRepository.existsByName(roleAddRequestDTO.name())) {
                var roleEntity = RoleEntity.builder()
                        .name(roleAddRequestDTO.name())
                        .description(roleAddRequestDTO.description())
                        .build();

                if (roleAddRequestDTO.permissionsId() != null && !roleAddRequestDTO.permissionsId().isEmpty()) {
                    log.debug("permissionsId : {}", roleAddRequestDTO.permissionsId().size());
                    Set<PermissionEntity> permissionEntities = new HashSet<>();
                    for (Long id : roleAddRequestDTO.permissionsId()) {
                        var permission = permissionRepository.findById(id);
                        permission.ifPresent(permissionEntities::add);
                    }
                    roleEntity.setPermissions(permissionEntities);
                }

                var savedGroupEntity = roleCrudService.create(roleEntity);
                transactionUtil.commit();
                log.debug("RoleService.add ended");
                return RoleResponseDTO.builder().roleId(savedGroupEntity.getRoleId())
                        .name(savedGroupEntity.getName())
                        .description(savedGroupEntity.getDescription())
                        .permissions(Optional.ofNullable(savedGroupEntity.getPermissions())
                                .orElseGet(Collections::emptySet).stream().map(permissionDTOMapper).collect(Collectors.toSet()))
                        .build();
            } else {
                log.info("role {} is already in use", roleAddRequestDTO.name());
                throw BusinessException.builder().name("add role").message("err.roleIsAlreadyInUse")
                        .description("role is already in use").code(null).field("username")
                        .value(roleAddRequestDTO.name()).build();
            }

        } catch (Exception e) {
            transactionUtil.rollback();
            throw new BusinessException(e);
        }
    }

    @Transactional
    public Page<RoleResponseDTO> findAll(@NonNull Pageable pageable) {
        log.debug("RoleService.findAll pageable started");
        return roleRepository.findAll(pageable)
                .map(role -> RoleResponseDTO.builder().roleId(role.getRoleId()).name(role.getName())
                        .description(role.getDescription())
                        .permissions(Optional.ofNullable(role.getPermissions())
                                .orElseGet(Collections::emptySet).stream().map(permission ->
                                        PermissionResponseDTO.builder()
                                                .permissionId(permission.getPermissionId())
                                                .code(permission.getCode())
                                                .description(permission.getDescription())
                                                .permissionParent(permissionDTOMapper.apply(permission.getPermissionParentEntity()))
                                                .build())
                                .collect(Collectors.toSet())).build());
    }

    public List<RoleAddResponseDTO> findAll() {
        log.debug("RoleService.findAll started");
        return roleRepository.findAll().stream()
                .map(role -> RoleAddResponseDTO.builder().name(role.getName()).description(role.getDescription())
                        .build()).collect(Collectors.toList());
    }

    public RoleResponseDTO update(@NonNull Long id, @NonNull RoleAddRequestDTO roleAddRequestDTO) {
        log.debug("RoleService.update started");
        try {
            transactionUtil.openTransaction();
            var roleOptional = roleRepository.findById(id);
            if (roleOptional.isEmpty()) {
                log.info("role Entity id = {} not found", id);
                throw BusinessException.builder().name("role user").message("err.roleNotFound")
                        .description("role Not Found").code(null).field("id").value(id).build();
            }
            var roleEntity = roleOptional.get();

            boolean changes = false;

            if (!roleEntity.getName().equals(roleAddRequestDTO.name())) {
                if (roleRepository.existsByName(roleAddRequestDTO.name())) {
                    log.info("role name {} is already in use", roleAddRequestDTO.name());
                    throw BusinessException.builder().name("update role").message("err.roleNameIsAlreadyInUse")
                            .description("role name is already in use").code(null).field("name")
                            .value(roleAddRequestDTO.name()).className(this.getClass().getName())
                            .line(Thread.currentThread().getStackTrace()[1].getLineNumber()).build();
                }
                roleEntity.setName(roleAddRequestDTO.name());
                changes = true;
            }

            if (!roleEntity.getDescription().equals(roleAddRequestDTO.description())) {
                if (roleRepository.existsByDescription(roleAddRequestDTO.description())) {
                    log.info("role description {} is already in use", roleAddRequestDTO.description());
                    throw BusinessException.builder().name("update role").message("err.roleDescriptionIsAlreadyInUse")
                            .description("role description is already in use").code(null).field("description")
                            .value(roleAddRequestDTO.description()).className(this.getClass().getName())
                            .line(Thread.currentThread().getStackTrace()[1].getLineNumber()).build();
                }
                roleEntity.setDescription(roleAddRequestDTO.description());
                changes = true;
            }

            if (roleAddRequestDTO.permissionsId() != null && !roleAddRequestDTO.permissionsId().isEmpty()) {
                log.debug("permissionsId {}", roleAddRequestDTO.permissionsId());
                Set<PermissionEntity> permissionEntities = new HashSet<>();
                for (Long permissionId : roleAddRequestDTO.permissionsId()) {
                    var permission = permissionRepository.findById(permissionId);
                    permission.ifPresent(permissionEntities::add);
                }
                roleEntity.setPermissions(permissionEntities);
                changes = true;
            } else {
                if (roleEntity.getPermissions() != null && !roleEntity.getPermissions().isEmpty()) {
                    roleEntity.setPermissions(null);
                    changes = true;
                }
            }

            var updatedRole = roleEntity;
            if (changes) {
                updatedRole = roleCrudService.update(updatedRole);
            }
            transactionUtil.commit();
            log.debug("RoleService.update ended");
            return RoleResponseDTO.builder().roleId(updatedRole.getRoleId())
                    .name(updatedRole.getName()).description(updatedRole.getDescription())
                    .permissions(Optional.ofNullable(updatedRole.getPermissions())
                            .orElseGet(Collections::emptySet).stream().map(permissionDTOMapper)
                            .collect(Collectors.toSet())).build();
        } catch (Exception e) {
            transactionUtil.rollback();
            throw new BusinessException(e);
        }
    }

    public PageResponseWithHeaderDTO<RoleResponseDTO> search(Pageable pageable, RoleSearchRequestDTO roleSearchRequestDTO) {
        log.debug("RoleService.search started");
        Page<RoleResponseDTO> pages = roleCrudService.search(pageable, roleSearchRequestDTO).map(roleDTOMapper);
        return paginationUtil.getHeader(pages, RoleResponseDTO.class);
    }
}