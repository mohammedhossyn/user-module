package com.usermodule.model.system;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners({AuditingEntityListener.class})
@Table(name = "OPTION_TABLE")
public class OptionEntity extends BaseEntity {

    @Id
    @Column(name = "OPTION_ID", columnDefinition = "NUMBER(38)", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "OPTION_SEQ")
    @SequenceGenerator(sequenceName = "OPTION_SEQ", allocationSize = 1, name = "OPTION_SEQ")
    private Long optionId;

    @Column(name = "CODE", columnDefinition = "NVARCHAR2(50)", nullable = false)
    private String code;

    @Column(name = "VALUE", columnDefinition = "NVARCHAR2(500)", nullable = false)
    private String value;

    @Column(name = "DESCRIPTION", columnDefinition = "NVARCHAR2(2000)", nullable = false)
    private String description;


}
