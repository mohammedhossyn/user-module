package com.usermodule.service.system;

import com.usermodule.dto.common.ApiErrorResponseDTO;
import com.usermodule.dto.common.PageResponseWithHeaderDTO;
import com.usermodule.dto.system.ErrorAddRequestDTO;
import com.usermodule.dto.system.ErrorResponseDTO;
import com.usermodule.dto.system.ErrorSearchRequestDTO;
import com.usermodule.dto.system.ErrorSearchResponseDTO;
import com.usermodule.model.system.ErrorEntity;
import com.usermodule.repository.system.ErrorRepository;
import com.usermodule.utils.DateUtil;
import com.usermodule.utils.PaginationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ErrorService {

    private final ErrorCrudService errorCrudService;
    private final ErrorRepository errorRepository;
    private final DateUtil dateUtil;
    private final PaginationUtil<ErrorSearchResponseDTO> errorResponseDTOPaginationUtil;

    public ApiErrorResponseDTO add(ErrorAddRequestDTO errorAddRequestDTO) {
        log.debug("ErrorService.add started");
        Long errorId = null;
        var error = ErrorEntity.builder()
                .title(errorAddRequestDTO.apiErrorResponseDTO().title())
                .name(errorAddRequestDTO.apiErrorResponseDTO().name())
                .code(errorAddRequestDTO.apiErrorResponseDTO().code())
                .field(errorAddRequestDTO.apiErrorResponseDTO().field())
                .message(errorAddRequestDTO.apiErrorResponseDTO().message())
                .value(errorAddRequestDTO.apiErrorResponseDTO().value() != null ?
                        errorAddRequestDTO.apiErrorResponseDTO().value().toString() : null)
                .description(errorAddRequestDTO.apiErrorResponseDTO().description())
                .path(errorAddRequestDTO.apiErrorResponseDTO().path())
                .className(errorAddRequestDTO.apiErrorResponseDTO().className())
                .methodName(errorAddRequestDTO.apiErrorResponseDTO().methodName())
                .line(errorAddRequestDTO.apiErrorResponseDTO().line())
                .requestIp(errorAddRequestDTO.requestIp())
                .hostName(errorAddRequestDTO.hostName())
                .hostAddress(errorAddRequestDTO.hostAddress())
                .username(errorAddRequestDTO.username())
                .userId(errorAddRequestDTO.userId())
                .isOpenTrans(errorAddRequestDTO.isOpenTrans())
                .stackTrace(errorAddRequestDTO.stackTrace() != null ? errorAddRequestDTO.stackTrace()
                        .substring(0, 8000) : null)
                .build();
        try {
            var errorEntity = errorCrudService.create(error);
            errorId = errorEntity.getErrorId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.debug("ErrorService.add ended");
        return ApiErrorResponseDTO.builder()
                .errorId(errorId)
                .title(error.getTitle())
                .name(error.getName())
                .code(error.getCode())
                .field(error.getField())
                .message(error.getMessage())
                .value(errorAddRequestDTO.apiErrorResponseDTO().value())
                .description(error.getDescription())
                .path(error.getPath())
                .className(error.getClassName())
                .methodName(error.getMethodName())
                .line(error.getLine())
                .build();
    }

    public PageResponseWithHeaderDTO<ErrorSearchResponseDTO> search(Pageable pageable,
                                                                          ErrorSearchRequestDTO errorSearchRequestDTO) {
        log.debug("ErrorService.search started");
        Page<ErrorSearchResponseDTO> pages = errorRepository.search(pageable,
                        errorSearchRequestDTO.errorId(),
                        errorSearchRequestDTO.username(),
                        errorSearchRequestDTO.code(),
                        errorSearchRequestDTO.requestIp(),
                        errorSearchRequestDTO.hostAddress(),
                        dateUtil.getDate(errorSearchRequestDTO.createdDateFrom()),
                        dateUtil.getDate(errorSearchRequestDTO.createdDateTo()),
                        errorSearchRequestDTO.path())
                .map(errorEntity -> ErrorSearchResponseDTO.builder()
                        .errorId(errorEntity.getErrorId())
                        .createdDate(errorEntity.getCreatedDate())
                        .username(errorEntity.getUsername())
                        .code(errorEntity.getCode())
                        .description(errorEntity.getDescription())
                        .requestIp(errorEntity.getRequestIp())
                        .hostAddress(errorEntity.getHostAddress())
                        .build());
        log.debug("ErrorService.search ended");
        return errorResponseDTOPaginationUtil.getHeader(pages, ErrorSearchResponseDTO.class);
    }

    public ErrorResponseDTO getError(Long errorId) {
        log.debug("ErrorService.getError started");
        var errorDetailEntity = errorCrudService.read(errorId);
        return ErrorResponseDTO.builder()
                .errorId(errorDetailEntity.getErrorId())
                .title(errorDetailEntity.getTitle())
                .name(errorDetailEntity.getName())
                .code(errorDetailEntity.getCode())
                .field(errorDetailEntity.getField())
                .message(errorDetailEntity.getMessage())
                .value(errorDetailEntity.getValue())
                .description(errorDetailEntity.getDescription())
                .path(errorDetailEntity.getPath())
                .className(errorDetailEntity.getClassName())
                .methodName(errorDetailEntity.getMethodName())
                .line(errorDetailEntity.getLine())
                .requestIp(errorDetailEntity.getRequestIp())
                .hostAddress(errorDetailEntity.getHostAddress())
                .hostName(errorDetailEntity.getHostName())
                .isOpenTrans(errorDetailEntity.getIsOpenTrans())
                .version(errorDetailEntity.getVersion())
                .username(errorDetailEntity.getUsername())
                .userId(errorDetailEntity.getUserId())
                .stackTrace(errorDetailEntity.getStackTrace())
                .createdDate(errorDetailEntity.getCreatedDate())
                .build();
    }

}
