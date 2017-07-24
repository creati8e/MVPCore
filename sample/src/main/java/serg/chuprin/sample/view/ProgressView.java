package serg.chuprin.sample.view;

import serg.chuprin.mvp_core.view.MvpView;


public interface ProgressView<Y> extends MvpView {

    void showProgress(Y progress);
}
