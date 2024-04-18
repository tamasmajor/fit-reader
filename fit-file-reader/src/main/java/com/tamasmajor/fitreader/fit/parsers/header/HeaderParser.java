package com.tamasmajor.fitreader.fit.parsers.header;

import com.tamasmajor.fitreader.fit.model.header.Header;
import com.tamasmajor.fitreader.util.ByteUtil;
import lombok.val;

import java.util.Arrays;

import static java.nio.ByteOrder.LITTLE_ENDIAN;

public class HeaderParser {

    public Header parse(byte[] headerData) {
        val headerLength = headerData[0];
        val dataSize = ByteUtil.asInt(Arrays.copyOfRange(headerData, 4, 8), LITTLE_ENDIAN);
        return Header.builder()
                .headerSize(headerLength)
                .dataSize(dataSize)
                .build();
    }

}
