package mvp_core;

import mvp_core.view.MvpView;
import mvp_core.view.NullObjectView;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public abstract class MvpPresenter<VIEW extends MvpView> {
    private CompositeSubscription viewSubscription = new CompositeSubscription();
    private NullObjectView<VIEW> view = new NullObjectView<>();
    private boolean viewAttached;

    final void attachView(VIEW view) {
        if (!viewAttached) {
            this.view.setView(view);
            viewAttached = true;
            onViewAttached();
        }
    }

    final void detachView() {
        view.removeView();
        viewAttached = false;
        unsubscribeAll();
    }

    final protected VIEW getView() {
        return view.get();
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
