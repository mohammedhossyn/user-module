package com.usermodule.repository.event;

import com.usermodule.model.event.EventEntity;
import com.usermodule.model.event.EventStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListPagingAndSortingRepository;

import java.util.Date;

public interface EventRepository  extends JpaRepository<EventEntity, Long>, ListPagingAndSortingRepository<EventEntity, Long> {
    @Query(value = " SELECT e " +
            " FROM EventEntity e " +
            " WHERE ( e.user.userId = :userId) " +
            " and (:#{#type} is null or e.type LIKE CONCAT('%', :#{#type},'%')) " +
            " and (:#{#status} is null or e.status LIKE CONCAT('%', :#{#status},'%')) " +
            " and (:createdDateFrom is null or fn_trunc(e.createdDate) >= :createdDateFrom) " +
            " and (:createdDateTo is null or fn_trunc(e.createdDate) <= :createdDateTo) " +
            " order by e.status desc, e.eventId desc ")
    Page<EventEntity> search(Pageable pageable, Long userId, String type,
                             String status, Date createdDateFrom, Date createdDateTo);


    int countByUser_UserIdAndStatus(Long userId, EventStatus status);

}
