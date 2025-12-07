package com.usermodule.exception;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class DataException extends RuntimeException {

    private String name;
    private Integer code;
    private String field;
    private String message;
    private String description;
    private Object value;
    private String className;
    private int line;
    private Exception exception;

    public DataException(Integer code) {
        this.code = code;
    }

    public DataException(Exception exception) {
        this.exception = exception;
    }

    public DataException(String description, Integer code) {
        this.description = description;
        this.code = code;
    }

}
