package serg.chuprin.processor;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;

import serg.chuprin.mvp_core.annotations.StateStrategyType;
import serg.chuprin.mvp_core.viewstate.MvpViewState;
import serg.chuprin.mvp_core.viewstate.ViewCommand;
import serg.chuprin.mvp_core.viewstate.strategy.AddToEndSingleStrategy;
import serg.chuprin.mvp_core.viewstate.strategy.StateStrategy;

class ViewStateGenerator {

    private static final String VIEW_STATE_SUFFIX = "$$ViewState";
    private static final String COMMAND_SUFFIX = "Command";
    private static final String EXECUTE_METHOD_NAME = "execute";
    private static final String APPLY_COMMAND_METHOD = "applyCommand";
    private static final String VIEW_PARAM = "view";
    private final Filer filer;
    private final Types typeUtils;
    private final TypeElement viewElement;
    private final ClassName viewInterface;
    private final ClassName viewCommandName;
    private final Class<? extends StateStrategy> defaultStrategy;

    ViewStateGenerator(TypeMirror mirror, Filer filer, Types typeUtils) {
        this.filer = filer;
        this.typeUtils = typeUtils;
        viewElement = (TypeElement) typeUtils.asElement(mirror);
        viewInterface = ClassName.get(viewElement);
        viewCommandName = ClassName.get(ViewCommand.class);
        defaultStrategy = AddToEndSingleStrategy.class;
    }

