package com.usermodule.service.security;

import com.usermodule.dto.user.UserDetailsDTO;
import com.usermodule.exception.BusinessException;
import com.usermodule.model.user.PermissionEntity;
import com.usermodule.model.user.UserRolePermissionEntity;
import com.usermodule.repository.user.UserRepository;
import com.usermodule.service.system.OptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.usermodule.exception.CodeException.Auth;

@Service
@RequiredArgsConstructor
public class UserDetailsImplService implements UserDetailsService {

    private final UserRepository userRepository;
    private final OptionService optionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        var userEntityOptional = userRepository.findByUsername(username);
        if (userEntityOptional.isPresent()) {
            var userEntity = userEntityOptional.get();

            List<UserRolePermissionEntity> userRolePermissionEntities = userEntity.getUserRolePermissions().stream().toList();
            UserRolePermissionEntity userRolePermission;
            if (userRolePermissionEntities.isEmpty()){
                userRolePermission = new UserRolePermissionEntity();
            } else {
                userRolePermission = userRolePermissionEntities.get(0);
            }


            String optionAccessUsers = optionService.getStringValueByCode("OPTION_ACCESS_USERS");
            if (optionAccessUsers != null && !optionAccessUsers.isEmpty()) {
                String[] users = optionAccessUsers.split(",");
                for (String user : users) {
                    if (userEntity.getUsername().equals(user)) {
                        userRolePermission.getPermissions().add(PermissionEntity.builder()
                                .permissionId(null)
                                .code("Option List")
                                .description(null)
                                .build());
                        userRolePermission.getPermissions().add(PermissionEntity.builder()
                                .permissionId(null)
                                .code("Update Option")
                                .description(null)
                                .build());
                        userRolePermission.getPermissions().add(PermissionEntity.builder()
                                .permissionId(null)
                                .code("Reload Option")
                                .description(null)
                                .build());
                        userRolePermission.getPermissions().add(PermissionEntity.builder()
                                .permissionId(null)
                                .code("Search Errors")
                                .description(null)
                                .build());
                        userRolePermission.getPermissions().add(PermissionEntity.builder()
                                .permissionId(null)
                                .code("Search Error")
                                .description(null)
                                .build());
                        break;
                    }
                }
            }
            return UserDetailsDTO.builder()
                    .user(userEntity)
                            .build();
        } else {
            throw new BusinessException(Auth.INVALID_USERNAME_OR_PASSWORD);
        }

    }
}
