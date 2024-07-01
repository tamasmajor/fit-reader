package com.tamasmajor.fitreader.preprocessing.annotation.unitoconverter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface UnitConverter {
    Class<?> converter();
    String method();
    Class<?> sourceType() default Void.class;
}
