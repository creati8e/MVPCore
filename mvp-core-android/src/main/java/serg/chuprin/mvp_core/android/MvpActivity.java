package serg.chuprin.mvp_core.android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import serg.chuprin.mvp_core.ComponentHolder;
import serg.chuprin.mvp_core.MvpDelegate;
import serg.chuprin.mvp_core.MvpPresenter;
import serg.chuprin.mvp_core.view.MvpView;


@SuppressWarnings({"unchecked", "unused"})
public abstract class MvpActivity<PRESENTER extends MvpPresenter>
        extends AppCompatActivity
        implements MvpView, ComponentHolder {

    private final CompositeSubscription compositeSubscription = new CompositeSubscription();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private MvpDelegate<PRESENTER> mvpDelegate;

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(getLayoutRes());
        mvpDelegate = new MvpDelegate<>(this, state);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mvpDelegate.attachView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mvpDelegate.resume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mvpDelegate.saveState(outState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mvpDelegate.stop(isChangingConfigurations());
        compositeSubscription.clear();
        compositeDisposable.clear();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mvpDelegate = null;
    }

    protected MvpDelegate<PRESENTER> getMvpDelegate() {
        return mvpDelegate;
    }

    protected abstract int getLayoutRes();

    protected final void addSubscription(Subscription subscription) {
        compositeSubscription.add(subscription);
    }

    protected final void addSubscription(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    protected final PRESENTER getPresenter() {
        return mvpDelegate.getPresenter();
    }

}
