package com.usermodule.model.system;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "ERROR_TABLE")
@EntityListeners({AuditingEntityListener.class})
public class ErrorEntity {

    @Id
    @Column(name = "ERROR_ID", columnDefinition = "NUMBER(38)")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ERROR_SEQ")
    @SequenceGenerator(sequenceName = "ERROR_SEQ", allocationSize = 1, name = "ERROR_SEQ")
    private Long errorId;

    @Column(name = "TITLE", columnDefinition = "NVARCHAR2(200)")
    private String title;

    @Column(name = "NAME", columnDefinition = "NVARCHAR2(500)")
    private String name;

    @Column(name = "CODE", columnDefinition = "NUMBER(8)")
    private Integer code;

    @Column(name = "FIELD", columnDefinition = "NVARCHAR2(500)")
    private String field;

    @Column(name = "MESSAGE", columnDefinition = "NVARCHAR2(2000)")
    private String message;

    @Column(name = "VALUE", columnDefinition = "NVARCHAR2(2000)")
    private String value;

    @Column(name = "DESCRIPTION", columnDefinition = "NVARCHAR2(2000)")
    private String description;

    @Column(name = "PATH", columnDefinition = "NVARCHAR2(2000)")
    private String path;

    @Column(name = "CLASS_NAME", columnDefinition = "NVARCHAR2(2000)")
    private String className;

    @Column(name = "METHOD_NAME", columnDefinition = "NVARCHAR2(500)")
    private String methodName;

    @Column(name = "LINE", columnDefinition = "NUMBER(8)")
    private Integer line;

    @Column(name = "REQUEST_IP", columnDefinition = "NVARCHAR2(50)")
    private String requestIp;

    @Column(name = "HOST_ADDRESS", columnDefinition = "NVARCHAR2(50)")
    private String hostAddress;

    @Column(name = "HOST_NAME", columnDefinition = "NVARCHAR2(16)")
    private String hostName;

    @Column(name = "IS_OPEN_TRANS", columnDefinition = "NUMBER(1,0)")
    private Boolean isOpenTrans;

    @Version
    @Column(name = "VERSION", columnDefinition = "number(38)", nullable = false)
    private Long version;

    @Column(name = "USER_NAME", columnDefinition = "NVARCHAR2(30)")
    private String username;

    @Column(name = "USER_ID", columnDefinition = "NVARCHAR2(30)")
    private Long userId;

    @CreatedDate
    @Column(name = "CREATED_DATE", columnDefinition = "date", updatable = false)
    private Date createdDate;

    @Lob
    @Column(name = "STACK_TRACE", columnDefinition = "CLOB")
    private String stackTrace;
}

