package com.usermodule.model.user;


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
@Table(name = "USER_ROLE_PERMISSION_TABLE")
public class UserRolePermissionEntity {

    @Id
    @Column(name = "USER_ROLE_PERMISSION_ID", columnDefinition = "NUMBER(38)", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_ROLE_PERMISSION_SEQ")
    @SequenceGenerator(sequenceName = "USER_ROLE_PERMISSION_SEQ", allocationSize = 1, name = "USER_ROLE_PERMISSION_SEQ")
    private Long userRolePermissionId;

    @ManyToOne(fetch = FetchType.EAGER)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.EAGER)
    private RoleEntity role;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role_permission_map",
            joinColumns = @JoinColumn(name = "user_role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private Set<PermissionEntity> permissions = new HashSet<>();

}
