package com.tamasmajor.fitreader.model.converter.unit;

import java.math.BigDecimal;
import java.math.MathContext;

public class CommonUnitConverter {
    private static final BigDecimal SEMICIRCLES_MULTIPLIER = new BigDecimal("180").divide(BigDecimal.valueOf(Math.pow(2, 31)), MathContext.DECIMAL64);

    public static Double fromFitAltitude(Integer value) {
        return (double) value / 5.0 - 500.0;
    }

    public static Double semicirclesToDegrees(Integer semicircles) {
        return BigDecimal.valueOf(semicircles).multiply(SEMICIRCLES_MULTIPLIER, MathContext.DECIMAL64).doubleValue();
    }

}
