package serg.chuprin.mvp_core.processor;

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
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.Filer;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import serg.chuprin.mvp_core.annotations.StateStrategyType;
import serg.chuprin.mvp_core.viewstate.MvpViewState;
import serg.chuprin.mvp_core.viewstate.ViewCommand;
import serg.chuprin.mvp_core.viewstate.strategy.AddToEndSingleOneExecutionStrategy;
import serg.chuprin.mvp_core.viewstate.strategy.StateStrategy;

import static java.lang.Character.isAlphabetic;

class ViewStateGenerator {

    private static final String VIEW_STATE_SUFFIX = "$$ViewState";
    private static final String COMMAND_SUFFIX = "Command";
    private static final String EXECUTE_METHOD_NAME = "execute";
    private static final String EXECUTE_COMMAND_METHOD = "executeCommand";
    private static final String VIEW_PARAM = "view";
    private final Filer filer;
    private final Types typeUtils;
    private final Elements elemUtils;
    private final TypeElement viewElement;
    private final ClassName viewInterface;
    private final ClassName viewCommandName;
    private final Class<? extends StateStrategy> defaultStrategy;

    ViewStateGenerator(TypeElement viewElem, Filer filer, Types typeUtils, Elements elemUtils) {
        this.filer = filer;
        this.typeUtils = typeUtils;
        this.elemUtils = elemUtils;

        viewElement = viewElem;
        viewInterface = ClassName.get(viewElement);
        viewCommandName = ClassName.get(ViewCommand.class);
        defaultStrategy = AddToEndSingleOneExecutionStrategy.class;
    }

