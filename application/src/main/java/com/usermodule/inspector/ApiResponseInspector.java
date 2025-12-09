package com.usermodule.inspector;


import com.usermodule.dto.common.ApiErrorResponseDTO;
import com.usermodule.dto.common.ApiResponseDTO;
import com.usermodule.dto.system.ErrorAddRequestDTO;
import com.usermodule.exception.BusinessException;
import com.usermodule.service.system.ErrorService;
import com.usermodule.utils.AuthUtil;
import com.usermodule.utils.ResourceBundleUtil;
import com.usermodule.utils.TransactionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.usermodule.exception.CodeException.App;

@Component
@Slf4j
@RequiredArgsConstructor
public class ApiResponseInspector {

    private final ErrorService errorService;
    private final TransactionUtil transactionUtil;
    private final ResourceBundleUtil resourceBundleUtil;

    public ApiResponseDTO apiResponseBuilder(Object data, String message, boolean status) {
        if (transactionUtil.isOpenTrans()) {
            throw new BusinessException(App.TRANSACTION_IS_OPEN);
        }
        message = message == null || message.isEmpty()  ? "msg.operation.successful" : message;
        return ApiResponseDTO.builder()
                .timestamp(new Date())
                .status(status)
                .data(data)
                .message(resourceBundleUtil.getMessage(message))
                .build();
    }

    public ApiResponseDTO apiErrorResponseBuilder(List<ApiErrorResponseDTO> errors, Throwable throwable) {
        boolean isOpenTrans = false;
        if (transactionUtil.isOpenTrans()) {
            isOpenTrans = true;
            transactionUtil.rollback();
        }
        /*supplyAsync*/
        var errorResponseDTOList = insertErrors(errors, isOpenTrans, throwable);
        return ApiResponseDTO.builder()
                .timestamp(new Date())
                .status(false)
                .errors(errorResponseDTOList)
                .build();
    }

    private List<ApiErrorResponseDTO> insertErrors(List<ApiErrorResponseDTO> errors, boolean isOpenTrans, Throwable throwable) {
        List<ApiErrorResponseDTO> errorResponseDTOList = new ArrayList<>();
        if (errors != null) {
            Long userId;
            String username;
            String requestIp;
            String hostName;
            String hostAddress;
            Integer cityCode;
            if (AuthUtil.GET_USER() != null) {
                userId = AuthUtil.GET_USER().userId();
                username = AuthUtil.GET_USER().username();
                requestIp = AuthUtil.GET_USER().requestIp();
                hostName = AuthUtil.GET_USER().hostName();
                hostAddress = AuthUtil.GET_USER().hostAddress();
            } else {
                userId = null;
                username = null;
                requestIp = null;
                hostName = null;
                hostAddress = null;
            }
            String stackTrace = ExceptionUtils.getStackTrace(throwable);
                try {
                    for (ApiErrorResponseDTO error : errors) {
                        var errorAddRequestDTO= ErrorAddRequestDTO.builder()
                                .apiErrorResponseDTO(error)
                                .userId(userId)
                                .username(username)
                                .requestIp(requestIp)
                                .hostName(hostName)
                                .hostAddress(hostAddress)
                                .isOpenTrans(isOpenTrans)
                                .stackTrace(stackTrace)
                                .build();
                        var apiErrorResponseDTO = errorService.add(errorAddRequestDTO);
                        errorResponseDTOList.add(apiErrorResponseDTO);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
        }
        return errorResponseDTOList;
    }
}
