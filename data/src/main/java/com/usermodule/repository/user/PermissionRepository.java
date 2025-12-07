package com.usermodule.repository.user;

import com.usermodule.dto.user.permission.PermissionSearchRequestDTO;
import com.usermodule.model.user.PermissionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListPagingAndSortingRepository;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<PermissionEntity, Long>, ListPagingAndSortingRepository<PermissionEntity, Long> {
    boolean existsByCode(String code);

    boolean existsByDescription(String description);

    Optional<PermissionEntity> findByCode(String code);

    Page<PermissionEntity> findAllByPermissionParentEntityIsNull(Pageable pageable);

    @Query(" SELECT sp " +
            " FROM PermissionEntity sp " +
            " WHERE (:#{#permissionDTO.description} IS NULL OR sp.description LIKE CONCAT('%', :#{#permissionDTO.description},'%')) " +
            " AND (:#{#permissionDTO.parentId} IS NULL OR sp.permissionParentEntity.permissionId = :#{#permissionDTO.parentId}) " +
            " AND (:#{#permissionDTO.roleId} IS NULL OR EXISTS (SELECT 1 FROM RoleEntity r JOIN r.permissions p " +
            "   WHERE p.permissionId = sp.permissionId AND r.roleId = :#{#permissionDTO.roleId})) ")
    Page<PermissionEntity> search(Pageable pageable, PermissionSearchRequestDTO permissionDTO);
}