package com.usermodule.model.auth;

import com.usermodule.model.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "LOGIN_HISTORY")
public class LoginHistoryEntity {

    @Id
    @Column(name = "login_history_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LOGIN_HISTORY_SEQ")
    @SequenceGenerator(sequenceName = "LOGIN_HISTORY_SEQ", allocationSize = 1, name = "LOGIN_HISTORY_SEQ")
    private Long loginHistoryId;

    @Column(name = "IP")
    private String requestIp;

    @Column(name = "SERVER_NAME", columnDefinition = "NVARCHAR2(50)")
    private String hostAddress;

    @Column(name = "HOST_NAME", columnDefinition = "NVARCHAR2(16)")
    private String hostName;

    @Column(name = "LOGIN_DATE")
    private Date createdDate;

    /**/
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    /*Deprecated Properties*/

//    @Deprecated
    @Column(name = "USER_NAME")
    private String userName;

    @Deprecated
    @Column(name="SESSION_ID", columnDefinition="VARCHAR2(200)")
    private String sessionId;

    @Deprecated
    @Column(name = "LOGIN_RESULT", columnDefinition = "NUMBER(1)")
    private String loginResult;


}

