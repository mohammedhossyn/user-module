package com.usermodule.model.user;

import com.usermodule.utils.LabeledEnum;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public enum InquiryStatus implements LabeledEnum {
    WAIT_FOR_APPROVE("waitForApprove"),
    APPROVED("approved"),
    REJECTED("rejected"),
    EXPIRED("expired");

    InquiryStatus(String label) {
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
