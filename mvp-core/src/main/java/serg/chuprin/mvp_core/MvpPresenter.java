package serg.chuprin.mvp_core;

import android.support.annotation.CallSuper;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import serg.chuprin.mvp_core.view.MvpView;
import serg.chuprin.mvp_core.viewstate.MvpViewState;

@SuppressWarnings("unchecked")
public abstract class MvpPresenter<VIEW extends MvpView> {
    private final MvpViewState<VIEW> viewState;
    private VIEW viewStateAsView;
    private CompositeSubscription viewSubscription = new CompositeSubscription();
    private boolean viewAttached;

    @CallSuper
    protected MvpPresenter() {
        viewState = (MvpViewState<VIEW>) MvpViewStateProvider.getViewState(this.getClass());
        viewStateAsView = (VIEW) viewState;
    }

    final void attachView(VIEW view) {
        if (!viewAttached) {
            viewState.attachView(view);
            viewStateAsView = (VIEW) viewState;
            viewAttached = true;
            onViewAttached();
        }
    }

    final void detachView() {
        viewState.detachView();
        viewAttached = false;
        unsubscribeAll();
    }

    final protected VIEW getViewState() {
        return viewStateAsView;
    }

    protected void onViewAttached() {

    }

    final protected void unsubscribeAll() {
        viewSubscription.clear();
    }

    final protected void subscribeView(Subscription subscription) {
        viewSubscription.add(subscription);
    }
}
