package com.tamasmajor.fitreader.fit.parsers;

import com.tamasmajor.fitreader.fit.model.file.FitFile;
import lombok.val;

import java.util.Arrays;

public class FileStructureParser {

    public FitFile parse(byte[] input) {
        if (input.length < 14) {
            throw new IllegalArgumentException("Invalid length: " + input.length + ". Minimum is 14");
        }
        val headerLength = input[0];
        val headerBytes = Arrays.copyOfRange(input, 0, headerLength);
        val dataBytes = Arrays.copyOfRange(input, headerLength, input.length - 2);
        val crcBytes = Arrays.copyOfRange(input, input.length - 2, input.length);
        return new FitFile(headerBytes, dataBytes, crcBytes);
    }

}