    boolean generate() {
        String stateName = String.format("%s%s", viewElement.getSimpleName(), VIEW_STATE_SUFFIX);
        List<InterfaceMethods> allMethods = getAllMethods();
        List<TypeVariableName> typeVariables = getInterfaceTypeVariables(viewElement);

        TypeSpec stateClass = TypeSpec.classBuilder(stateName)
                .superclass(ParameterizedTypeName.get(ClassName.get(MvpViewState.class), viewInterface))
                .addSuperinterface(getGenericSuperinterface(typeVariables))
                .addTypeVariables(typeVariables)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addTypes(createInnerCommandClasses(allMethods))
                .addMethods(createViewMethods(allMethods))
                .build();

        String packageName = viewElement.getEnclosingElement().getSimpleName().toString();

        try {
            JavaFile.builder(packageName, stateClass).build().writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    String getClassName() {
        String stateName = String.format("%s%s", viewElement.getSimpleName(), VIEW_STATE_SUFFIX);
        String packageName = viewElement.getEnclosingElement().getSimpleName().toString();
        return String.format("%s.%s", packageName, stateName);
    }

    private TypeName getGenericSuperinterface(List<TypeVariableName> typeVariables) {
        if (typeVariables.isEmpty()) {
            return viewInterface;
        }
        TypeName[] typeNames = new TypeName[typeVariables.size()];

        for (int i = 0; i < typeVariables.size(); i++) {
            typeNames[i] = typeVariables.get(i).withoutAnnotations();
        }
        return ParameterizedTypeName.get(viewInterface, typeNames);
    }

    private Iterable<TypeSpec> createInnerCommandClasses(List<InterfaceMethods> allMethods) {
        Class<? extends StateStrategy> viewStrategy = getElemStrategyOrDefault(viewElement, defaultStrategy);

        List<TypeSpec> classes = new ArrayList<>();

        for (InterfaceMethods interfaceMethods : allMethods) {

            for (ExecutableElement method : interfaceMethods.getMethods()) {
                classes.add(createCommandClass(method,
                        getElemStrategyOrDefault(method, viewStrategy),
                        interfaceMethods.getTypesMap()));
            }
        }
        return classes;
    }

    private TypeSpec createCommandClass(ExecutableElement method,
                                        Class<? extends StateStrategy> strategy,
                                        Map<String, String> typesMap) {

        Iterable<ParameterSpec> methodParams = getMethodParams(method, typesMap);

        return TypeSpec.classBuilder(getCapitalizedName(method) + COMMAND_SUFFIX)
                .superclass(ParameterizedTypeName.get(viewCommandName, viewInterface))
                .addTypeVariables(getMethodTypeVariables(method, typesMap))
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

    private Iterable<MethodSpec> createViewMethods(List<InterfaceMethods> allMethods) {
        List<MethodSpec> methods = new ArrayList<>();

        for (InterfaceMethods interfaceMethods : allMethods) {

            for (ExecutableElement method : interfaceMethods.getMethods()) {
                methods.add(createViewMethod(method, interfaceMethods.getTypesMap()));
            }
        }
        return methods;
    }

    private MethodSpec createViewMethod(ExecutableElement method, Map<String, String> typesMap) {
        Iterable<ParameterSpec> methodParams = getMethodParams(method, typesMap);
        String commandClassName = getCapitalizedName(method) + COMMAND_SUFFIX;

        CodeBlock code = CodeBlock.builder()
                .addStatement("$N(new " + commandClassName + "($N))",
                        EXECUTE_COMMAND_METHOD,
                        getMethodParamsForCaller(methodParams).toString())
                .build();

        return MethodSpec.methodBuilder(method.getSimpleName().toString())
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addExceptions(getThrownTypes(method))
                .varargs(method.isVarArgs())
                .addParameters(methodParams)
                .addTypeVariables(getMethodTypeVariables(method, typesMap))
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

    private List<TypeVariableName> getMethodTypeVariables(ExecutableElement method, Map<String, String> typesMap) {
        List<TypeVariableName> typeVariableNames = new ArrayList<>();

        for (TypeParameterElement typeParam : method.getTypeParameters()) {

            List<? extends TypeMirror> typeBounds = typeParam.getBounds();

            TypeName[] resolvedBounds = new TypeName[typeBounds.size()];

            for (int i = 0; i < typeBounds.size(); i++) {
                resolvedBounds[i] = inferTypeName(typeBounds.get(i), typesMap);
            }
            typeVariableNames.add(TypeVariableName.get(typeParam.toString(), resolvedBounds));
        }
        return typeVariableNames;
    }

    private List<TypeVariableName> getInterfaceTypeVariables(TypeElement element) {
        List<TypeVariableName> typeVariableNames = new ArrayList<>();

        for (TypeParameterElement typeParam : element.getTypeParameters()) {
            typeVariableNames.add(TypeVariableName.get(typeParam));
        }
        return typeVariableNames;
    }

    private Iterable<ParameterSpec> getMethodParams(ExecutableElement method, Map<String, String> typesMap) {
        List<ParameterSpec> params = new ArrayList<>();

        for (VariableElement parameter : method.getParameters()) {
            TypeName inferTypeName = inferTypeName(parameter.asType(), typesMap);
            params.add(ParameterSpec.builder(
                    inferTypeName,
                    parameter.getSimpleName().toString())
                    .addAnnotations(getMethodParamAnnotations(parameter))
                    .build());
        }
        return params;
    }

    private TypeName inferTypeName(TypeMirror mirror, Map<String, String> typesMap) {

        String typeStr = mirror.toString();

        List<Pair<Integer, String>> indexes = new ArrayList<>();

        // entry example: (key) V -> (value) List<Long>
        for (Map.Entry<String, String> entry : typesMap.entrySet()) {

            if (typeStr.equals(entry.getKey())) {
                return TypeName.get(entry.getValue());
            }

            int replacementIndex = -1;
            char genericArg = entry.getKey().charAt(0);

            for (int i = 1; i < typeStr.length() - 1; ++i) {
                if (typeStr.charAt(i) == genericArg
                        && !isAlphabetic(typeStr.charAt(i - 1))
                        && !isAlphabetic(typeStr.charAt(i + 1))) {

                    replacementIndex = i;
                    break;
                }
            }
            if (replacementIndex > -1) {
                indexes.add(new Pair<>(replacementIndex, entry.getValue()));
            }
        }

        if (!indexes.isEmpty()) {
            StringBuilder builder = new StringBuilder(typeStr);

            int offset = 0;
            for (Pair<Integer, String> index : indexes) {
                int actualIndex = offset + index.getFirst();
                builder.replace(actualIndex, actualIndex + 1, index.getSecond());
                offset += index.getSecond().length() - 1;
            }

            return TypeName.get(builder.toString());
        }
        return TypeName.get(mirror);
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
        try {
            StateStrategyType annotation = element.getAnnotation(StateStrategyType.class);
            if (annotation != null) {
                annotation.value();
            }
        } catch (MirroredTypeException mte) {
            try {
                return (Class<? extends StateStrategy>) Class.forName(mte.getTypeMirror().toString());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return fallbackStrategy;
    }

    /**
     * non-recursive getting methods from all super interfaces
     */
    private List<InterfaceMethods> getAllMethods() {
        Map<TypeMirror, InterfaceMethods> methodsMap = new HashMap<>();

        Deque<TypeMirror> elements = new ArrayDeque<>(Collections.singletonList(viewElement.asType()));

        TypeMirror previousInterface = null;

        while (!elements.isEmpty()) {

            TypeMirror anInterface = elements.poll();
            TypeElement interfaceType = (TypeElement) typeUtils.asElement(anInterface);

            methodsMap.put(anInterface, new InterfaceMethods(
                    getInterfaceMethods(interfaceType),
                    createInterfaceTypeArgsMapping(methodsMap, anInterface, previousInterface)));

            for (TypeMirror interfaceMirror : interfaceType.getInterfaces()) {
                elements.addFirst(interfaceMirror);
            }
            previousInterface = anInterface;
        }
        List<InterfaceMethods> methods = new ArrayList<>(methodsMap.size());
        for (Map.Entry<TypeMirror, InterfaceMethods> entry : methodsMap.entrySet()) {
            methods.add(entry.getValue());
        }
        return methods;
    }

    private Map<String, String> createInterfaceTypeArgsMapping(Map<TypeMirror, InterfaceMethods> methodsMap,
                                                               TypeMirror curInterface,
                                                               TypeMirror prevInterface) {

        InterfaceMethods interfaceMethods = methodsMap.get(prevInterface);
        if (interfaceMethods == null) {
            return Collections.emptyMap();
        }
        return replaceWithParentMapping(getInterfaceTypeMapping(curInterface), interfaceMethods.getTypesMap());
    }

    /**
     * @return Map<String,String> contains generic types mapping to arguments. I.e S -> String
     */
    private Map<String, String> getInterfaceTypeMapping(TypeMirror interfaceMirror) {
        DeclaredType declaredType = (DeclaredType) interfaceMirror;

        List<? extends TypeMirror> typeArguments = declaredType.getTypeArguments();
        if (typeArguments.isEmpty()) {
            return Collections.emptyMap();
        }

        TypeElement element = (TypeElement) declaredType.asElement();
        List<? extends TypeParameterElement> typeParameters = element.getTypeParameters();

        Map<String, String> typesMapping = new HashMap<>(typeArguments.size());
        for (int i = 0; i < typeArguments.size(); i++) {
            typesMapping.put(typeParameters.get(i).toString(), typeArguments.get(i).toString());
        }
        return typesMapping;
    }

    /**
     * @param currentMapping current interface mapping.
     *                       Contains generic generic types and args. I.e S -> P
     * @param parentMapping  Parent interface mapping.
     *                       Contains resolved args. I.e P -> String
     * @return current mapping without unresolved args.
     * So they are replaced to resolved args from parent mapping
     */
    private Map<String, String> replaceWithParentMapping(Map<String, String> currentMapping,
                                                         Map<String, String> parentMapping) {


        for (Map.Entry<String, String> entry : currentMapping.entrySet()) {
            String arg = parentMapping.get(entry.getValue());
            if (arg != null) {
                currentMapping.put(entry.getKey(), arg);
            } else {
                String genericMapping = entry.getValue();

                List<Pair<Integer, String>> indexes = new ArrayList<>();
                for (Map.Entry<String, String> parentArg : parentMapping.entrySet()) {

                    int replacementIndex = -1;
                    char genericArg = parentArg.getKey().charAt(0);

                    for (int i = 1; i < genericMapping.length() - 1; ++i) {
                        if (genericMapping.charAt(i) == genericArg
                                && !isAlphabetic(genericMapping.charAt(i - 1))
                                && !isAlphabetic(genericMapping.charAt(i + 1))) {

                            replacementIndex = i;
                            break;
                        }
                    }
                    if (replacementIndex > -1) {
                        indexes.add(new Pair<>(replacementIndex, parentArg.getValue()));
                    }
                }
                if (!indexes.isEmpty()) {
                    StringBuilder builder = new StringBuilder(genericMapping);

                    for (Pair<Integer, String> index : indexes) {
                        builder.replace(index.getFirst(), index.getFirst() + 1, index.getSecond());
                    }
                    currentMapping.remove(entry.getKey());

                    String inferredGeneric = builder.toString();
                    currentMapping.put(genericMapping, inferredGeneric);
                    currentMapping.put(entry.getKey(), inferredGeneric);
                }
            }
        }
        return currentMapping;
    }

    private List<ExecutableElement> getInterfaceMethods(TypeElement anInterface) {
        List<ExecutableElement> methods = new ArrayList<>();

        for (Element element : anInterface.getEnclosedElements()) {

            if (element.getKind() == ElementKind.METHOD) {

                ExecutableElement method = (ExecutableElement) element;
                if (method.getReturnType().getKind() == TypeKind.VOID) {
                    methods.add(method);
                } else {
                    MvpProcessor.error(method, "In canonical MVP all view methods should be 'void': ",
                            method.getSimpleName().toString());
                }
            }
        }
        return methods;
    }
}
