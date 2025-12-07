package com.usermodule.model.message;

import com.usermodule.utils.LabeledEnum;
import lombok.Getter;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

@Getter
public enum MessageType implements LabeledEnum {

    FORGOT_PASSWORD("forgot.password");

    MessageType(String label){
        this.label = label;
    }
    private final String label;
    private static final ResourceBundle bundle = ResourceBundle.getBundle("bundle");

    public String getLabel() {
        try {
            return bundle.getString(this.label);
        } catch (MissingResourceException e) {
            return name();
        }
    }
}
