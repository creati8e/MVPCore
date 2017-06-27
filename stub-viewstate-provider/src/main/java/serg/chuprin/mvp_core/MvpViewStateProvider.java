package serg.chuprin.mvp_core;

import java.util.HashMap;
import java.util.Map;

public class MvpViewStateProvider {

    private static final Map<Class<?>, Object> viewStatesMap = new HashMap<>();

    public static Object getViewState(Class<?> presenter) {
        return viewStatesMap.get(presenter);
    }
}
