package com.tamasmajor.fitreader.preprocessing;

import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import java.util.Set;

@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_17)
@SupportedAnnotationTypes("{com.tamasmajor.fitreader.preprocessing.annotation.Message}")
public class TypePreprocessor extends AbstractProcessor {

    private Elements elementsUtil;
    private Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        processingEnvironment.getMessager().printMessage(Diagnostic.Kind.NOTE, "FIT preprocessor");
        elementsUtil = processingEnvironment.getElementUtils();
        filer = processingEnvironment.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        super.init(processingEnv);
        System.out.println("Test");
        return false;
    }

}
