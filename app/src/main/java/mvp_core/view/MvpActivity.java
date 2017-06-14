package mvp_core.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import mvp_core.ComponentHolder;
import mvp_core.MvpPresenter;
import mvp_core.PresenterHelper;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;


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
    protected void onCreate(@Nullable Bundle state) {
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
