package com.usermodule.service.common;

import org.springframework.stereotype.Component;


@Component
public class FileCategoryService {

    public interface UserImagesType {
        String USER_IMAGE_TYPE_PHOTO = "photo";
        String USER_IMAGE_TYPE_SIGN = "sign";
        String USER_IMAGE_TYPE_UNKNOWN = "unknown";
    }
}
