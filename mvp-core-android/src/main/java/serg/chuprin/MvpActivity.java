package serg.chuprin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import serg.chuprin.mvp_core.ComponentHolder;
import serg.chuprin.mvp_core.MvpPresenter;
import serg.chuprin.mvp_core.PresenterHelper;
import serg.chuprin.mvp_core.view.MvpView;


public abstract class MvpActivity<PRESENTER extends MvpPresenter>
        extends AppCompatActivity
        implements MvpView, ComponentHolder {

    private PresenterHelper<PRESENTER> helper;
    private CompositeSubscription compositeSubscription;

    @Override
    protected void onStop() {
        super.onStop();
        compositeSubscription.clear();
    }

    @Override
    protected void onResume() {
        super.onResume();
        helper.resume();
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(getLayoutRes());
        compositeSubscription = new CompositeSubscription();
        helper = new PresenterHelper<>(this, state);
        helper.attachView();
    }

    protected abstract int getLayoutRes();

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

    protected void addSubscription(Subscription subscription) {
        compositeSubscription.add(subscription);
    }

    protected PRESENTER getPresenter() {
        return helper.getPresenter();
    }

}
