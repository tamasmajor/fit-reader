package com.tamasmajor.fitreader.testapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.tamasmajor.fitreader.FitReader;
import com.tamasmajor.fitreader.model.FitFile;
import com.tamasmajor.fitreader.model.definition.RecordMessage;
import lombok.val;

import java.nio.file.Paths;

public class App {

    public static void main(String[] args) throws Exception {
        new App().run();
    }

    private void run() throws Exception {
        val resource = Paths.get(getClass().getClassLoader().getResource("testfiles/Activity.fit").getPath());
        val fitReader = new FitReader();
        val fitFile = fitReader.read(resource);

        printAsJson(fitFile);
        printRecordsAsCsv(fitFile);
    }

    private void printAsJson(FitFile fitFile) throws Exception {
        val mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        val json = mapper.writeValueAsString(fitFile);
        System.out.println(json);
    }

    private void printRecordsAsCsv(FitFile fitFile) throws Exception {
        val mapper = new CsvMapper();
        val schema = mapper.schemaFor(RecordMessage.class).withHeader();
        val writer = mapper.writer(schema);
        val csv = writer.writeValueAsString(fitFile.getRecords());
        System.out.println(csv);
    }

}