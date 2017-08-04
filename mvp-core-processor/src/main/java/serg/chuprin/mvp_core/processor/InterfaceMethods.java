package serg.chuprin.mvp_core.processor;

import java.util.List;
import java.util.Map;

import javax.lang.model.element.ExecutableElement;

class InterfaceMethods {

    private final List<ExecutableElement> methods;

    /**
     * contains generic params and their arguments. Example S -> List<Boolean>
     */
    private final Map<String, String> genericTypesMap;

    InterfaceMethods(List<ExecutableElement> methods, Map<String, String> genericTypesMap) {
        this.methods = methods;
        this.genericTypesMap = genericTypesMap;
    }

    Map<String, String> getTypesMap() {
        return genericTypesMap;
    }

    List<ExecutableElement> getMethods() {
        return methods;
    }
}
