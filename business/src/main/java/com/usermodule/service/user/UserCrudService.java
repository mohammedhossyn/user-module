package com.usermodule.service.user;

import com.usermodule.dto.user.UserDetailsDTO;
import com.usermodule.dto.user.UserAddRequestDTO;
import com.usermodule.dto.user.UserResponseDTO;
import com.usermodule.exception.AlreadyExistsException;
import com.usermodule.exception.BusinessException;
import com.usermodule.mapper.user.UserAddDTOMapper;
import com.usermodule.mapper.user.UserDTOMapper;
import com.usermodule.model.user.*;
import com.usermodule.repository.user.RoleRepository;
import com.usermodule.repository.user.UserRepository;
import com.usermodule.repository.user.UserRolePermissionRepository;
import com.usermodule.utils.CustomPasswordEncoder;
import com.usermodule.utils.TransactionUtil;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.usermodule.model.user.UserStatus.ACTIVE;
import static com.usermodule.model.user.UserStatus.INACTIVE;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserCrudService {

    private final RoleRepository roleRepository;
    private final CustomPasswordEncoder customPasswordEncoder;
    private final UserDTOMapper userDTOMapper;
    private final UserAddDTOMapper userAddDTOMapper;
    private final Validator validator;
    private final TransactionUtil transactionUtil;
    private final UserRepository userRepository;
    private final UserRolePermissionRepository userRolePermissionRepository;

    public UserResponseDTO add(UserAddRequestDTO userAddRequestDTO) {
        log.debug("UserCrudService.add started");
        try {
            transactionUtil.openTransaction();
            if (userRepository.existsByUsername(userAddRequestDTO.username())) {
                log.info("username {} is already in use", userAddRequestDTO.username());
                throw new AlreadyExistsException("username : " + userAddRequestDTO.username());
            }
            var userEntity = userAddDTOMapper.apply(userAddRequestDTO);
            Set<ConstraintViolation<UserEntity>> violations = validator.validate(userEntity);
            if (!violations.isEmpty()) {
                log.debug("violations is not empty");
                StringBuilder sb = new StringBuilder();
                for (ConstraintViolation<UserEntity> constraintViolation : violations) {
                    sb.append(constraintViolation.getMessage());
                }
                throw new ConstraintViolationException("Error occurred: " + sb, violations);
            }
            userEntity.setUserId(null);
            userEntity.setPassword(customPasswordEncoder.encode(userEntity.getPassword()));

            UserRolePermissionEntity userRolePermissionEntity = createUserRolePermission(userAddRequestDTO);
            userRolePermissionRepository.save(userRolePermissionEntity);
            log.debug("userRolePermissionEntity saved");
            userRolePermissionEntity.setUser(userEntity);
            userEntity.setUserRolePermissions(new HashSet<>());
            userEntity.getUserRolePermissions().add(userRolePermissionEntity);
            var savedUser = userRepository.save(userEntity);
            transactionUtil.commit();
            log.debug("UserCrudService.add ended");
            return userDTOMapper.apply(savedUser);
        } catch (Exception e) {
            transactionUtil.rollback();
            throw new BusinessException(e);
        }
    }

    private UserRolePermissionEntity createUserRolePermission(UserAddRequestDTO userAddRequestDTO) {
        log.debug("UserCrudService.createUserRolePermission started");
        List<PermissionEntity> permissionEntities = new ArrayList<>();

        Long roleId = userAddRequestDTO.roleId();
        RoleEntity roleEntity = roleRepository.findById(roleId).orElseThrow();

        permissionEntities.addAll(roleEntity.getPermissions());
        Set<PermissionEntity> permissionsUserToHave = new HashSet<>();
        if (userAddRequestDTO.permissions() != null) {
            log.debug("permissionEntities is not null");
            for (Long permissionId : userAddRequestDTO.permissions()) {
                for (PermissionEntity permissionEntity : permissionEntities) {
                    if (permissionEntity.getPermissionId().equals(permissionId)) {
                        log.debug("permissionId : {}", permissionId);
                        permissionsUserToHave.add(permissionEntity);
                    }
                }
            }
        }
        UserRolePermissionEntity userRolePermissionEntity = UserRolePermissionEntity.builder()
                .role(roleEntity).build();
        if (!permissionsUserToHave.isEmpty()) {
            log.debug("permissionsUserToHave is not empty");
            userRolePermissionEntity.setPermissions(new HashSet<>(permissionsUserToHave));
        } else {
            userRolePermissionEntity.setPermissions(new HashSet<>(roleEntity.getPermissions()));
        }
        log.debug("UserCrudService.createUserRolePermission ended");
        return userRolePermissionEntity;
    }


    public UserEntity create(@NonNull UserEntity userEntity) {
        log.debug("UserCrudService.create started");
        if (userEntity.getUserId() == null) {
            return userRepository.save(userEntity);
        } else {
            throw new BusinessException(0);
        }
    }

    public UserEntity read(@NonNull Long id) {
        log.debug("UserCrudService.read started");
        Optional<UserEntity> optionalError = userRepository.findById(id);
        if (optionalError.isPresent()) {
            return optionalError.get();
        } else {
            throw new BusinessException(0);
        }
    }

    public UserEntity update(@NonNull UserEntity userEntity) {
        log.debug("UserCrudService.update started");
        if (userRepository.existsById(userEntity.getUserId())) {
            return userRepository.save(userEntity);
        } else {
            throw new BusinessException(0);
        }
    }

    public void delete(@NonNull UserEntity userEntity) {
        log.debug("UserCrudService.delete started");
        if (userRepository.existsById(userEntity.getUserId())) {
            userRepository.delete(userEntity);
        } else {
            throw new BusinessException(0);
        }
    }

    public UserEntity saveOrUpdate(@NonNull UserEntity userEntity) {
        log.debug("UserCrudService.saveOrUpdate started");
        return userRepository.save(userEntity);
    }

    public UserEntity addWithSpecificRole(UserEntity user, String roleName) {
        log.debug("UserCrudService.addCustomer started");
        var roleEntity = roleRepository.findByName(roleName).orElseThrow();
        var userRolePermissionEntity = UserRolePermissionEntity.builder()
                .role(roleEntity).build();
        userRolePermissionEntity.setPermissions(new HashSet<>(roleEntity.getPermissions()));
        userRolePermissionEntity.setUser(user);
        user.setUserRolePermissions(new HashSet<>());
        user.getUserRolePermissions().add(userRolePermissionEntity);
        user.setPassword(customPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        log.debug("UserCrudService.addCustomer ended");
        return user;
    }


    public UserEntity changeStatus(Long id, UserStatus userStatus) {
        log.debug("UserCrudService.changeStatus started");
        var user = userRepository.findById(id).orElseThrow();
        log.debug("userStatus : {}", userStatus);
        if (userStatus == null){
            log.debug("user.getStatus() : {}", user.getStatus());
            if (user.getStatus().equals(ACTIVE)){
                user.setStatus(INACTIVE);
            } else if (user.getStatus().equals(INACTIVE)){
                user.setStatus(ACTIVE);
            }
        } else {
            user.setStatus(userStatus);
        }
        log.debug("UserCrudService.changeStatus ended");
        return update(user);
    }

    public UserEntity getCurrentUser() {
        return Optional.of(((UserDetailsDTO) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUser()).orElseThrow();
    }

    public RoleEntity getCurrentUserRole() {
        return getCurrentUser().getUserRolePermissions().stream().toList().get(0).getRole();
    }

    public String getUserRole(UserEntity user) {
        return user.getUserRolePermissions().stream().toList().get(0).getRole().getName();
    }
}