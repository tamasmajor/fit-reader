package com.tamasmajor.fitreader;

import com.tamasmajor.fitreader.fit.parsers.data.DataHolder;
import com.tamasmajor.fitreader.fit.parsers.data.definition.DefinitionMessageParser;
import com.tamasmajor.fitreader.fit.parsers.data.header.RecordHeaderParser;
import com.tamasmajor.fitreader.fit.parsers.file.FileStructureParser;
import com.tamasmajor.fitreader.fit.parsers.header.HeaderParser;
import lombok.val;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws Exception {
        new Main().run();
    }

    private void run() throws IOException {
        val fitFileStructureParser = new FileStructureParser();
        val headerParser = new HeaderParser();
        val recordHeaderParser = new RecordHeaderParser();
        val definitionMessageParser = new DefinitionMessageParser();

        // TODO
        val testFile = Main.class.getClassLoader().getResource("testfiles/Activity.fit").getPath();
        val input = Files.readAllBytes(Paths.get(testFile));

        val fitFile = fitFileStructureParser.parse(input);
        val header = headerParser.parse(fitFile.getHeaderBytes());
        var dataHolder = new DataHolder(fitFile.getDataBytes());
        val type = recordHeaderParser.getRecordType(dataHolder.getCurrentByte());
        val definitionMessage = definitionMessageParser.parse(dataHolder);
        System.out.println(definitionMessage);
    }

}