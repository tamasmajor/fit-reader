package com.tamasmajor.fitreader.preprocessing;

import com.squareup.javapoet.*;
import com.tamasmajor.fitreader.preprocessing.annotation.FieldNumber;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.lang.model.element.*;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.List;

public class TypeCreator {
    private static final String TYPE_SUFFIX = "Message";
    private static final String DEFAULT_INDENT = "    ";

    private final Types typeUtils;
    private final Elements elementsUtil;

    public TypeCreator(Types typeUtils, Elements elementsUtil) {
        this.typeUtils = typeUtils;
        this.elementsUtil = elementsUtil;
    }

    public JavaFile create(TypeElement el) {
        TypeSpec.Builder typeSpec = TypeSpec.classBuilder(getGeneratedName(el))
                .addAnnotation(Getter.class)
                .addAnnotation(Builder.class)
                .addAnnotation(ToString.class)
                .addModifiers(Modifier.PUBLIC);

        generateFields(typeSpec, el);

        return JavaFile.builder(getPackageName(el), typeSpec.build()).indent(DEFAULT_INDENT).build();
    }

    private void generateFields(TypeSpec.Builder typeSpec, TypeElement el) {
        List<? extends Element> fields = getFields(el);
        fields.forEach(field -> {
            TypeName typeName = ClassName.get(field.asType());
            typeSpec.addField(FieldSpec.builder(typeName, field.getSimpleName().toString())
                    .addModifiers(Modifier.FINAL)
                    .addModifiers(Modifier.PRIVATE)
                    .build());
        });
    }

    private String getGeneratedName(TypeElement el) {
        return el.getSimpleName() + TYPE_SUFFIX;
    }

    private String getPackageName(TypeElement el) {
        PackageElement pkg = elementsUtil.getPackageOf(el);
        if (pkg.isUnnamed()) {
            throw new IllegalArgumentException("Unnamed package is not supported: " + el.getQualifiedName());
        }
        return pkg.getQualifiedName().toString();
    }

    private List<? extends Element> getFields(TypeElement typeElement) {
        return typeElement.getEnclosedElements().stream()
                .filter(e -> e.getKind() == ElementKind.FIELD)
                .filter(e -> e.getAnnotation(FieldNumber.class) != null)
                .toList();
    }

}
