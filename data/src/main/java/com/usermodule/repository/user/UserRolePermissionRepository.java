package com.usermodule.repository.user;

import com.usermodule.model.user.PermissionEntity;
import com.usermodule.model.user.UserEntity;
import com.usermodule.model.user.UserRolePermissionEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListPagingAndSortingRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserRolePermissionRepository extends JpaRepository<UserRolePermissionEntity, Long>, ListPagingAndSortingRepository<UserRolePermissionEntity, Long> {
    Optional<UserRolePermissionEntity> findByUser_UserIdAndRole_RoleId(Long userId, Long roleId);

    @Query("SELECT urp FROM UserRolePermissionEntity urp JOIN urp.permissions p " +
            "   WHERE urp.user = :user AND p IN :permissions")
    List<UserRolePermissionEntity> findByUserAndPermissionsIn(
            UserEntity user,
            Collection<PermissionEntity> permissions
    );

    @EntityGraph(attributePaths = {"permissionEntity"})
    List<UserRolePermissionEntity> findWithPermissionsByUser_UserId(Long id);
}
