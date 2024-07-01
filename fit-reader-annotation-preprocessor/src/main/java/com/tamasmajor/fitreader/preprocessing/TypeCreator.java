package com.tamasmajor.fitreader.preprocessing;

import com.squareup.javapoet.*;
import com.tamasmajor.fitreader.preprocessing.annotation.FieldNumber;
import com.tamasmajor.fitreader.preprocessing.annotation.ValueConverter;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.lang.model.element.*;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.List;
import java.util.Map;

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
        generateFactory(typeSpec, el);

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

    private void generateFactory(TypeSpec.Builder typeSpec, TypeElement el) {
        String valueConverterReference = getValueConverterReference(el);
        List<? extends Element> fields = getFields(el);
        ClassName generatedType = ClassName.get(getPackageName(el), getGeneratedName(el));
        String generatedTypeName = getGeneratedName(el);

        CodeBlock.Builder builderMethodBody = CodeBlock.builder();
        builderMethodBody.add("$LBuilder builder = $L.builder();\n", generatedTypeName, generatedTypeName);
        builderMethodBody.add("values.forEach((key, value) -> {\n");
        fields.forEach(field -> {
            String fieldName = field.getSimpleName().toString();
            int fieldNumber = field.getAnnotation(FieldNumber.class).value();
            builderMethodBody.add("\tif (key == $L) {\n", fieldNumber);
            builderMethodBody.add("\t\tbuilder.$L($L.convert(value, $L.class));\n", fieldName, valueConverterReference, field.asType().toString());
            builderMethodBody.add("\t}\n");
        });
        builderMethodBody.add("});\n");
        builderMethodBody.addStatement("return builder.build()");

        typeSpec.addMethod(MethodSpec.methodBuilder("of")
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.STATIC)
                .addParameter(createBuilderMethodParameterMap())
                .returns(generatedType)
                .addCode(builderMethodBody.build())
                .build());
    }

    private ParameterSpec createBuilderMethodParameterMap() {
        ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                ClassName.get(Integer.class),
                ArrayTypeName.of(TypeName.BYTE));

        return ParameterSpec.builder(parameterizedTypeName, "values").build();
    }

    private String getValueConverterReference(Element el) {
        String reference = null;
        try {
            ValueConverter annotation = el.getAnnotation(ValueConverter.class);
            annotation.converter(); // This line will throw MirroredTypeException
        } catch (MirroredTypeException mte) {
            TypeMirror mirror = mte.getTypeMirror();
            TypeElement element = (TypeElement) typeUtils.asElement(mirror);
            reference = getPackageName(element) + "." + element.getSimpleName().toString();

        }
        return reference;
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
