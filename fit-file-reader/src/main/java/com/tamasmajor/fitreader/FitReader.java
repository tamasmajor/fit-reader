package com.tamasmajor.fitreader;

import com.tamasmajor.fitreader.fit.model.data.DataMessage;
import com.tamasmajor.fitreader.fit.model.data.definition.DefinitionMessage;
import com.tamasmajor.fitreader.fit.model.data.header.normal.MessageType;
import com.tamasmajor.fitreader.fit.parsers.data.DataHolder;
import com.tamasmajor.fitreader.fit.parsers.data.DataMessageParser;
import com.tamasmajor.fitreader.fit.parsers.data.definition.DefinitionMessageParser;
import com.tamasmajor.fitreader.fit.parsers.data.header.RecordHeaderParser;
import com.tamasmajor.fitreader.fit.parsers.file.FileStructureParser;
import com.tamasmajor.fitreader.fit.parsers.header.HeaderParser;
import lombok.val;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FitReader {
    private final FileStructureParser fitFileStructureParser = new FileStructureParser();
    private final HeaderParser headerParser = new HeaderParser();
    private final RecordHeaderParser recordHeaderParser = new RecordHeaderParser();
    private final DefinitionMessageParser definitionMessageParser = new DefinitionMessageParser();
    private final DataMessageParser dataMessageParser = new DataMessageParser();

    public List<DataMessage> read(Path file) {
        val data = readFile(file);
        return read(data);
    }

    public List<DataMessage> read(byte[] data) {
        val fitFile = fitFileStructureParser.parse(data);

        val header = headerParser.parse(fitFile.getHeaderBytes());
        var dataHolder = new DataHolder(fitFile.getDataBytes());

        val definitionMessages = new HashMap<Integer, DefinitionMessage>();
        val dataMessages = new ArrayList<DataMessage>();
        do {
            // TODO: handle the timestamp records
            val messageType = recordHeaderParser.parseNormalHeader(dataHolder.getCurrentByte()).getMessageType();
            if (MessageType.DEFINITION == messageType || MessageType.DEFINITION_DEV_EXTENDED == messageType) {
                val definitionMessage = definitionMessageParser.parse(dataHolder);
                definitionMessages.put(definitionMessage.getLocalMessageNumber(), definitionMessage);
                dataHolder.movePositionBy(definitionMessage.length());
            } else if (MessageType.DATA == messageType) {
                val dataMessage = dataMessageParser.parse(dataHolder, definitionMessages);
                dataMessages.add(dataMessage);
                dataHolder.movePositionBy(dataMessage.length());
            } else {
                throw new RuntimeException("Message type is not yet supported: " + messageType.name());
            }
        } while (dataHolder.getCurrentPosition() < dataHolder.length());
        return dataMessages;
    }

    private byte[] readFile(Path file) {
        try {
            return Files.readAllBytes(file);
        } catch (IOException ioe) {
            throw new RuntimeException("Could not read the FIT file", ioe);
        }
    }
}
