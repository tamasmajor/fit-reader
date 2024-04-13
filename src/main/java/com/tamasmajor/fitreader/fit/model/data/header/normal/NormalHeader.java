package com.tamasmajor.fitreader.fit.model.data.header.normal;

import com.tamasmajor.fitreader.fit.model.data.header.RecordType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@Builder
@ToString
public class NormalHeader {
    private RecordType recordType;
    private MessageType messageType;
}
