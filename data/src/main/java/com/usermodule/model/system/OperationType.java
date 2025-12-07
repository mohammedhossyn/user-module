package com.usermodule.model.system;

import com.usermodule.utils.LabeledEnum;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public enum OperationType implements LabeledEnum {
    INSERT("insert"),
    UPDATE("update"),
    DELETE("delete"),;

    OperationType(String label){
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