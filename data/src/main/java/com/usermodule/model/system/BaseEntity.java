package com.usermodule.model.system;

import com.usermodule.model.user.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@SuperBuilder
@EntityListeners({AuditingEntityListener.class})
public class BaseEntity {

    @CreatedDate
    @Column(name = "CREATED_DATE", columnDefinition = "date", nullable = false,  updatable = false)
    private Date createdDate;

    @CreatedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CREATED_BY", referencedColumnName = "user_id", updatable = false)
    private UserEntity createdBy;

    @LastModifiedDate
    @Column(name = "LAST_MODIFIED_DATE", columnDefinition = "date")
    private Date lastModifiedDate;

    @LastModifiedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LAST_MODIFIED_BY", referencedColumnName = "user_id")
    private UserEntity lastModifiedBY;

    @Version
    @Column(name = "VERSION", columnDefinition = "number(38)",  nullable = false)
    private Long version;
}
