package serg.chuprin.mvp_core.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import serg.chuprin.mvp_core.viewstate.MvpViewState;

class ViewStateProviderGenerator {
    static final String PROVIDE_VIEW_STATE_METHOD = "provideViewState";
    private static final String VIEW_STATE_PROVIDER_SUFFIX = "Provider";
    private final Pair<TypeElement, String> pair;
    private final ClassName className;
    private final String providerName;
    private final Filer filer;

    ViewStateProviderGenerator(Filer filer, Pair<TypeElement, String> pair) {
        this.pair = pair;
        className = ClassName.bestGuess(pair.getSecond());
        this.filer = filer;
        providerName = className.simpleName() + VIEW_STATE_PROVIDER_SUFFIX;
    }

    String generate() {
        TypeSpec providerClass = TypeSpec.classBuilder(providerName)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(serg.chuprin.mvp_core.ViewStateProvider.class)
                .addMethod(MethodSpec.methodBuilder(PROVIDE_VIEW_STATE_METHOD)
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PUBLIC)
                        .returns(MvpViewState.class)
                        .addCode(CodeBlock.builder()
                                .addStatement("return new $N()", pair.getSecond())
                                .build())
                        .build())
                .build();

        try {
            JavaFile.builder(className.packageName(), providerClass)
                    .build()
                    .writeTo(filer);

        } catch (IOException e) {
            e.printStackTrace();
            return getProviderName();
        }
        return getProviderName();
    }

    private String getProviderName() {
        return className.packageName() + "." + providerName;
    }
}
