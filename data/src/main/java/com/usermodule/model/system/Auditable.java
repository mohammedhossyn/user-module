package com.usermodule.model.system;

import com.usermodule.listener.audit.AuditListener;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;

@Setter
@Getter
//@EqualsAndHashCode(onlyExplicitlyIncluded = true)
//@ToString
@MappedSuperclass
@EntityListeners({AuditingEntityListener.class, AuditListener.class})
public class Auditable<T, U> implements Serializable {

    @CreatedDate
    @Column(name = "AUDIT_CREATED_DATE", columnDefinition = "date", updatable = false)
    private T auditCreatedDate;

    @CreatedBy
    @OneToOne(fetch = FetchType.LAZY)//cascade = CascadeType.ALL
    @JoinColumn(name = "AUDIT_CREATED_BY", referencedColumnName = "user_id", updatable = false)
    private U auditCreatedBy;

    @LastModifiedDate
    @Column(name = "AUDIT_MODIFIED_DATE", columnDefinition = "date")
    private T auditModifiedDate;

    @LastModifiedBy
    @OneToOne(fetch = FetchType.LAZY)//cascade = CascadeType.ALL,
    @JoinColumn(name = "AUDIT_MODIFIED_BY", referencedColumnName = "user_id")
    private U auditModifiedBy;
}