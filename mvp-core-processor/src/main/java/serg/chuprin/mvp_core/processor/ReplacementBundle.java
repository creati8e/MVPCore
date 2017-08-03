package serg.chuprin.mvp_core.processor;

class ReplacementBundle {

    private final int startIndex;
    private final String genericType;
    private final String genericTypeArg;

    ReplacementBundle(int startIndex, String genericType, String genericTypeArg) {
        this.startIndex = startIndex;
        this.genericType = genericType;
        this.genericTypeArg = genericTypeArg;
    }

    int getStartIndex() {
        return startIndex;
    }

    String getGenericType() {
        return genericType;
    }

    String getGenericTypeArg() {
        return genericTypeArg;
    }
}
