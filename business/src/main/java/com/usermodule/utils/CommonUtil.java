package com.usermodule.utils;


import org.springframework.stereotype.Component;

@Component
public class CommonUtil {

    public String getFullName(String firstName, String surname) {
        String fullName = null;
        if (firstName != null || surname != null) {
            if (firstName != null) {
                fullName = firstName;
            }
            if (surname != null) {
                if (fullName != null) {
                    fullName += " " + surname;
                } else {
                    fullName = surname;
                }
            }
        }
        return fullName;
    }
}
