package com.usermodule.model.message;

import com.usermodule.model.system.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SuperBuilder
@Table(name = "MESSAGE_TABLE")
public class MessageEntity extends BaseEntity {

    @Id
    @Column(name = "MESSAGE_ID", columnDefinition = "NUMBER(38)", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MESSAGE_SEQ")
    @SequenceGenerator(sequenceName = "MESSAGE_SEQ", allocationSize = 1, name = "MESSAGE_SEQ")
    private Long messageId;

    @Column(name = "MESSAGE_TEXT", columnDefinition = "NVARCHAR2(2000)", nullable = false)
    private String message;

    @Column(name = "MESSAGE_SENDER", columnDefinition = "NVARCHAR2(20)", nullable = false)
    private String sender;

    @Column(name = "MESSAGE_RECIPIENT", columnDefinition = "NVARCHAR2(20)", nullable = false)
    private String recipient;

    @Enumerated(EnumType.STRING)
    @Column(name = "MESSAGE_TYPE", columnDefinition = "NVARCHAR2(100)", nullable = false)
    private MessageType type;

    @Column(name = "IS_SENT", columnDefinition = "NUMBER(20)")
    private Boolean isSent;

    @Transient
    private String messageTypeForSearch;

}
