package com.usermodule.exception;

public interface CodeException {

    interface App {
        int INTERNAL_SYSTEM_ERROR = 500;
        int TRANSACTION_IS_OPEN = 501;
        int SERVICE_NOT_FOUND = 502;
    }

    interface Auth {
        int INVALID_USERNAME_OR_PASSWORD = 1000;
        int TOKEN_EXPIRE_DATE = 1010;
        int TOKEN_SIGNATURE_INVALID = 1020;
        int TOKEN_ALGORITHM_DOESNT_MATCH = 1030;
        int TOKEN_NOT_FOUND= 1040;
    }

    interface User {
        int USER_NOT_FOUND = 2000;
    }


    interface File {
        int FILE_NOT_FOUND = 3000;
    }

}
