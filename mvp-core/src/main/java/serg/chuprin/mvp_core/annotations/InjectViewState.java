package serg.chuprin.mvp_core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import serg.chuprin.mvp_core.view.MvpView;

@Target(value = ElementType.TYPE)
public @interface InjectViewState {

    Class<? extends MvpView> view() default MvpView.class;
}
