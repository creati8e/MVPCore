package serg.chuprin.processor;

class Pair<V, T> {
    private final V first;
    private final T second;

    Pair(V first, T second) {
        this.first = first;
        this.second = second;
    }

    T getSecond() {
        return second;
    }

    V getFirst() {
        return first;
    }
}
