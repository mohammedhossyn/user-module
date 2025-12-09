package com.usermodule.repository.user;

import com.usermodule.model.user.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListPagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long>, ListPagingAndSortingRepository<UserEntity, Long> {

    @Query(value = " SELECT su " +
            " FROM UserEntity su " +
            " WHERE (:#{#userEntity.username} is null or su.username LIKE CONCAT('%', :#{#userEntity.username},'%')) " +
            " and (:#{#userEntity.mobileNumber} is null or su.mobileNumber LIKE CONCAT('%', :#{#userEntity.mobileNumber},'%')) " +
            " and (:#{#userEntity.email} is null or su.email LIKE CONCAT('%', :#{#userEntity.email},'%')) " +
            " and (:#{#userEntity.nationalId} is null or su.nationalId LIKE CONCAT('%', :#{#userEntity.nationalId},'%')) " +
            " and (:#{#userEntity.firstName} is null or su.firstName LIKE CONCAT('%', :#{#userEntity.firstName},'%')) " +
            " and (:#{#userEntity.lastName} is null or su.lastName LIKE CONCAT('%', :#{#userEntity.lastName},'%')) " +
            " and (:#{#userEntity.statusForSearch} is null or su.status LIKE CONCAT('%', :#{#userEntity.statusForSearch},'%')) " +
            " order by su.userId ")
    Page<UserEntity> search(Pageable pageable, UserEntity userEntity);

    boolean existsByUsername(String username);

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByUserIdAndUsername(Long id, String username);


    @Query(" SELECT urp.user FROM UserRolePermissionEntity urp JOIN urp.permissions p " +
            " WHERE p.code IN :codes")
    List<UserEntity> findByPermissionCodes(List<String> codes);

    @Query("SELECT DISTINCT urp.user su FROM UserRolePermissionEntity urp JOIN urp.permissions p " +
            "   WHERE p.code IN :codes ")
    Page<UserEntity> findByPermissionCodes(Pageable pageable, List<String> codes);

    Optional<UserEntity> findByUserIdAndUserRolePermissions_Role_Name(Long userId, String userRolePermissionsRoleName);

    @Query(" SELECT su " +
            " FROM UserEntity su " +
            " WHERE (:#{#username} is null or su.username = :#{#username}) " +
            " AND su.userId <> :#{#id}")
    Optional<UserEntity> existByUsernameAndNotThisId(Long id, String username);
}
