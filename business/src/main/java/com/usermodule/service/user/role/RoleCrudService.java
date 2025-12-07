package com.usermodule.service.user.role;

import com.usermodule.dto.user.role.RoleSearchRequestDTO;
import com.usermodule.exception.BusinessException;
import com.usermodule.model.user.PermissionEntity;
import com.usermodule.model.user.RoleEntity;
import com.usermodule.repository.user.RoleRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleCrudService {

    private final RoleRepository roleRepository;

    @Transactional
    public RoleEntity create(@NonNull RoleEntity roleEntity) {
        log.debug("RoleRepository.create started");
        if (roleEntity.getRoleId() == null) {
            var role = roleRepository.save(roleEntity);
            for (PermissionEntity permission : role.getPermissions()) {
                if (permission.getPermissionParentEntity() != null) {
                    Hibernate.initialize(permission.getPermissionParentEntity());
                }
            }
            return role;
        } else {
            throw new BusinessException(0);
        }
    }

    public RoleEntity read(@NonNull RoleEntity roleEntity) {
        log.debug("RoleRepository.read started");
        Optional<RoleEntity> optionalRole = roleRepository.findById(roleEntity.getRoleId());
        if (optionalRole.isPresent()) {
            return optionalRole.get();
        } else {
            throw new BusinessException(0);
        }
    }

    public RoleEntity update(@NonNull RoleEntity roleEntity) {
        log.debug("RoleRepository.update started");
        if (roleRepository.existsById(roleEntity.getRoleId())) {
            return roleRepository.save(roleEntity);
        } else {
            throw new BusinessException(0);
        }
    }

    public void delete(@NonNull RoleEntity roleEntity) {
        log.debug("RoleRepository.RoleEntity started");
        if (roleRepository.existsById(roleEntity.getRoleId())) {
            roleRepository.delete(roleEntity);
        } else {
            throw new BusinessException(0);
        }
    }

    public RoleEntity saveOrUpdate(@NonNull RoleEntity roleEntity) {
        log.debug("RoleRepository.saveOrUpdate started");
        return roleRepository.save(roleEntity);
    }

    public Page<RoleEntity> search(Pageable pageable, RoleSearchRequestDTO roleSearchRequestDTO) {
        log.debug("RoleRepository.search started");
        return roleRepository.search(pageable, roleSearchRequestDTO);
    }
}
