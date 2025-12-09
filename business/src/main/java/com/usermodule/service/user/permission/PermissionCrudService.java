package com.usermodule.service.user.permission;

import com.usermodule.dto.user.permission.PermissionAddRequestDTO;
import com.usermodule.dto.user.permission.PermissionAddToRoleRequestDTO;
import com.usermodule.dto.user.permission.PermissionResponseDTO;
import com.usermodule.dto.user.permission.PermissionSearchRequestDTO;
import com.usermodule.exception.BusinessException;
import com.usermodule.mapper.user.permission.PermissionDTOMapper;
import com.usermodule.model.user.PermissionEntity;
import com.usermodule.repository.user.PermissionRepository;
import com.usermodule.repository.user.RoleRepository;
import com.usermodule.utils.TransactionUtil;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PermissionCrudService {

    private final PermissionRepository permissionRepository;
    private final PermissionDTOMapper permissionDTOMapper;
    private final RoleRepository roleRepository;
    private final TransactionUtil transactionUtil;

    public PermissionResponseDTO add(PermissionAddRequestDTO permissionAddRequestDTO) {
        log.debug("PermissionCrudService.add started");
        try {
            transactionUtil.openTransaction();
            if (!permissionRepository.existsByCode(permissionAddRequestDTO.code())) {
                var permission = PermissionEntity.builder().code(permissionAddRequestDTO.code())
                        .description(permissionAddRequestDTO.description()).build();
                if (permissionAddRequestDTO.parentId() != null) {
                    log.debug("permissionAddRequestDTO.parentId() {}", permissionAddRequestDTO.parentId());
                    var parent = permissionRepository.findById(permissionAddRequestDTO.parentId()).orElseThrow();
                    permission.setPermissionParentEntity(parent);
                }
                var savedPermissionEntity = permissionRepository.save(permission);
                if (permissionAddRequestDTO.roleId() != null) {
                    log.debug("permissionAddRequestDTO.roleId() {}", permissionAddRequestDTO.roleId());
                    var role = roleRepository.findById(permissionAddRequestDTO.roleId()).orElseThrow();
                    role.getPermissions().add(permission);
                    roleRepository.save(role);
                }
                log.debug("PermissionCrudService.add ended");
                transactionUtil.commit();
                return PermissionResponseDTO.builder().permissionId(savedPermissionEntity.getPermissionId())
                        .code(savedPermissionEntity.getCode()).description(savedPermissionEntity.getDescription())
                        .permissionParent(savedPermissionEntity.getPermissionParentEntity() != null ?
                                permissionDTOMapper.apply(savedPermissionEntity.getPermissionParentEntity())
                                : null).build();
            } else {
                log.info("action {} is already in use", permissionAddRequestDTO.code());
                throw BusinessException.builder().name("add action").message("err.permissionIsAlreadyInUse")
                        .description("permission is already in use").code(null).field("code")
                        .value(permissionAddRequestDTO.code()).build();
            }
        } catch (Exception e) {
            transactionUtil.rollback();
            throw new BusinessException(e);
        }
    }

    public PermissionResponseDTO update(Long id, PermissionAddRequestDTO permissionAddRequestDTO) {
        log.debug("PermissionCrudService.update started");
        var permissionOptional = permissionRepository.findById(id);
        if (permissionOptional.isEmpty()) {
            log.info("Permission Entity id = {} not found", id);
            throw BusinessException.builder()
                    .name("permission user")
                    .message("err.permissionNotFound")
                    .description("Permission Not Found").code(null).field("id").value(id).build();
        }
        var permissionEntity = permissionOptional.get();

        boolean changes = false;

        if (!permissionEntity.getCode().equals(permissionAddRequestDTO.code())) {
            if (permissionRepository.existsByCode(permissionAddRequestDTO.code())) {
                log.info("permission code {} is already in use", permissionAddRequestDTO.code());
                throw BusinessException.builder().name("update permission").message("err.permissionCodeIsAlreadyInUse")
                        .description("permission code is already in use").code(null).field("code")
                        .value(permissionAddRequestDTO.code()).className(this.getClass().getName())
                        .line(Thread.currentThread().getStackTrace()[1].getLineNumber()).build();
            }
            permissionEntity.setCode(permissionAddRequestDTO.code());
            changes = true;
        }

        if (permissionAddRequestDTO.parentId() != null) {
            log.debug("permissionAddRequestDTO.parentId() {}", permissionAddRequestDTO.parentId());
            var parent = permissionRepository.findById(permissionAddRequestDTO.parentId()).orElseThrow();
            permissionEntity.setPermissionParentEntity(parent);
            changes = true;
        } else {
            if (permissionEntity.getPermissionParentEntity() != null && permissionEntity.getPermissionParentEntity()
                    .getCode() != null) {
                permissionEntity.setPermissionParentEntity(null);
                changes = true;
            }
        }

        if (!permissionEntity.getDescription().equals(permissionAddRequestDTO.description())) {
            if (permissionRepository.existsByDescription(permissionAddRequestDTO.description())) {
                log.info("permission description {} is already in use", permissionAddRequestDTO.description());
                throw BusinessException.builder().name("update permission").message("err.permissionDescriptionIsAlreadyInUse")
                        .description("permission description is already in use").code(null).field("description")
                        .value(permissionAddRequestDTO.description()).className(this.getClass().getName())
                        .line(Thread.currentThread().getStackTrace()[1].getLineNumber()).build();
            }
            permissionEntity.setDescription(permissionAddRequestDTO.description());
            changes = true;
        }

        var updatedPermission = permissionEntity;
        if (changes) {
            updatedPermission = permissionRepository.save(updatedPermission);
            if (permissionAddRequestDTO.roleId() != null) {
                log.debug("permissionAddRequestDTO.roleId() {}", permissionAddRequestDTO.roleId());
                var role = roleRepository.findById(permissionAddRequestDTO.roleId()).orElseThrow();
                role.getPermissions().add(updatedPermission);
                roleRepository.save(role);
            }
        }
        log.debug("PermissionCrudService.findAllParents ended");
        return permissionDTOMapper.apply(updatedPermission);
    }

    public Page<PermissionEntity> findAllParents(Pageable pageable) {
        log.debug("PermissionCrudService.findAllParents started");
        return permissionRepository.findAllByPermissionParentEntityIsNull(pageable);
    }

    @Transactional
    public Page<PermissionEntity> search(Pageable pageable,
                                               PermissionSearchRequestDTO permissionSearchRequestDTO) {
        log.debug("PermissionCrudService.search started");
        var permissions = permissionRepository.search(pageable, permissionSearchRequestDTO);
        for (PermissionEntity permission : permissions) {
            if (permission.getPermissionParentEntity() != null) {
                Hibernate.initialize(permission.getPermissionParentEntity());
            }
        }
        log.debug("PermissionCrudService.search ended");
        return permissions;
    }

    public PermissionResponseDTO addToRole(@NonNull PermissionAddToRoleRequestDTO permissionAddToRoleRequestDTO) {
        log.debug("PermissionCrudService.addToRole started");
       if (permissionAddToRoleRequestDTO.id() != null && permissionRepository.existsById(permissionAddToRoleRequestDTO.id())) {
           log.debug("permissionAddToRoleRequestDTO.id() {}", permissionAddToRoleRequestDTO.id());
           if (permissionAddToRoleRequestDTO.roleId() != null && roleRepository.existsById(permissionAddToRoleRequestDTO.roleId())) {
               var permission = permissionRepository.findById(permissionAddToRoleRequestDTO.id()).orElseThrow();
               var role = roleRepository.findById(permissionAddToRoleRequestDTO.roleId()).orElseThrow();
               role.getPermissions().add(permission);
               roleRepository.save(role);
               log.debug("PermissionCrudService.addToRole ended");
               return permissionDTOMapper.apply(permission);
           } else {
               throw new BusinessException("err.role.does.not.exist");
           }
       } else {
           throw new BusinessException("err.permission.does.not.exist");
       }
    }
}
