package com.tamasmajor.fitreader;

import com.tamasmajor.fitreader.fit.parsers.FileStructureParser;
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

        // TODO
        val testFile = Main.class.getClassLoader().getResource("testfiles/test.fit").getPath();
        val input = Files.readAllBytes(Paths.get(testFile));

        val fitFile = fitFileStructureParser.parse(input);
    }

}