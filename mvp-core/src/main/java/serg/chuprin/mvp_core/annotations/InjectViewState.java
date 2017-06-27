package serg.chuprin.mvp_core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import serg.chuprin.mvp_core.view.MvpView;
import serg.chuprin.mvp_core.viewstate.DefaultMvpViewState;
import serg.chuprin.mvp_core.viewstate.MvpViewState;

@Target(value = ElementType.TYPE)
public @interface InjectViewState {

    Class<? extends MvpViewState> value() default DefaultMvpViewState.class;

    Class<? extends MvpView> view() default MvpView.class;
}
