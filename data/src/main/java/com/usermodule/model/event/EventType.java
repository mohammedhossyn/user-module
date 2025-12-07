package com.usermodule.model.event;


import com.usermodule.utils.LabeledEnum;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public enum EventType implements LabeledEnum {
    NEW_ORDER("newOrder"),
    CHANGE_STATUS_ORDER("changeStatusOrder"),
    ASSIGN_ORDER("assignOrder"),;

    private EventType(String label) {
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
