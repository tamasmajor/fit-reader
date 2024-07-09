package com.tamasmajor.fitreader.model;

import lombok.Getter;

@Getter
public enum FitMessageType {
    RECORD(20);

    private final int messageNumber;

    FitMessageType(int messageNumber) {
        this.messageNumber = messageNumber;
    }

}
