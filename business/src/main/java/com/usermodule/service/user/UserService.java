package com.usermodule.service.user;

import com.usermodule.dto.common.PageResponseWithHeaderDTO;
import com.usermodule.dto.user.forgetAndChangePassword.ChangePasswordRequestDTO;
import com.usermodule.dto.user.forgetAndChangePassword.ChangePasswordResponseDTO;
import com.usermodule.dto.user.forgetAndChangePassword.ForgetPasswordRequestDTO;
import com.usermodule.dto.user.forgetAndChangePassword.ForgetPasswordResponseDTO;
import com.usermodule.dto.user.*;
import com.usermodule.dto.user.getUserByPermission.GetUserByPermissionRequestDTO;
import com.usermodule.dto.user.getUserByPermission.GetUserByPermissionResponseDTO;
import com.usermodule.exception.BusinessException;
import com.usermodule.mapper.user.DTOUserListResponseMapper;
import com.usermodule.mapper.user.UserDTOMapper;
import com.usermodule.mapper.user.UserSearchDTOMapper;
import com.usermodule.model.user.ForgetPasswordCodeEntity;
import com.usermodule.model.user.RoleEntity;
import com.usermodule.model.user.UserEntity;
import com.usermodule.model.user.UserStatus;
import com.usermodule.repository.event.EventRepository;
import com.usermodule.repository.user.ForgetPasswordCodeRepository;
import com.usermodule.repository.user.UserRepository;
import com.usermodule.service.message.MessageService;
import com.usermodule.utils.CustomPasswordEncoder;
import com.usermodule.utils.PaginationUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.usermodule.model.event.EventStatus.UNSEEN;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserCrudService userCrudService;
    private final UserRepository userRepository;
    private final UserSearchDTOMapper userSearchDTOMapper;
    private final DTOUserListResponseMapper dtoUserListResponseMapper;
    private final PaginationUtil<UserSearchResponseDTO> userListResponseDTOPaginationUtil;
    private final UserDTOMapper userDTOMapper;
    private final MessageService messageService;
    private final ForgetPasswordCodeRepository forgetPasswordCodeRepository;
    private final ForgetPasswordCodeCrudService forgetPasswordCodeCrudService;
    private final CustomPasswordEncoder customPasswordEncoder;
    @Value("${sms.code-digits}")
    private int codeDigits;

    @Value("${forget-password.code.timeout}")
    private int forgetPasswordCodeTimeout;

    private final EventRepository eventRepository;

    public UserResponseDTO add(@NonNull UserAddRequestDTO userAddRequestDTO) {
        log.debug("UserService.add started");
        return userCrudService.add(userAddRequestDTO);
    }


    public UserResponseDTO getUser(Long id) {
        log.debug("UserService.getUser started");
        return userDTOMapper.apply(userCrudService.read(id));
    }

    public PageResponseWithHeaderDTO<UserSearchResponseDTO> search(Pageable pageable, UserSearchRequestDTO userSearchRequestDTO) {
        log.debug("UserService.search started");
        var userEntity = dtoUserListResponseMapper.apply(userSearchRequestDTO);
        Page<UserSearchResponseDTO> pages = userRepository.search(pageable, userEntity).map(userSearchDTOMapper);
        return userListResponseDTOPaginationUtil.getHeader(pages, UserSearchResponseDTO.class);
    }

    public UserConfResponseDTO conf() {
        log.debug("UserService.conf started");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var userEntity = ((UserDetailsDTO)authentication.getPrincipal()).getUser();

        var permissions = new ArrayList<>(authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList());

        var unseenNotifyCount = eventRepository.countByUser_UserIdAndStatus(userEntity.getUserId(),
                UNSEEN);
        log.debug("UserService.conf ended");
        return UserConfResponseDTO.builder()
                .id(userEntity.getUserId())
                .permissions(permissions)
                .unseenNotifyCount(unseenNotifyCount)
                .build();
    }

    public List<GetUserByPermissionResponseDTO> getUsersByPermission(GetUserByPermissionRequestDTO getUserByPermissionRequestDTO) {
        log.debug("UserService.getUsersByPermission started");
        var list = userRepository.findByPermissionCodes(getUserByPermissionRequestDTO.codes());
        return list.stream().map(user -> GetUserByPermissionResponseDTO.builder()
                        .id(user.getUserId())
                        .build())
                .toList();
    }

    public ForgetPasswordResponseDTO forgetPassword(ForgetPasswordRequestDTO forgetPasswordRequestDTO){
        log.debug("UserService.forgetPassword started");
        String code = String.valueOf(generateCode());
       var userEntity = userRepository.findByUsername(forgetPasswordRequestDTO.username()).orElseThrow();
        var message = messageService.sendMessageOfForgetPassword(code, userEntity.getMobileNumber());
        if (message.getIsSent()){
            var forgetPasswordCode = ForgetPasswordCodeEntity.builder()
                    .code(code)
                    .username(forgetPasswordRequestDTO.username())
                    .isUsed(0)
                    .build();
            forgetPasswordCodeRepository.save(forgetPasswordCode);
            log.debug("UserService.forgetPassword ended");
            return ForgetPasswordResponseDTO.builder().username(forgetPasswordRequestDTO.username()).code(code).build();
        } else {
            throw new RuntimeException();
        }

    }

    public ChangePasswordResponseDTO changePassword(ChangePasswordRequestDTO changePasswordRequestDTO) {
        log.debug("UserService.changePassword started");
        var userEntity = userRepository.findByUsername(changePasswordRequestDTO.username()).orElseThrow();
        var forgetPasswordCodeExists = forgetPasswordCodeRepository
                .findByUsernameAndCode(userEntity.getUsername(), changePasswordRequestDTO.code(), getMinutesAgo(forgetPasswordCodeTimeout));
        if (forgetPasswordCodeExists.isPresent()){
            var forgetPasswordCode = forgetPasswordCodeExists.get();
            forgetPasswordCode.setIsUsed(1);
            forgetPasswordCodeCrudService.update(forgetPasswordCode);
            userEntity.setPassword(customPasswordEncoder.encode(changePasswordRequestDTO.newPassword()));
            userCrudService.update(userEntity);
            log.debug("UserService.changePassword ended");
            return ChangePasswordResponseDTO.builder().username(userEntity.getUsername()).build();
        } else {
            return null;
        }
    }

    private Date getMinutesAgo(int minutes) {
        return new Date(System.currentTimeMillis() - minutes * 60000L);
    }


    public int generateCode(){
        int min = (int) Math.pow(10, codeDigits - 1);
        int max = (int) Math.pow(10, codeDigits) - 1;
        return min + (int) (Math.random() * (max - min + 1));
    }

    public UserEntity findByUsername(String username) {
        log.debug("UserService.findByUsername started");
        return userRepository.findByUsername(username).orElseThrow();
    }


    public boolean existByUsername(String username) {
        log.debug("UserService.existByUsername started");
        return userRepository.existsByUsername(username);
    }

    public boolean existByUsernameAndNotThisId(Long id, String username) {
        log.debug("UserService.existByUsername started");
        return userRepository.existByUsernameAndNotThisId(id, username).isPresent();
    }

    public UserResponseDTO changeStatus(@NonNull UserChangeStatusRequestDTO userChangeStatusRequestDTO) {
        log.debug("UserService.changeStatus started");
        UserStatus userStatus;
        log.debug("userChangeStatusRequestDTO.status() {}", userChangeStatusRequestDTO.status());
        try{
            if (userChangeStatusRequestDTO.status().isEmpty()){
                userStatus = null;
            } else {
                userStatus = UserStatus.valueOf(userChangeStatusRequestDTO.status());
            }
        } catch (Exception e){
            throw BusinessException.builder()
                    .name("change user status")
                    .message("err.user.status.not.exists")
                    .build();
        }
        log.debug("UserService.changeStatus ended");
        return userDTOMapper.apply(userCrudService.changeStatus(userChangeStatusRequestDTO.id(), userStatus));
    }

    public UserResponseDTO update(UserEntity user) {
        return userDTOMapper.apply(userCrudService.update(user));
    }

    public UserEntity getCurrentUser() {
        return userCrudService.getCurrentUser();
    }

    public RoleEntity getCurrentUserRole() {
        return userCrudService.getCurrentUserRole();
    }

    public String getUserRole(UserEntity user) {
        return userCrudService.getUserRole(user);
    }

}