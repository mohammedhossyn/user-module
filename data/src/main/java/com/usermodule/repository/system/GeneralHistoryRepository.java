package com.usermodule.repository.system;

import com.usermodule.model.system.GeneralHistoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListPagingAndSortingRepository;

import java.util.Collection;

public interface GeneralHistoryRepository extends JpaRepository<GeneralHistoryEntity, Long>, ListPagingAndSortingRepository<GeneralHistoryEntity, Long> {
    @Query(value = "SELECT sgh " +
            " FROM GeneralHistoryEntity sgh " +
            " WHERE sgh.tableId = :tableId " +
            " and sgh.changedTable = :changedTable " +
            " and (:excludeFields is null or sgh.tableField not in :excludeFields) " +
            " and sgh.tableField not in ('version', 'createdDate', 'createdBy', 'lastModifiedBy', 'actionDate', 'modifiedDate') ")
    Page<GeneralHistoryEntity> findGeneralChangeHistoryEntities(Pageable pageable,
                                                                      String changedTable,
                                                                      Long tableId,
                                                                      Collection<String> excludeFields);

}