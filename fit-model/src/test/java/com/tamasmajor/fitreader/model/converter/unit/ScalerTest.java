package com.tamasmajor.fitreader.model.converter.unit;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ScalerTest {

    @Test
    void shouldDivideBy10DoDivision() {
        assertEquals(1.0, Scaler.divideBy10(10));
        assertEquals(-1.0, Scaler.divideBy10(-10));
        assertEquals(1.3, Scaler.divideBy10(13));
    }

    @Test
    void shouldDivideBy100DoDivision() {
        assertEquals(1.0, Scaler.divideBy100(100));
        assertEquals(-1.0, Scaler.divideBy100(-100));
        assertEquals(1.33, Scaler.divideBy100(133));
    }

}