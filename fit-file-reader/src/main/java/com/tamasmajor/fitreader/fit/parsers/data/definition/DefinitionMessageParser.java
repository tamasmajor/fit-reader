package com.tamasmajor.fitreader.fit.parsers.data.definition;

import com.tamasmajor.fitreader.fit.model.data.BaseType;
import com.tamasmajor.fitreader.fit.model.data.definition.DefinitionMessage;
import com.tamasmajor.fitreader.fit.model.data.definition.FieldDefinition;
import com.tamasmajor.fitreader.fit.parsers.data.DataHolder;
import com.tamasmajor.fitreader.util.ByteUtil;
import lombok.val;

import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

import static com.tamasmajor.fitreader.util.ByteUtil.isBitSet;
import static java.util.stream.Collectors.joining;

public class DefinitionMessageParser {
    private static final int HEADER_DEVELOPER_EXTENDED_BIT_POSITION = 5;

    private static final int ARCHITECTURE_FIELD_POSITION = 1;
    private static final int GLOBAL_MESSAGE_NUMBER_POSITION = 2;
    private static final int NUMBER_OF_FIELDS_POSITION = 4;

    private static final int RESERVED_FIELD_LENGTH = 1;
    private static final int ARCHITECTURE_FIELD_LENGTH = 1;
    private static final int GLOBAL_MESSAGE_NUMBER_FIELD_LENGTH = 2;
    private static final int NUMBER_OF_FIELDS_FIELD_LENGTH = 1;
    private static final int NUMBER_OF_DEV_EXT_FIELDS_FIELD_LENGTH = 1;
    private static final int FIELD_DEFINITION_LENGTH = 3;

    private static final int COMMON_FIELD_LENGTH = RESERVED_FIELD_LENGTH + ARCHITECTURE_FIELD_LENGTH +
            GLOBAL_MESSAGE_NUMBER_FIELD_LENGTH + NUMBER_OF_FIELDS_FIELD_LENGTH;

    private record RecordHeader(int localMessage, boolean hasDevFields) {}
    private record FieldCount(int numberOfFields, int numberOfDevFields) {}

    public DefinitionMessage parse(DataHolder dataHolder) {
        val recordHeader = parseRecordHeader(dataHolder.getCurrentByte());
        dataHolder.movePositionBy(1);

        val fieldCount = parseNumberOfFields(recordHeader, dataHolder);
        byte[] recordBytes = getRecordBytes(fieldCount, dataHolder);

        val builder  = DefinitionMessage.builder();
        addLocalMessageNumber(builder, recordHeader);
        addArchitecture(builder, recordBytes);
        addGlobalMessageNumber(builder, recordBytes);
        addFields(builder, fieldCount, recordBytes);
        addDevFields(builder, fieldCount, recordBytes);

        dataHolder.movePositionBy(-1);
        return builder.build();
    }

    private RecordHeader parseRecordHeader(byte recordHeaderByte) {
        val hasDeveloperFields = isBitSet(recordHeaderByte, HEADER_DEVELOPER_EXTENDED_BIT_POSITION);
        Function<Integer,String> bitMapping = p -> ByteUtil.isBitSet(recordHeaderByte, p) ? "1" : "0";
        val localMessageBits = IntStream.rangeClosed(0, 3).boxed().sorted(Comparator.reverseOrder()).map(bitMapping).collect(joining());
        val localMessageNumber = Integer.parseInt(localMessageBits, 2);
        return new RecordHeader(localMessageNumber, hasDeveloperFields);
    }

    private FieldCount parseNumberOfFields(RecordHeader header, DataHolder dataHolder) {
        val numberOfFields = dataHolder.getNextBytes(NUMBER_OF_FIELDS_POSITION + 1)[NUMBER_OF_FIELDS_POSITION];
        int numOfDevFields = 0;
        if (header.hasDevFields()) {
            val numOfDevExtFieldPos = COMMON_FIELD_LENGTH + numberOfFields * FIELD_DEFINITION_LENGTH;
            numOfDevFields =  dataHolder.getNextBytes(numOfDevExtFieldPos + 1)[numOfDevExtFieldPos];
        }
        return new FieldCount(numberOfFields, numOfDevFields);
    }

    private void addLocalMessageNumber(DefinitionMessage.DefinitionMessageBuilder builder, RecordHeader header) {
        builder.localMessageNumber(header.localMessage());
    }

