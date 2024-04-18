package com.tamasmajor.fitreader.fit.model.file;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class FitFile {
    byte[] headerBytes;
    byte[] dataBytes;
    byte[] crcBytes;
}
