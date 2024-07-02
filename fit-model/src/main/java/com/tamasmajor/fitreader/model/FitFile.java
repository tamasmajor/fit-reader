package com.tamasmajor.fitreader.model;

import com.tamasmajor.fitreader.model.definition.RecordMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class FitFile {
    List<RecordMessage> records;
}
