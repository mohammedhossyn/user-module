package com.usermodule.model.attachment;

import com.usermodule.utils.LabeledEnum;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public enum AttachmentCategory implements LabeledEnum {
    PRODUCER_NATIONAL_CODE("producer/nationalCode");

    AttachmentCategory(String label) {
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
