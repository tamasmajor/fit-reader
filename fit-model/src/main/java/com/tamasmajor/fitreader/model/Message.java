package com.tamasmajor.fitreader.model;

import com.tamasmajor.fitreader.model.definition.RecordMessage;
import lombok.Getter;

import java.util.Map;
import java.util.function.Function;

@Getter
public enum Message {
    RECORD(20, RecordMessage::of);

    private final int messageNumber;
    private final Function<Map<Integer, byte[]>, Object> mapping;

    Message(int messageNumber, Function<Map<Integer, byte[]>, Object> mapping) {
        this.messageNumber = messageNumber;
        this.mapping = mapping;
    }

}
