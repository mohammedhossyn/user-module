package com.usermodule.dto.user.validation;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Setter
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PermissionData {

    private Long permissionId;
    private String code;
    private String description;
    private List<PermissionData> children;
}
