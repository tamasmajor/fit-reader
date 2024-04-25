package com.tamasmajor.fitreader.fit.model.data;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public enum BaseType {
    ENUM(0),
    SINT8(1),
    UINT8(2),
    STRING(7),
    UINT8Z(10),
    BYTE(13),
    SINT16(131),
    UINT16(132),
    SINT32(133),
    UINT32(134),
    FLOAT32(136),
    FLOAT64(137),
    UINT16Z(139),
    UINT32Z(140),
    SINT64(142),
    UINT64(143),
    UINT64Z(144);

    private final static Map<Integer, BaseType> byCode = Stream.of(values()).collect(toMap(v -> v.code, v -> v));

    private final int code;

    BaseType(int code) {
        this.code = code;
    }

    int getCode() {
        return code;
    }

    public static BaseType fromCode(int code) {
        return Optional.ofNullable(byCode.get(code))
                .orElseThrow(() -> new IllegalArgumentException("Unsupported BaseType code " + code));
    }

}
