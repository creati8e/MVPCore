package serg.chuprin.processor;

public class Pair<V, T> {
    private final V first;
    private final T second;

    public Pair(V first, T second) {
        this.first = first;
        this.second = second;
    }

    public T getSecond() {
        return second;
    }

    public V getFirst() {
        return first;
    }
}
