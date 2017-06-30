package serg.chuprin.sample;

import serg.chuprin.mvp_core.MvpPresenter;
import serg.chuprin.mvp_core.view.MvpView;

public abstract class BasePresenter<V extends MvpView> extends MvpPresenter<V> {
}
