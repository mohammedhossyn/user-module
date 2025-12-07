package com.usermodule.model.user;

import com.usermodule.model.system.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@SuperBuilder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ROLE_TABLE")
public class RoleEntity extends BaseEntity {

    @Id
    @Column(name = "ROLE_ID", columnDefinition = "NUMBER(38)")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROLE_SEQ")
    @SequenceGenerator(sequenceName = "ROLE_SEQ", allocationSize = 1, name = "ROLE_SEQ")
    private Long roleId;

    @Column(name = "NAME", columnDefinition = "NVARCHAR2(200)", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION", columnDefinition = "NVARCHAR2(2000)")
    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<PermissionEntity> permissions = new HashSet<>();
}
