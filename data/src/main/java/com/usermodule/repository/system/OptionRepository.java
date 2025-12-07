package com.usermodule.repository.system;

import com.usermodule.model.system.OptionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListPagingAndSortingRepository;

public interface OptionRepository extends JpaRepository<OptionEntity, Long>, ListPagingAndSortingRepository<OptionEntity, Long> {
    @Query(value = " SELECT so " +
            " FROM OptionEntity so " +
            " WHERE (:code is null or so.code LIKE CONCAT('%', :code, '%')) " +
            " and (:value is null or so.value LIKE CONCAT('%', :value, '%')) " +
            " and (:description is null or so.description LIKE CONCAT('%', :description, '%')) " +
            " order by so.code ")
    Page<OptionEntity> findOptionEntity(Pageable pageable, String description, String code, String value);

}
