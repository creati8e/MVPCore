package serg.chuprin.mvp_core.viewstate;

import serg.chuprin.mvp_core.view.MvpView;

public abstract class ViewCommand<V extends MvpView> {

    abstract void execute(V view);
}
