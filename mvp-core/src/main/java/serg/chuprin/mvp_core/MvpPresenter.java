package serg.chuprin.mvp_core;

import android.support.annotation.CallSuper;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import serg.chuprin.mvp_core.view.MvpView;
import serg.chuprin.mvp_core.view.nullView.NullObjectView;
import serg.chuprin.mvp_core.viewstate.MvpViewState;

@SuppressWarnings({"unchecked", "WeakerAccess", "unused"})
public abstract class MvpPresenter<VIEW extends MvpView> {
    private final MvpViewState<VIEW> viewState;
    private final CompositeSubscription viewSubscription = new CompositeSubscription();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private VIEW viewStateAsView;
    private NullObjectView<VIEW> nullObjectView;
    private boolean viewAttached;
    private boolean isFirstAttach = true;

    @CallSuper
    protected MvpPresenter() {
        viewState = (MvpViewState<VIEW>) MvpViewStateProvider.getView(this.getClass());
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
        unsubscribeAll();
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

    protected final void unsubscribeAll() {
        viewSubscription.clear();
        compositeDisposable.clear();
    }

    protected final void subscribeView(Subscription subscription) {
        viewSubscription.add(subscription);
    }

    protected final void subscribeView(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    protected final void removeSubscription(Disposable disposable) {
        compositeDisposable.delete(disposable);
    }

    protected final void removeSubscription(Subscription subscription) {
        viewSubscription.remove(subscription);
    }

    protected boolean isFirstAttach() {
        return isFirstAttach;
    }

    protected void onViewDetached() {

    }

    protected void onViewDestroyed() {

    }
}
