package serg.chuprin.mvp_core.processor;

import javax.lang.model.element.ExecutableElement;

public class Method {

    private final ExecutableElement executableElement;
    private String uniqueName;

    public Method(ExecutableElement executableElement, String uniqueName) {
        this.executableElement = executableElement;
        this.uniqueName = uniqueName;
    }

    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }

    public String getUniqueName() {
        return uniqueName;
    }

    public ExecutableElement getExecutableElement() {
        return executableElement;
    }
}
