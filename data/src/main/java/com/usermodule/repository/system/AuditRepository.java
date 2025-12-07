package com.usermodule.repository.system;

import com.usermodule.model.system.AuditEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

import java.util.List;

public interface AuditRepository extends JpaRepository<AuditEntity, Long>, ListPagingAndSortingRepository<AuditEntity, Long> {

    List<AuditEntity> findByEntityAndReferenceIdOrderByActionDateAsc(String entity, Long referenceId);

}