    private void addArchitecture(DefinitionMessage.DefinitionMessageBuilder builder, byte[] recordBytes) {
        builder.architecture(parseArchitecture(recordBytes));
    }

    private void addGlobalMessageNumber(DefinitionMessage.DefinitionMessageBuilder builder, byte[] recordBytes) {
        val architecture = parseArchitecture(recordBytes);
        val byteOrder = DefinitionMessage.Architecture.LITTLE_ENDIAN == architecture ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN;
        val globalMessageBytes = Arrays.copyOfRange(recordBytes, GLOBAL_MESSAGE_NUMBER_POSITION, GLOBAL_MESSAGE_NUMBER_POSITION + GLOBAL_MESSAGE_NUMBER_FIELD_LENGTH);
        builder.globalMessageNumber(ByteUtil.asInt(globalMessageBytes, byteOrder));
    }

    private void addFields(DefinitionMessage.DefinitionMessageBuilder builder, FieldCount fieldCount, byte[] recordBytes) {
        builder.numberOfFields(fieldCount.numberOfFields());
        if (fieldCount.numberOfFields() > 0) {
            val fieldDefinitionLength = fieldCount.numberOfFields() * FIELD_DEFINITION_LENGTH;
            byte[] fieldDefinitionBytes = Arrays.copyOfRange(recordBytes, NUMBER_OF_FIELDS_POSITION + 1, NUMBER_OF_FIELDS_POSITION + fieldDefinitionLength  + 1);
            val fieldDefinitions =  parseFieldDefinitions(fieldCount.numberOfFields(), fieldDefinitionBytes);
            builder.fieldDefinitions(fieldDefinitions);
        }
    }

    private void addDevFields(DefinitionMessage.DefinitionMessageBuilder builder, FieldCount fieldCount, byte[] recordBytes) {
        builder.numberOfDevFields(fieldCount.numberOfDevFields());
        if (fieldCount.numberOfDevFields() > 0) {
            val definitionStart = NUMBER_OF_FIELDS_POSITION + fieldCount.numberOfFields * FIELD_DEFINITION_LENGTH + 2;
            val fieldDefinitionLength = fieldCount.numberOfDevFields() * FIELD_DEFINITION_LENGTH;
            byte[] fieldDefinitionBytes = Arrays.copyOfRange(recordBytes, definitionStart, definitionStart + fieldDefinitionLength);
            val fieldDefinitions = parseFieldDefinitions(fieldCount.numberOfDevFields(), fieldDefinitionBytes);
            builder.devFieldDefinitions(fieldDefinitions);
        }
    }

    private DefinitionMessage.Architecture parseArchitecture(byte[] recordBytes) {
        return (recordBytes[ARCHITECTURE_FIELD_POSITION] & 0xFF) == 0
                ? DefinitionMessage.Architecture.LITTLE_ENDIAN
                : DefinitionMessage.Architecture.BIG_ENDIAN;
    }

    private List<FieldDefinition> parseFieldDefinitions(int numberOfFields, byte[] fieldDefinitionBytes) {
        if (fieldDefinitionBytes.length != numberOfFields * FIELD_DEFINITION_LENGTH) {
            throw new IllegalArgumentException("Field definition bytes length is invalid");
        }
        List<FieldDefinition> definitions = new ArrayList<>();
        for (int i = 0; i < numberOfFields; i++) {
            val fieldDefinitionNumber = fieldDefinitionBytes[i * 3] & 0xFF;
            val size = fieldDefinitionBytes[i * 3 + 1];
            val baseType = BaseType.fromCode(fieldDefinitionBytes[i * 3 + 2] & 0xFF);
            definitions.add(new FieldDefinition(fieldDefinitionNumber, size, baseType));
        }
        return definitions;
    }

    private byte[] getRecordBytes(FieldCount fieldCount, DataHolder dataHolder) {
        int recordLength = COMMON_FIELD_LENGTH + fieldCount.numberOfFields() * FIELD_DEFINITION_LENGTH;
        if (fieldCount.numberOfDevFields() > 0) {
            recordLength += NUMBER_OF_DEV_EXT_FIELDS_FIELD_LENGTH + fieldCount.numberOfDevFields() * FIELD_DEFINITION_LENGTH;
        }
        return dataHolder.getNextBytes(recordLength);
    }

}
