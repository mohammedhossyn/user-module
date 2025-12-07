package com.usermodule.repository.system;

import com.usermodule.model.system.ErrorEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListPagingAndSortingRepository;

import java.util.Date;

public interface ErrorRepository extends JpaRepository<ErrorEntity, Long>, ListPagingAndSortingRepository<ErrorEntity, Long> {
    @Query(value = " SELECT se " +
            " FROM ErrorEntity se " +
            " WHERE (:errorId is null or se.errorId = :systemErrorId) " +
            " and (:username is null or se.username = :username) " +
            " and (:code is null or se.code = :code) " +
            " and (:requestIp is null or se.requestIp = :requestIp) " +
            " and (:hostAddress is null or se.hostAddress = :hostAddress) " +
            " and (:createdDateFrom is null or fn_trunc(se.createdDate) >= :createdDateFrom) " +
            " and (:createdDateTo is null or fn_trunc(se.createdDate) <= :createdDateTo) " +
            " and (:path is null or se.path like concat('%', :path, '%')) " +
            " order by se.errorId desc")
    Page<ErrorEntity> search(Pageable pageable, Long errorId, String username, Integer code,
                                   String requestIp, String hostAddress, Date createdDateFrom, Date createdDateTo,
                                   String path);


}
