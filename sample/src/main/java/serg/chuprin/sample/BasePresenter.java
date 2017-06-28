package serg.chuprin.sample;

import serg.chuprin.mvp_core.MvpPresenter;
import serg.chuprin.mvp_core.annotations.InjectViewState;
import serg.chuprin.mvp_core.view.MvpView;
import serg.chuprin.sample.view.UserView;

@InjectViewState(view = UserView.class)
public abstract class BasePresenter<V extends MvpView> extends MvpPresenter<V> {
}
