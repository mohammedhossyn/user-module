package com.usermodule.utils;

import com.usermodule.exception.DataException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataExceptionUtil {



    public String getMessage(DataException ex) {
        return ex.getMessage();
    }

    public DataException generateBusinessExceptionByCode(Integer code) {
        // generate business exception...
        return null;
    }
}
