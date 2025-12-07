package com.usermodule.model.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SuperBuilder
@Table(name = "FORGET_PASSWORD_CODE_TABLE")
@EntityListeners({AuditingEntityListener.class})
public class ForgetPasswordCodeEntity {

    @Id
    @Column(name = "FORGET_PASSWORD_CODE_ID", columnDefinition = "NUMBER(38)", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FORGET_PASSWORD_CODE_SEQ")
    @SequenceGenerator(sequenceName = "FORGET_PASSWORD_CODE_SEQ", allocationSize = 1, name = "FORGET_PASSWORD_CODE_SEQ")
    private Long forgetPasswordCodeId;

    @Column(name = "USERNAME", columnDefinition = "NVARCHAR2(30)", nullable = false)
    private String username;

    @Column(name = "SENT_CODE", columnDefinition = "NVARCHAR2(6)", nullable = false)
    private String code;

    @CreatedDate
    @Column(name = "CREATED_DATE", columnDefinition = "Date", nullable = false,  updatable = false)
    private Date createdDate;

    @Column(name = "IS_USED", columnDefinition = "NUMBER(1)", nullable = false)
    private Integer isUsed;
}
