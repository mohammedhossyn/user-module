package com.usermodule.exception;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class BusinessException extends RuntimeException {

    private String name;
    private Integer code;
    private String field;
    private String message;
    private String description;
    private Object value;
    private String className;
    private int line;
    private Exception exception;

    public BusinessException(String message) {
        this.message = message;
    }

    public BusinessException(String message, Object value) {
        this.message = message;
        this.value = value;
    }

    public BusinessException(Integer code) {
        this.code = code;
    }

    public BusinessException(Exception exception) {
        this.exception = exception;
    }

    public BusinessException(Integer code, Object value) {
        this.value = value;
        this.code = code;
    }

}
