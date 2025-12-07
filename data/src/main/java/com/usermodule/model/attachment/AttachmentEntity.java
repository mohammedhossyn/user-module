package com.usermodule.model.attachment;

import com.usermodule.model.system.BaseEntity;
import com.usermodule.model.user.UserEntity;
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
@Table(name = "ATTACHMENT_TABLE")
public class AttachmentEntity extends BaseEntity {

    @Id
    @Column(name = "ATTACHMENT_ID", columnDefinition = "NUMBER(38)", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ATTACHMENT_SEQ")
    @SequenceGenerator(sequenceName = "ATTACHMENT_SEQ", allocationSize = 1, name = "ATTACHMENT_SEQ")
    private Long attachmentId;

    @Column(name = "FILE_NAME", columnDefinition = "NVARCHAR2(500)")
    private String fileName;

    @Column(name = "FILE_PATH", columnDefinition = "NVARCHAR2(1000)")
    private String filePath;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "NVARCHAR2(500)")
    private AttachmentCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", referencedColumnName = "user_id")
    private UserEntity user;
}
