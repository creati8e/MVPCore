package serg.chuprin.mvp_core.processor;

import java.util.List;
import java.util.Map;

import javax.lang.model.type.TypeMirror;

class InterfaceMethods {

    private final TypeMirror viewInterface;
    private final List<Method> methods;

    /**
     * contains generic params and their arguments. Example S -> List<Boolean>
     */
    private final Map<String, String> genericTypesMap;

    InterfaceMethods(TypeMirror viewInterface,
                     List<Method> methods,
                     Map<String, String> genericTypesMap) {
        this.viewInterface = viewInterface;
        this.methods = methods;
        this.genericTypesMap = genericTypesMap;
    }

    Map<String, String> getTypesMap() {
        return genericTypesMap;
    }

    List<Method> getMethods() {
        return methods;
    }

    TypeMirror getViewInterface() {
        return viewInterface;
    }
}
