package mvp_core;

public interface ComponentHolder {
    Object createComponent();

    Class<?> componentClass();
}
