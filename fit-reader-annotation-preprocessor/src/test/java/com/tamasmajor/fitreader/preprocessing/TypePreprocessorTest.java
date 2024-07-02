package com.tamasmajor.fitreader.preprocessing;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.annotation.processing.AbstractProcessor;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;

@ExtendWith(MockitoExtension.class)
class TypePreprocessorTest {

    Compiler compiler;

    @BeforeEach
    public void setupCompiler() throws Exception {
        Class<?> lombokAnnotationProcessor =
                getClass().getClassLoader().loadClass("lombok.launch.AnnotationProcessorHider$AnnotationProcessor");
        Class<?> lombokClaimingProcessor =
                getClass().getClassLoader().loadClass("lombok.launch.AnnotationProcessorHider$ClaimingProcessor");
        compiler = javac()
                .withProcessors(
                        new TypePreprocessor(),
                        (AbstractProcessor) lombokAnnotationProcessor.getDeclaredConstructor().newInstance(),
                        (AbstractProcessor) lombokClaimingProcessor.getDeclaredConstructor().newInstance()
                );
    }

    @Test
    void shouldConvertWithoutNoProperties() {
        Compilation compilation = compiler.compile(JavaFileObjects.forResource("testcases/SimpleNoProperties.java"));
        assertThat(compilation).succeeded();
        assertThat(compilation)
                .generatedSourceFile("testcases/SimpleNoPropertiesMessage")
                .hasSourceEquivalentTo(JavaFileObjects.forResource("testcases/SimpleNoPropertiesMessage.java"));
    }

    @Test
    void shouldConvertWithSingleSimpleProperty() {
        Compilation compilation = compiler.compile(JavaFileObjects.forResource("testcases/SimpleProperty.java"));
        assertThat(compilation).succeeded();
        assertThat(compilation)
                .generatedSourceFile("testcases/SimplePropertyMessage")
                .hasSourceEquivalentTo(JavaFileObjects.forResource("testcases/SimplePropertyMessage.java"));
    }

    @Test
    void shouldConvertUsingCustomConverter() {
        Compilation compilation = compiler.compile(JavaFileObjects.forResource("testcases/PropertyCustomConverter.java"));
        assertThat(compilation).succeeded();
        assertThat(compilation)
                .generatedSourceFile("testcases/PropertyCustomConverterMessage")
                .hasSourceEquivalentTo(JavaFileObjects.forResource("testcases/PropertyCustomConverterMessage.java"));
    }


}