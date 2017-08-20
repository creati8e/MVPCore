package serg.chuprin.mvp_core;

import android.support.annotation.CallSuper;

import serg.chuprin.mvp_core.view.MvpView;
import serg.chuprin.mvp_core.view.nullView.NullObjectView;
import serg.chuprin.mvp_core.viewstate.MvpViewState;

@SuppressWarnings({"unchecked", "WeakerAccess", "unused", "EmptyMethod"})
public abstract class MvpPresenter<VIEW extends MvpView> {
    private final MvpViewState<VIEW> viewState;
    private VIEW viewStateAsView;
    private NullObjectView<VIEW> nullObjectView;
    private boolean viewAttached;
    private boolean isFirstAttach = true;

    @CallSuper
    protected MvpPresenter() {
        viewState = (MvpViewState<VIEW>) MvpViewStateBinder.getView(this.getClass());
        viewStateAsView = (VIEW) viewState;

        if (viewState == null) {
            nullObjectView = new NullObjectView<>();
        }
    }

    final void attachView(VIEW view) {
        if (viewAttached) {
            return;
        }
        if (viewState != null) {
            viewState.attachView(view);
            viewStateAsView = (VIEW) viewState;
        } else {
            nullObjectView.setView(view);
        }

        viewAttached = true;
        onViewAttached();
        isFirstAttach = false;
    }

    final void detachView() {
        if (!viewAttached) {
            return;
        }
        if (viewState != null) {
            viewState.detachView();
        } else {
            nullObjectView.removeView();
        }
        viewAttached = false;
        onViewDetached();
    }

    final void destroyView() {
        if (viewState != null) {
            viewState.destroyView();
            onViewDestroyed();
        }
    }

    protected final VIEW getView() {
        return viewStateAsView != null ? viewStateAsView : nullObjectView.get();
    }

    protected void onViewAttached() {

    }

    protected boolean isFirstAttach() {
        return isFirstAttach;
    }

    protected void onViewDetached() {

    }

    protected void onViewDestroyed() {

    }
}
