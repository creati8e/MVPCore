package serg.chuprin.mvp_core.android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import serg.chuprin.mvp_core.ComponentHolder;
import serg.chuprin.mvp_core.MvpPresenter;
import serg.chuprin.mvp_core.PresenterHelper;
import serg.chuprin.mvp_core.view.MvpView;


@SuppressWarnings({"unchecked", "unused"})
public abstract class MvpActivity<PRESENTER extends MvpPresenter>
        extends AppCompatActivity
        implements MvpView, ComponentHolder {

    private final CompositeSubscription compositeSubscription = new CompositeSubscription();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private PresenterHelper<PRESENTER> helper;

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(getLayoutRes());
        helper = new PresenterHelper<>(this, state);
    }

    @Override
    protected void onStart() {
        super.onStart();
        helper.attachView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        helper.resume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        helper.saveState(outState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        helper.stop(isChangingConfigurations());
        compositeSubscription.clear();
        compositeDisposable.clear();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        helper = null;
    }

    protected abstract int getLayoutRes();

    protected final void addSubscription(Subscription subscription) {
        compositeSubscription.add(subscription);
    }

    protected final void addSubscription(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    protected final PRESENTER getPresenter() {
        return helper.getPresenter();
    }

}
