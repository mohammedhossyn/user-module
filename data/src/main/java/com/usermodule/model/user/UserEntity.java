package com.usermodule.model.user;

import com.usermodule.model.system.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SuperBuilder
@EntityListeners({AuditingEntityListener.class})
@Table(name ="USER_TABLE")
public class UserEntity extends BaseEntity {

    @Id
    @Column(name = "USER_ID", columnDefinition = "NUMBER(38)", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_SEQ")
    @SequenceGenerator(sequenceName = "USER_SEQ", allocationSize = 1, name = "USER_SEQ")
    private Long userId;

    @Column(name = "USER_NAME", columnDefinition = "NVARCHAR2(30)", nullable = false, unique = true)
    private String username;

    @Column(name = "PASSWORD", columnDefinition = "NVARCHAR2(100)", nullable = false)
    private String password;

    @Column(name = "MOBILE_NUMBER", columnDefinition = "NVARCHAR2(11)", nullable = false)
    private String mobileNumber;

    @Email
    @Column(columnDefinition = "NVARCHAR2(100)", nullable = false)
    private String email;

    @Column(name = "NATIONAL_ID", columnDefinition = "NVARCHAR2(10)", nullable = false)
    private String nationalId;

    @Column(name = "FIRST_NAME", columnDefinition = "NVARCHAR2(50)", nullable = false)
    private String firstName;

    @Column(name = "LAST_NAME", columnDefinition = "NVARCHAR2(50)", nullable = false)
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "NVARCHAR2(100)", nullable = false)
    private UserStatus status;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<UserRolePermissionEntity> userRolePermissions = new HashSet<>();

    @Transient
    private String statusForSearch;

    public String getFullName(){
        String fullName = "";
        if (this.firstName != null || this.lastName != null){
            if (this.firstName != null && !this.firstName.isEmpty()) fullName = this.firstName;
            if (this.lastName != null && !this.lastName.isEmpty()) {
                if (fullName.isEmpty()) fullName = this.lastName;
                else fullName += " " +this.firstName;
            }
        } else {
            fullName = this.username;
        }
        return fullName;
    }
    
}