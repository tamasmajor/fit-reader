package com.tamasmajor.fitreader.model.converter.unit;

public class Scaler {

    public static Double divideBy10(Integer value) {
        return (double) value / 10.0;
    }

    public static Double divideBy100(Integer value) {
        return (double) value / 100.0;
    }

}
