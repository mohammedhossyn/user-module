package com.usermodule.mapper.user.role;

import com.usermodule.dto.user.permission.PermissionResponseDTO;
import com.usermodule.dto.user.role.RoleResponseDTO;
import com.usermodule.mapper.user.permission.PermissionDTOMapper;
import com.usermodule.model.user.RoleEntity;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RoleDTOMapper implements Function<RoleEntity, RoleResponseDTO> {

    private final PermissionDTOMapper permissionDTOMapper;

    @Override
    public RoleResponseDTO apply(@NonNull RoleEntity roleEntity) {
        return RoleResponseDTO.builder()
                .name(roleEntity.getName())
                .description(roleEntity.getDescription())
                .permissions(Optional.ofNullable(roleEntity.getPermissions())
                        .orElseGet(Collections::emptySet)
                        .stream()
                        .map(permission ->
                                PermissionResponseDTO.builder().code(permission.getCode()).build())
                        .collect(Collectors.toSet())
                )
                .build();
    }

    public RoleEntity apply(@NonNull RoleResponseDTO roleResponseDTO) {
        PermissionDTOMapper permissionDTOMapper = new PermissionDTOMapper();
        return RoleEntity.builder()
                .name(roleResponseDTO.name())
                .description(roleResponseDTO.description())
                .permissions(Optional.ofNullable(roleResponseDTO.permissions())
                        .orElseGet(Collections::emptySet)
                        .stream()
                        .map(permissionDTOMapper::apply)
                        .collect(Collectors.toSet())
                )
                .build();
    }
}