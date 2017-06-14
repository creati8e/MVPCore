package mvp_core.view;

import android.os.Bundle;
import android.support.annotation.Nullable;

import org.polaric.colorful.ColorfulActivity;

import mvp_core.ComponentHolder;
import mvp_core.MvpPresenter;
import mvp_core.PresenterHelper;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;


public abstract class MvpThemedActivity<PRESENTER extends MvpPresenter>
        extends ColorfulActivity
        implements MvpView, ComponentHolder {

    private PresenterHelper<PRESENTER> helper;
    private CompositeSubscription mCompositeSubscription;

    @Override
    @SuppressWarnings("unchecked")
    protected void onCreate(@Nullable Bundle state) {
        super.onCreate(state);
        setContentView(getLayoutRes());
        mCompositeSubscription = new CompositeSubscription();
        helper = new PresenterHelper<>(this, state);
        helper.attachView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        helper.resume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unsubscribeAll();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        helper.stop(isChangingConfigurations());
        helper = null;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        helper.saveState(outState);
    }

    protected abstract int getLayoutRes();

    protected void addSubscription(Subscription subscription) {
        mCompositeSubscription.add(subscription);
    }

    protected void unsubscribeAll() {
        mCompositeSubscription.clear();
    }

    protected PRESENTER getPresenter() {
        return helper.getPresenter();
    }

}
