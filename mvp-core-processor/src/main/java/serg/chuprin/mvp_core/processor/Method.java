package serg.chuprin.mvp_core.processor;

import javax.lang.model.element.ExecutableElement;

class Method {

    private final ExecutableElement executableElement;
    private String uniqueName;

    Method(ExecutableElement executableElement, String uniqueName) {
        this.executableElement = executableElement;
        this.uniqueName = uniqueName;
    }

    void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }

    String getUniqueName() {
        return uniqueName;
    }

    ExecutableElement getExecutableElement() {
        return executableElement;
    }
}
