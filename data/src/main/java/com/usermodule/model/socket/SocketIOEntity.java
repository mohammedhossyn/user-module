package com.usermodule.model.socket;

import com.usermodule.model.user.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "SOCKET_IO")
@EntityListeners({AuditingEntityListener.class})
public class SocketIOEntity {
    @Id
    @Column(name = "SOCKET_IO_ID", columnDefinition = "NUMBER(38)", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SOCKET_IO_SEQ")
    @SequenceGenerator(sequenceName = "SOCKET_IO_SEQ", allocationSize = 1, name = "SOCKET_IO_SEQ")
    private Long socketIOId;

    @Column(name = "SESSION_ID", columnDefinition = "VARCHAR2(40)" , nullable = false)
    private String sessionId;

    @OneToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "user_id", updatable = false)
    private UserEntity user;

    @Column(name = "USERNAME", columnDefinition = "NVARCHAR2(30)", nullable = false)
    private String username;

    @CreatedDate
    @Column(name = "CREATED_DATE", columnDefinition = "date", nullable = false,  updatable = false)
    private Date createdDate;

    @LastModifiedDate
    @Column(name = "LAST_MODIFIED_DATE", columnDefinition = "date")
    private Date lastModifiedDate;

    @Column(name = "SERVER_NAME", columnDefinition = "varchar2(64)")
    private String serverName;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "NVARCHAR2(100)", nullable = false)
    private SocketIOStatus status;

    @Version
    @Column(name = "VERSION", columnDefinition = "number(38)",  nullable = false)
    private Long version;

    @Transient
    private String statusForSearch;

}
