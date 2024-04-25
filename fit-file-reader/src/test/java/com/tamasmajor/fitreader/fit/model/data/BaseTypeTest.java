package com.tamasmajor.fitreader.fit.model.data;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BaseTypeTest {

    @ParameterizedTest(name = "Mapping type {0} from its code")
    @EnumSource(value = BaseType.class)
    void shouldReturnTypeForCode(BaseType type) {
        assertEquals(type, BaseType.fromCode(type.getCode()));
    }

    @Test
    void shouldThrowExceptionWhenTypeCodeIsUnknown() {
        val ex = assertThrows(IllegalArgumentException.class, () -> BaseType.fromCode(999));
        assertEquals("Unsupported BaseType code 999", ex.getMessage());
    }

}