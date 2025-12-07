package com.usermodule.model.event;


import com.usermodule.model.user.UserEntity;
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
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "EVENT_TABLE")
@EntityListeners({AuditingEntityListener.class})
public class EventEntity {

    @Id
    @Column(name = "EVENT_ID", columnDefinition = "NUMBER(38)", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EVENT_SEQ")
    @SequenceGenerator(sequenceName = "EVENT_SEQ", allocationSize = 1, name = "EVENT_SEQ")
    private Long eventId;

    @OneToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "user_id", updatable = false)
    private UserEntity user;

    @Column(name = "TITLE", columnDefinition = "NVARCHAR2(100)", nullable = false)
    private String title;

    @Column(name = "MESSAGE", columnDefinition = "NVARCHAR2(500)")
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", columnDefinition = "NVARCHAR2(100)", nullable = false)
    private EventType type;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "NVARCHAR2(100)", nullable = false)
    private EventStatus status;

    @Column(name = "REFERENCE_ID", columnDefinition = "number(38)")
    private Long  referenceId;

    @Transient
    private String statusForSearch;

    @Transient
    private String eventTypeForSearch;

    @CreatedDate
    @Column(name = "CREATED_DATE", columnDefinition = "Date", nullable = false,  updatable = false)
    private Date createdDate;

}
