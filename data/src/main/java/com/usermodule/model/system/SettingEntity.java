package com.usermodule.model.system;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "SETTING_TABLE")
public class SettingEntity extends BaseEntity {

    @Id
    @Column(name = "SETTING_ID", columnDefinition = "NUMBER(38)", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SETTING_SEQ")
    @SequenceGenerator(sequenceName = "SETTING_SEQ", allocationSize = 1, name = "SETTING_SEQ")
    private Long settingId;

    // Add any parameters you want

}
