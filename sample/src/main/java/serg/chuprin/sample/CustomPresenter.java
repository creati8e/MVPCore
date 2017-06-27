package serg.chuprin.sample;

import serg.chuprin.mvp_core.MvpPresenter;
import serg.chuprin.mvp_core.annotations.InjectViewState;

@InjectViewState
public abstract class CustomPresenter extends MvpPresenter<CustomView> {
    CustomPresenter() {
        super();
    }
}