    String generate() {
        String stateName = String.format("%s%s", viewElement.getSimpleName(), VIEW_STATE_SUFFIX);
        TypeSpec stateClass = TypeSpec.classBuilder(stateName)
                .superclass(ParameterizedTypeName.get(ClassName.get(MvpViewState.class), viewInterface))
                .addSuperinterface(viewInterface)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addTypes(createInnerCommandClasses())
                .addMethods(createViewMethods())
                .build();

        String packageName = viewElement.getEnclosingElement().getSimpleName().toString();

        try {
            JavaFile.builder(packageName, stateClass).build().writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return String.format("%s.%s", packageName, stateName);
    }

    private Iterable<TypeSpec> createInnerCommandClasses() {
        Class<? extends StateStrategy> viewStrategy = getElemStrategyOrDefault(viewElement, defaultStrategy);

        List<TypeSpec> classes = new ArrayList<>();
        for (Element elem : viewElement.getEnclosedElements()) {
            if (elem.getKind() == ElementKind.METHOD) {

                classes.add(createCommandClass((ExecutableElement) elem,
                        getElemStrategyOrDefault(elem, viewStrategy)));
            }
        }
        return classes;
    }

    private TypeSpec createCommandClass(ExecutableElement method, Class<? extends StateStrategy> strategy) {
        Iterable<ParameterSpec> methodParams = getMethodParams(method);

        return TypeSpec.classBuilder(getCapitalizedName(method) + COMMAND_SUFFIX)
                .superclass(ParameterizedTypeName.get(viewCommandName, viewInterface))
                .addTypeVariables(getTypeVariables(method))
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addFields(createCommandFields(methodParams))
                .addMethod(createCommandConstructor(method, methodParams, strategy))
                .addMethod(createExecuteMethod(method, methodParams))
                .build();
    }

    private String getCapitalizedName(ExecutableElement method) {
        String simpleName = method.getSimpleName().toString();
        return simpleName.substring(0, 1).toUpperCase() + simpleName.substring(1);
    }

    private Iterable<FieldSpec> createCommandFields(Iterable<ParameterSpec> methodParams) {
        List<FieldSpec> fields = new ArrayList<>();

        for (ParameterSpec param : methodParams) {
            fields.add(FieldSpec.builder(
                    param.type,
                    param.name,
                    Modifier.FINAL,
                    Modifier.PUBLIC)
                    .addAnnotations(param.annotations)
                    .build());
        }

        return fields;
    }

    private MethodSpec createExecuteMethod(ExecutableElement method, Iterable<ParameterSpec> methodParams) {
        return MethodSpec.methodBuilder(EXECUTE_METHOD_NAME)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addParameter(viewInterface, VIEW_PARAM)
                .addStatement("$N.$N($N)",
                        VIEW_PARAM,
                        method.getSimpleName(),
                        getMethodParamsForCaller(methodParams))
                .build();
    }

    private StringBuilder getMethodParamsForCaller(Iterable<ParameterSpec> methodParams) {
        StringBuilder paramsStr = new StringBuilder();

        for (Iterator<ParameterSpec> iterator = methodParams.iterator(); iterator.hasNext(); ) {

            paramsStr.append(iterator.next().name).append(iterator.hasNext() ? "," : "");
        }
        return paramsStr;
    }

    private MethodSpec createCommandConstructor(ExecutableElement method,
                                                Iterable<ParameterSpec> methodParams,
                                                Class<? extends StateStrategy> strategy) {
        MethodSpec.Builder builder = MethodSpec.constructorBuilder()
                .varargs(method.isVarArgs())
                .addModifiers(Modifier.PUBLIC)
                .addStatement("super(new $T())", strategy)
                .addParameters(methodParams);

        for (ParameterSpec param : methodParams) {
            builder.addStatement("this.$N = $N", param.name, param.name);
        }
        return builder.build();
    }

    private Iterable<MethodSpec> createViewMethods() {
        List<MethodSpec> methods = new ArrayList<>();

        for (Element elem : viewElement.getEnclosedElements()) {
            if (elem.getKind() == ElementKind.METHOD) {
                ExecutableElement method = (ExecutableElement) elem;

                if (method.getReturnType().getKind() == TypeKind.VOID) {
                    methods.add(createViewMethod(method));
                } else {
                    MvpProcessor.error(method, "In canonical MVP all view methods should be 'void': ",
                            method.getSimpleName().toString());
                }
            }
        }
        return methods;
    }

    private MethodSpec createViewMethod(ExecutableElement method) {
        Iterable<ParameterSpec> methodParams = getMethodParams(method);

        String commandClassName = getCapitalizedName(method) + COMMAND_SUFFIX;

        CodeBlock code = CodeBlock.builder()
                .addStatement("$N(new " + commandClassName + "($N))",
                        APPLY_COMMAND_METHOD,
                        getMethodParamsForCaller(methodParams).toString())
                .build();


        return MethodSpec.methodBuilder(method.getSimpleName().toString())
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addExceptions(getThrownTypes(method))
                .varargs(method.isVarArgs())
                .addParameters(methodParams)
                .addTypeVariables(getTypeVariables(method))
                .returns(ClassName.get(method.getReturnType()))
                .addCode(code)
                .build();
    }

    private List<TypeName> getThrownTypes(ExecutableElement method) {
        List<TypeName> exceptions = new ArrayList<>();

        for (TypeMirror thrownType : method.getThrownTypes()) {
            exceptions.add(TypeName.get(thrownType));
        }
        return exceptions;
    }

    private List<TypeVariableName> getTypeVariables(ExecutableElement method) {
        List<TypeVariableName> typeVariableNames = new ArrayList<>();

        for (TypeParameterElement typeParam : method.getTypeParameters()) {
            typeVariableNames.add(TypeVariableName.get(typeParam));
        }
        return typeVariableNames;
    }

    private Iterable<ParameterSpec> getMethodParams(ExecutableElement method) {
        List<ParameterSpec> params = new ArrayList<>();

        for (VariableElement parameter : method.getParameters()) {

            params.add(ParameterSpec.builder(
                    TypeName.get(parameter.asType()),
                    parameter.getSimpleName().toString())
                    .addAnnotations(getMethodParamAnnotations(parameter))
                    .build());
        }
        return params;
    }

    private List<AnnotationSpec> getMethodParamAnnotations(VariableElement parameter) {
        List<AnnotationSpec> annotations = new ArrayList<>();

        for (AnnotationMirror annotationMirror : parameter.getAnnotationMirrors()) {
            annotations.add(AnnotationSpec.get(annotationMirror));
        }
        return annotations;
    }

    @SuppressWarnings("unchecked")
    private Class<? extends StateStrategy> getElemStrategyOrDefault(
            Element element,
            Class<? extends StateStrategy> fallbackStrategy) {

        StateStrategyType annotation = element.getAnnotation(StateStrategyType.class);
        try {
            return annotation == null ? fallbackStrategy : annotation.value();
        } catch (MirroredTypeException mte) {
            try {
                return (Class<? extends StateStrategy>) Class.forName(mte.getTypeMirror().toString());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return fallbackStrategy;
        }
    }
}
