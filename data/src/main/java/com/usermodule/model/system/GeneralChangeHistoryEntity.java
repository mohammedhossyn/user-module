package com.usermodule.model.system;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Setter
@Getter
@Entity
@RequiredArgsConstructor
@SuperBuilder
@Table(name = "GENERAL_CHANGE_HISTORY")
public class GeneralChangeHistoryEntity {
    public static final Integer EDIT_USER = 1;
    public static final Integer EDIT_USER_ACCESS = 2;
    public static final Integer CHANGE_ORIGIN_OPTION = 3;

    @Id
    @Column(name = "GENERAL_CHANGE_HISTORY_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GENERAL_CHANGE_HISTORY_SEQ")
    @SequenceGenerator(name = "GENERAL_CHANGE_HISTORY_SEQ", allocationSize = 1)
    private Long generalChangeHistoryId;
    @Column(name = "CHANGED_TABLE")
    private String changedTable;
    @Column(name = "CHANGE_ORIGIN")
    private Integer changeOrigin;
    @Column(name = "CHANGE_DATE")
    private Date changeDate;
    @Column(name = "CHANGED_BY")
    private String changedBy;
    @Column(name = "TABLE_ID")
    private Long tableId;
    @Column(name = "TABLE_FIELD")
    private String tableField;
    @Column(name = "OLD_VALUE")
    private String oldValue;
    @Column(name = "NEW_VALUE")
    private String newValue;
    @Transient
    private String comments;
}
