package com.usermodule.model.user;

import com.usermodule.model.system.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PERMISSION_TABLE")
public class PermissionEntity extends BaseEntity {

    @Id
    @Column(name = "PERMISSION_ID", columnDefinition = "NUMBER(38)", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PERMISSION_SEQ")
    @SequenceGenerator(sequenceName = "PERMISSION_SEQ", allocationSize = 1, name = "PERMISSION_SEQ")
    private Long permissionId;

    @Column(name = "CODE", columnDefinition = "NVARCHAR2(200)", nullable = false)
    private String code;

    @Column(name = "DESCRIPTION", columnDefinition = "NVARCHAR2(2000)")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID", columnDefinition = "NUMBER(38)")
    private PermissionEntity permissionParentEntity;
}