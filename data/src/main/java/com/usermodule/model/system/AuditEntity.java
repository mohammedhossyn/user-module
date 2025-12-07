package com.usermodule.model.system;


import com.usermodule.model.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.LastModifiedBy;

import java.util.Date;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "AUDIT_TABLE")
public class AuditEntity {

    @Id
    @Column(name = "AUDIT_ID", columnDefinition = "NUMBER(38,0)")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AUDIT_SEQ")
    @SequenceGenerator(sequenceName = "AUDIT_SEQ", allocationSize = 1, name = "AUDIT_SEQ")
    private Long auditId;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "NVARCHAR2(100)")
    private OperationType operation;

    @Column(columnDefinition = "NVARCHAR2(2000)")
    private String payload;

    @Column(columnDefinition = "VARCHAR2(300)")
    private String entity;

    @Column(columnDefinition = "NUMBER(38,0)")
    private Long referenceId;

    @Column(name = "ACTION_DATE", columnDefinition = "DATE")
    private Date actionDate;

    @LastModifiedBy
    @OneToOne
    @JoinColumn(name = "ACTION_BY", referencedColumnName = "user_id", updatable = false)
    private UserEntity actionBy;
}
