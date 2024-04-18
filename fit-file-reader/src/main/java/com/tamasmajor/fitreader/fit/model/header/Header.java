package com.tamasmajor.fitreader.fit.model.header;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class Header {
    // TODO: extend with the mapping for all properties
    int headerSize;
    int dataSize;
}
