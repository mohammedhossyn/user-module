package com.usermodule.controller.user;

import com.usermodule.dto.common.ApiResponseDTO;
import com.usermodule.dto.common.PaginationRequestDTO;
import com.usermodule.dto.user.*;
import com.usermodule.dto.user.forgetAndChangePassword.ChangePasswordRequestDTO;
import com.usermodule.dto.user.forgetAndChangePassword.ForgetPasswordRequestDTO;
import com.usermodule.dto.user.getUserByPermission.GetUserByPermissionRequestDTO;
import com.usermodule.inspector.ApiResponseInspector;
import com.usermodule.service.auth.LoginHistoryService;
import com.usermodule.service.user.UserService;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@EnableMethodSecurity
@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final LoginHistoryService loginHistoryService;
    private final ApiResponseInspector apiResponseInspector;

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('Manager') or hasAuthority('User Management') or hasAuthority('Add User')")
    public ApiResponseDTO add(@Valid @RequestBody UserAddRequestDTO userAddRequestDTO) {
        var userResponseDTO = userService.add(userAddRequestDTO);
        return apiResponseInspector.apiResponseBuilder(userResponseDTO, "",
                true);
    }

    @PostMapping("/change/status")
    @PreAuthorize("hasAuthority('Manager') or hasAuthority('User Management') or hasAuthority('User Change Status') or hasAuthority('Activate User')")
    public ApiResponseDTO changeStatus(@NonNull @RequestBody UserChangeStatusRequestDTO userChangeStatusRequestDTO) {
        var userResponseDTO = userService.changeStatus(userChangeStatusRequestDTO);
        return apiResponseInspector.apiResponseBuilder(userResponseDTO, "",
                true);
    }

    @PostMapping("/search")
    @PreAuthorize("hasAuthority('Manager') or hasAuthority('User Management') or hasAuthority('Search Users')")
    public ApiResponseDTO search(@NonNull PaginationRequestDTO paginationRequestDTO,
                                 @RequestBody UserSearchRequestDTO userSearchRequestDTO) {
        var listDTO = userService.search(paginationRequestDTO.getPageable(), userSearchRequestDTO);
        return apiResponseInspector.apiResponseBuilder(listDTO, "",
                true);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('Manager') or hasAuthority('User Management') or hasAuthority('Search User')")
    public ApiResponseDTO user(@NonNull @PathVariable Long id) {
        var userResponseDTO = userService.getUser(id);
        return apiResponseInspector.apiResponseBuilder(userResponseDTO, "",
                true);
    }

//    @PutMapping("/update/{id}")
//    @PreAuthorize("hasAuthority('USER_MANAGEMENT') and hasAuthority('USER_UPDATE')")
//    public ApiResponseDTO update(@NonNull @PathVariable Long id,
//                                 @Valid @RequestBody UserAddRequestDTO userAddRequestDTO) {
//        var userResponseDTO = userService.update(id, userAddRequestDTO);
//        return apiResponseInspector.apiResponseBuilder(userResponseDTO, "",
//                true);
//    }

    @GetMapping("/loginHistory/{id}")
    @PreAuthorize("hasAuthority('Manager') or hasAuthority('User Management') or hasAuthority('User Login History')")
    public ApiResponseDTO loginHistory(PaginationRequestDTO paginationRequestDTO,
                                       @NonNull @PathVariable Long id) {
        var userResponseDTO = loginHistoryService.findByUserId(paginationRequestDTO.getPageable(), id);
        return apiResponseInspector.apiResponseBuilder(userResponseDTO, "",
                true);
    }

    @GetMapping("/conf")
    public ApiResponseDTO conf() {
        var userResponseDTO = userService.conf();
        return apiResponseInspector.apiResponseBuilder(userResponseDTO, "",
                true);
    }

    @PostMapping("/getUsersByPermission")
    @PreAuthorize("hasAuthority('Manager') or hasAuthority('User Management') or hasAuthority('Search Users By Permission')")
    public ApiResponseDTO getUsersByPermission(@RequestBody GetUserByPermissionRequestDTO getUserByPermissionRequestDTO) {
        var userResponseDTO = userService.getUsersByPermission(getUserByPermissionRequestDTO);
        return apiResponseInspector.apiResponseBuilder(userResponseDTO, "",
                true);
    }

    @PostMapping("/forgetPassword")
    public ApiResponseDTO forgetPassword(@RequestBody ForgetPasswordRequestDTO forgetPasswordRequestDTO) {
        var forgetPasswordResponseDTO = userService.forgetPassword(forgetPasswordRequestDTO);
        return apiResponseInspector.apiResponseBuilder(forgetPasswordResponseDTO, "",
                true);
    }

    @PostMapping("/changePassword")
    public ApiResponseDTO changePassword(@RequestBody ChangePasswordRequestDTO changePasswordRequestDTO) {
        var userResponseDTO = userService.changePassword(changePasswordRequestDTO);
        return apiResponseInspector.apiResponseBuilder(userResponseDTO, "",
                true);
    }

}