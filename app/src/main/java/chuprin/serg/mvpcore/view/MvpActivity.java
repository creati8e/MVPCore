package chuprin.serg.mvpcore.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import chuprin.serg.mvpcore.MvpPresenter;
import chuprin.serg.mvpcore.PresenterHelper;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;


public abstract class MvpActivity<PRESENTER extends MvpPresenter>
        extends AppCompatActivity
        implements MvpView {

    private PresenterHelper<PRESENTER> helper;
    private CompositeSubscription mCompositeSubscription;

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
        mCompositeSubscription = new CompositeSubscription();
        helper = new PresenterHelper<>(this, createComponent(state));
        helper.attachView();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mCompositeSubscription.clear();
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

    protected PRESENTER getPresenter() {
        return helper.getPresenter();
    }

    protected abstract Object createComponent(@Nullable Bundle state);
}
