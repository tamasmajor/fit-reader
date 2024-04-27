package com.tamasmajor.fitreader.fit.parsers.data;

import com.tamasmajor.fitreader.fit.model.data.DataMessage;
import com.tamasmajor.fitreader.fit.model.data.Value;
import com.tamasmajor.fitreader.fit.model.data.definition.DefinitionMessage;
import com.tamasmajor.fitreader.fit.model.data.definition.FieldDefinition;
import com.tamasmajor.fitreader.util.ByteUtil;
import lombok.val;

import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

public class DataMessageParser {

    private record RecordHeader(int localMessage) {}

    public DataMessage parse(DataHolder dataHolder, Map<Integer, DefinitionMessage> definitionMessages) {
        val recordHeader = parseRecordHeader(dataHolder.getCurrentByte());
        val localMessage = recordHeader.localMessage();
        val definitionMessage = definitionMessages.get(localMessage);

        val dataRecord = getDataRecord(definitionMessage, dataHolder);
        val values = collectValues(definitionMessage, dataRecord);

        return new DataMessage(localMessage, values, definitionMessage);
    }

    private DataMessageParser.RecordHeader parseRecordHeader(byte recordHeaderByte) {
        Function<Integer,String> bitMapping = p -> ByteUtil.isBitSet(recordHeaderByte, p) ? "1" : "0";
        val localMessageBits = IntStream.rangeClosed(0, 3).boxed().sorted(Comparator.reverseOrder()).map(bitMapping).collect(joining());
        val localMessageNumber = Integer.parseInt(localMessageBits, 2);
        return new DataMessageParser.RecordHeader(localMessageNumber);
    }

    private List<Value> collectValues(DefinitionMessage definitionMessage, byte[] dataRecord) {
        val fieldDefinitions = Stream.concat(definitionMessage.getFieldDefinitions().stream(),
                definitionMessage.getDevFieldDefinitions().stream()).toList();

        var position = 0;
        List<Value> values = new ArrayList<>();
        for (FieldDefinition fieldDefinition : fieldDefinitions) {
            val fieldSize = fieldDefinition.getSize();
            val fieldData = Arrays.copyOfRange(dataRecord, position, position + fieldSize);
            position += fieldSize;
            values.add(new Value(fieldData));
        }
        return values;
    }

    private byte[] getDataRecord(DefinitionMessage definitionMessage, DataHolder dataHolder) {
        int fieldsLength = Stream.concat(definitionMessage.getFieldDefinitions().stream(), definitionMessage.getDevFieldDefinitions().stream())
                .map(FieldDefinition::getSize).reduce(0, Integer::sum);
        val recordBytes = dataHolder.getNextBytes(fieldsLength + 1);
        return Arrays.copyOfRange(recordBytes, 1, fieldsLength + 1);
    }

}
