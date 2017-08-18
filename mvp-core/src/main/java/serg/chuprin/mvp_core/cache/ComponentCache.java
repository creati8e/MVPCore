package serg.chuprin.mvp_core.cache;

import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;


public class ComponentCache {
    private static ComponentCache instance;
    private final String BUNDLE_KEY = "BUNDLE_KEY";
    private final AtomicInteger componentId = new AtomicInteger();
    /**
     * contains dagger2 components
     */
    @SuppressWarnings({"UseSparseArrays"})
    private final Map<Integer, Object> cache = new HashMap<>();

    public static ComponentCache getInstance() {
        if (instance == null) {
            instance = new ComponentCache();
        }
        return instance;
    }

    public final Object get(Bundle bundle) {
        int key = bundle.getInt(BUNDLE_KEY);
        Object component = cache.get(key);
        cache.remove(key);
        return component;
    }

    public final void save(Bundle bundle, Object component) {
        if (!cache.containsValue(component)) {
            bundle.putInt(BUNDLE_KEY, componentId.incrementAndGet());
            cache.put(componentId.get(), component);
        } else {
            Integer key = getKeyByValue(component);
            if (key != null) {
                bundle.putInt(BUNDLE_KEY, key);
            }
        }
    }

    public final void delete(Object component) {
        cache.values().remove(component);
    }

    private Integer getKeyByValue(Object value) {
        for (Map.Entry<Integer, Object> entry : cache.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }
}
