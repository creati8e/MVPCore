package serg.chuprin.mvp_core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import serg.chuprin.mvp_core.view.MvpView;
import serg.chuprin.mvp_core.viewstate.DefaultViewState;
import serg.chuprin.mvp_core.viewstate.ViewState;

@Target(value = ElementType.TYPE)
public @interface InjectViewState {

    Class<? extends ViewState> value() default DefaultViewState.class;

    Class<? extends MvpView> view() default MvpView.class;
}
