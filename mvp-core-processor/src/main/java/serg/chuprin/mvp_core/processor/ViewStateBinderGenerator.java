package serg.chuprin.mvp_core.processor;

import com.google.common.reflect.TypeToken;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import serg.chuprin.mvp_core.ViewStateProvider;

class ViewStateBinderGenerator {

    private static final String FACTORY_CLASS_NAME = "MvpViewStateBinder";
    private static final String MAP_FIELD = "viewStatesMap";
    private static final String GET_VIEW_STATE_METHOD = "getView";
    private static final String PRESENTER_PARAM = "presenter";
    private final Filer filer;
    private final List<Pair<TypeElement, String>> presenterViewParis;

    ViewStateBinderGenerator(Filer filer, List<Pair<TypeElement, String>> presenterViewParis) {
        this.filer = filer;
        this.presenterViewParis = presenterViewParis;
    }

    boolean generate() {
        try {
            JavaFile.builder("serg.chuprin.mvp_core", createFactoryClass())
                    .build()
                    .writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private TypeSpec createFactoryClass() {
        ParameterizedTypeName mapType = ParameterizedTypeName.get(Map.class,
                getClassType(),
                ViewStateProvider.class);

        return TypeSpec.classBuilder(FACTORY_CLASS_NAME)
                .addAnnotation(AnnotationSpec.builder(SuppressWarnings.class)
                        .addMember("value", "{$N}", "\"unchecked\"")
                        .build())
                .addField(FieldSpec.builder(mapType,
                        MAP_FIELD,
                        Modifier.PRIVATE,
                        Modifier.FINAL,
                        Modifier.STATIC)
                        .build())
                .addModifiers(Modifier.FINAL, Modifier.PUBLIC)
                .addStaticBlock(createStaticBlock())
                .addMethod(createGetterMethod())
                .build();
    }

    private MethodSpec createGetterMethod() {
        return MethodSpec.methodBuilder(GET_VIEW_STATE_METHOD)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(getClassType(), PRESENTER_PARAM)
                .addCode(CodeBlock.builder()
                        .addStatement("ViewStateProvider provider = $N.get($N)", MAP_FIELD, PRESENTER_PARAM)
                        .addStatement("if (provider == null) return null")
                        .addStatement("return provider.$N()", ViewStateProviderGenerator.PROVIDE_VIEW_STATE_METHOD)
                        .build())
                .returns(Object.class)
                .build();
    }

    private Type getClassType() {
        return new TypeToken<Class<?>>() {
        }.getType();
    }

    private CodeBlock createStaticBlock() {

        CodeBlock.Builder builder = CodeBlock.builder()
                .addStatement("$N = new $T()", MAP_FIELD, HashMap.class);

        for (Pair<TypeElement, String> pair : presenterViewParis) {

            builder.addStatement("$N.put($N.class,new $N())", MAP_FIELD,
                    ClassName.get(pair.getFirst()).toString(),
                    new ViewStateProviderGenerator(filer, pair).generate());
        }
        return builder.build();
    }
}
