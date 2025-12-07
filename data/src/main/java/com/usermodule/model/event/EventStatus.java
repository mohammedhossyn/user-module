package com.usermodule.model.event;

import com.usermodule.utils.LabeledEnum;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public enum EventStatus implements LabeledEnum {
    UNSEEN("unseen"),
    SEEN("seen"),;
    
    EventStatus(String label){
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
