package serg.chuprin.mvp_core;

import android.os.Bundle;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import serg.chuprin.mvp_core.cache.ComponentCache;
import serg.chuprin.mvp_core.view.MvpView;

@SuppressWarnings("unchecked")
public class PresenterHelper<PRESENTER extends MvpPresenter> {
    private final ComponentCache componentCache = ComponentCache.getInstance();
    private final MvpView view;
    private final Object component;
    private boolean isRecreating;
    private PRESENTER presenter;
    private ComponentHolder holder;

    public <V extends MvpView & ComponentHolder> PresenterHelper(V viewHolder, Bundle bundle) {
        view = viewHolder;
        holder = viewHolder;
        component = findCachedComponent(bundle);
        inject();
        setPresenter(view);
    }

    public void stop(boolean retainComponent) {
        presenter.detachView();
        if (!isRecreating && !retainComponent) {
            componentCache.delete(component);
        }
    }

    public void saveState(Bundle bundle) {
        isRecreating = true;
        if (bundle != null) {
            componentCache.save(bundle, component);
        }
    }

    public void attachView() {
        presenter.attachView(view);
    }

    public PRESENTER getPresenter() {
        return presenter;
    }

    private void setPresenter(MvpView view) {
        for (Field field : view.getClass().getDeclaredFields()) {
            if (MvpUtils.isClassSubType(field.getType().getSuperclass(), MvpPresenter.class)) {
                try {
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                    presenter = (PRESENTER) field.get(view);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //region internal

    public void resume() {
        isRecreating = false;
    }

    private void inject() {
        try {
            Class viewClass = view.getClass();
            Class componentClass = component.getClass();
            Method[] methods = componentClass.getMethods();
            for (Method method : methods) {
                Class types[] = method.getParameterTypes();
                if (method.getName().startsWith("inject") &&
                        types != null && types.length == 1 &&
                        types[0].isAssignableFrom(viewClass)) {
                    method.invoke(component, view);
                    return; // all ok
                }
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private Object findCachedComponent(Bundle bundle) {
        Object component = null;
        if (bundle != null) {
            component = componentCache.get(bundle);
        }
        if (bundle == null || component == null) {
            component = holder.createComponent();
        }
        try {
            return holder.componentClass().cast(component);
        } catch (ClassCastException e) {
            return holder.createComponent();
        }
    }

    //endregion
}


