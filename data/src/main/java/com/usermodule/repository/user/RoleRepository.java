package com.usermodule.repository.user;

import com.usermodule.dto.user.role.RoleSearchRequestDTO;
import com.usermodule.model.user.RoleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListPagingAndSortingRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Long>, ListPagingAndSortingRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByName(String name);
    boolean existsByName(String name);
    boolean existsByDescription(String description);

    @Query(" SELECT r " +
            " FROM RoleEntity r " +
            " WHERE (:#{#roleSearchDTO.name} is null or r.name LIKE CONCAT('%', :#{#roleSearchDTO.name},'%')) " +
            " AND  (:#{#roleSearchDTO.description} is null or r.description LIKE CONCAT('%', :#{#roleSearchDTO.description},'%')) ")
    Page<RoleEntity> search(Pageable pageable, RoleSearchRequestDTO roleSearchDTO);
}
