package com.tamasmajor.fitreader;

import com.tamasmajor.fitreader.fit.model.data.DataMessage;
import com.tamasmajor.fitreader.fit.model.data.Value;
import com.tamasmajor.fitreader.fit.model.data.definition.DefinitionMessage;
import com.tamasmajor.fitreader.fit.model.data.definition.FieldDefinition;
import com.tamasmajor.fitreader.fit.model.data.header.normal.MessageType;
import com.tamasmajor.fitreader.fit.parsers.data.DataHolder;
import com.tamasmajor.fitreader.fit.parsers.data.DataMessageParser;
import com.tamasmajor.fitreader.fit.parsers.data.definition.DefinitionMessageParser;
import com.tamasmajor.fitreader.fit.parsers.data.header.RecordHeaderParser;
import com.tamasmajor.fitreader.fit.parsers.file.FileStructureParser;
import com.tamasmajor.fitreader.fit.parsers.header.HeaderParser;
import com.tamasmajor.fitreader.model.FitFile;
import com.tamasmajor.fitreader.model.FitMessageType;
import com.tamasmajor.fitreader.model.definition.RecordMessage;
import lombok.val;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FitReader {
    private final static List<FitMessageType> DEFAULT_RELEVANT_MESSAGES = List.of(FitMessageType.RECORD);

    private final FileStructureParser fitFileStructureParser = new FileStructureParser();
    private final HeaderParser headerParser = new HeaderParser();
    private final RecordHeaderParser recordHeaderParser = new RecordHeaderParser();
    private final DefinitionMessageParser definitionMessageParser = new DefinitionMessageParser();
    private final DataMessageParser dataMessageParser = new DataMessageParser();

    public FitFile read(Path file) {
        return read(file, DEFAULT_RELEVANT_MESSAGES);
    }

    public FitFile read(Path file, List<FitMessageType> relevantMessageTypes) {
        val dataMessage = readDataMessages(file);
        val fitFileBuilder = FitFile.builder();
        relevantMessageTypes.forEach(messageType -> setMessages(fitFileBuilder, dataMessage, messageType));
        return fitFileBuilder.build();
    }

    private void setMessages(FitFile.FitFileBuilder builder, List<DataMessage> dataMessages, FitMessageType messageType) {
        val relevantMessages = dataMessages.stream().filter(m -> m.getDefinitionMessage().getGlobalMessageNumber() == messageType.getMessageNumber());
        val relevantMessagesAsPropertyMaps = relevantMessages.map(this::asPropertyMap);
        if (FitMessageType.RECORD == messageType) {
            builder.records(relevantMessagesAsPropertyMaps.map(RecordMessage::of).toList());
        }
    }

    private Map<Integer, byte[]> asPropertyMap(DataMessage dataMessage) {
        Map<Integer, byte[]> tmp = new HashMap<>();
        List<FieldDefinition> fieldDefinitions = dataMessage.getDefinitionMessage().getFieldDefinitions();
        List<Value> values = dataMessage.getValues();
        for (int i = 0; i < fieldDefinitions.size(); i++) {
            tmp.put(fieldDefinitions.get(i).getFieldDefinitionNumber(), values.get(i).getData());
        }
        return tmp;
    }

    private List<DataMessage> readDataMessages(Path file) {
        val data = readFile(file);
        return readDataMessages(data);
    }

    private List<DataMessage> readDataMessages(byte[] data) {
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
