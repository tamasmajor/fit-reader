package com.tamasmajor.fitreader.fit.model.data.definition;

import com.tamasmajor.fitreader.fit.model.data.BaseType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@AllArgsConstructor
@ToString
public class FieldDefinition {
    private int fieldDefinitionNumber;
    private int size;
    private BaseType baseType;
}
