package com.tamasmajor.fitreader.preprocessing;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.tamasmajor.fitreader.preprocessing.annotation.DataDefinition;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_17)
@SupportedAnnotationTypes({"com.tamasmajor.fitreader.preprocessing.annotation.DataDefinition"})
public class TypePreprocessor extends AbstractProcessor {

    private TypeCreator typeCreator;
    private Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        processingEnvironment.getMessager().printMessage(Diagnostic.Kind.NOTE, "");
        processingEnvironment.getMessager().printMessage(Diagnostic.Kind.NOTE, "-------------------------[ FIT preprocessing ]--------------------------");
        Elements elementsUtil = processingEnvironment.getElementUtils();
        Types typeUtils = processingEnvironment.getTypeUtils();
        filer = processingEnvironment.getFiler();
        typeCreator = new TypeCreator(typeUtils, elementsUtil);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<TypeElement> annotatedDefinitionTypes = collectDataDefinitionTypes(roundEnv);
        List<JavaFile> files = annotatedDefinitionTypes.stream().map(t -> typeCreator.create(t)).toList();
        files.forEach(jf -> {
            try {
                jf.writeTo(filer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return false;
    }

    private Set<TypeElement> collectDataDefinitionTypes(RoundEnvironment roundEnvironment) {
        return roundEnvironment.getElementsAnnotatedWith(DataDefinition.class)
                .stream()
                .map(TypeElement.class::cast)
                .collect(Collectors.toSet());
    }

}
