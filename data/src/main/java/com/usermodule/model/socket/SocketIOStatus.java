package com.usermodule.model.socket;

import com.usermodule.utils.LabeledEnum;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public enum SocketIOStatus implements LabeledEnum {
    INACTIVE("inactive"),
    ACTIVE("active");

    private final String label;
    SocketIOStatus(String label) {
        this.label = label;
    }

    private static final ResourceBundle bundle = ResourceBundle.getBundle("bundle");

    public String getLabel() {
        try {
            return bundle.getString(this.label);
        } catch (MissingResourceException e) {
            return name();
        }
    }
}
