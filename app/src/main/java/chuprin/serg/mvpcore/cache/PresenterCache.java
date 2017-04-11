package chuprin.serg.mvpcore.cache;

import android.annotation.SuppressLint;
import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import chuprin.serg.mvpcore.MvpPresenter;
import chuprin.serg.mvpcore.view.MvpView;


public class PresenterCache {
    private static PresenterCache instance;
    private final String BUNDLE_KEY = "BUNDLE_KEY";
    private AtomicInteger presenterId = new AtomicInteger();
    @SuppressLint("UseSparseArrays")
    private Map<Integer, MvpPresenter<? extends MvpView>> cache = new HashMap<>();

    public static PresenterCache getInstance() {
        if (instance == null) {
            instance = new PresenterCache();
        }
        return instance;
    }

    public final MvpPresenter<? extends MvpView> get(Bundle bundle) {
        int key = bundle.getInt(BUNDLE_KEY);
        MvpPresenter<? extends MvpView> presenter = cache.get(key);
        cache.remove(key);
        return presenter;
    }

    public final void save(Bundle bundle, MvpPresenter<? extends MvpView> presenter) {
        if (!cache.containsValue(presenter)) {
            bundle.putInt(BUNDLE_KEY, presenterId.incrementAndGet());
            cache.put(presenterId.get(), presenter);
        } else {
            Integer key = getKeyByValue(presenter);
            if (key != null) {
                bundle.putInt(BUNDLE_KEY, key);
            }
        }
    }

    public final void delete(MvpPresenter<? extends MvpView> presenter) {
        cache.values().remove(presenter);
    }

    private Integer getKeyByValue(MvpPresenter<? extends MvpView> value) {
        for (Map.Entry<Integer, MvpPresenter<? extends MvpView>> entry : cache.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }
}
