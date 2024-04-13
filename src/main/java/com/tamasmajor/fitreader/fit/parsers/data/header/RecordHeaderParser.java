package com.tamasmajor.fitreader.fit.parsers.data.header;

import com.tamasmajor.fitreader.fit.model.data.header.RecordType;
import com.tamasmajor.fitreader.fit.model.data.header.normal.MessageType;
import com.tamasmajor.fitreader.fit.model.data.header.normal.NormalHeader;
import lombok.val;

import static com.tamasmajor.fitreader.util.ByteUtil.isBitSet;

public class RecordHeaderParser {
    private static final int RECORD_TYPE_BIT_POSITION = 7;
    private static final int MESSAGE_TYPE_BIT_POSITION = 6;
    private static final int DEVELOPER_EXTENDED_BIT_POSITION = 5;

    public NormalHeader parseNormalHeader(byte recordHeaderByte) {
        val recordType = getRecordType(recordHeaderByte);
        if (recordType != RecordType.NORMAL) {
            throw new IllegalArgumentException("It must represent a normal header");
        }
        return NormalHeader.builder()
                .recordType(getRecordType(recordHeaderByte))
                .messageType(getMessageType(recordHeaderByte))
                .build();
    }

    // TODO: parseCompressedTimestampHeader

    public RecordType getRecordType(byte recordHeaderByte) {
        if (isBitSet(recordHeaderByte, RECORD_TYPE_BIT_POSITION)) {
            return RecordType.TIMESTAMP;
        } else {
            return RecordType.NORMAL;
        }
    }

    private MessageType getMessageType(byte recordHeaderByte) {
        boolean definitionMessage = isBitSet(recordHeaderByte, MESSAGE_TYPE_BIT_POSITION);
        boolean developerExtendedDefinition = isBitSet(recordHeaderByte, DEVELOPER_EXTENDED_BIT_POSITION);
        if (definitionMessage && developerExtendedDefinition) {
            return MessageType.DEFINITION_DEV_EXTENDED;
        } else if (definitionMessage) {
            return MessageType.DEFINITION;
        } else {
            return MessageType.DATA;
        }
    }

}
