package com.tamasmajor.fitreader.model.converter.unit;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CommonUnitConverterTest {

    @Test
    void shouldConvertFitAltitudeWithOffset() {
        assertEquals(0.0, CommonUnitConverter.fromFitAltitude(2500));
        assertEquals(100.0, CommonUnitConverter.fromFitAltitude(3000));
    }

    @Test
    void shouldConvertSemicirclesToDegrees() {
        assertEquals(0.0, CommonUnitConverter.semicirclesToDegrees(0));
        assertEquals(47.51896063797176, CommonUnitConverter.semicirclesToDegrees(566923283));
    }

}