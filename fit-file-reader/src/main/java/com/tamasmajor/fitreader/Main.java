package com.tamasmajor.fitreader;

import lombok.val;

import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {
        FitReader fitReader = new FitReader();
        val dataMessages = fitReader.read(Paths.get(getClass().getClassLoader().getResource("testfiles/Activity.fit").getPath()));
        dataMessages.forEach(System.out::println);
    }

}