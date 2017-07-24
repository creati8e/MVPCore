package serg.chuprin.mvp_core.android;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import serg.chuprin.mvp_core.ComponentHolder;
import serg.chuprin.mvp_core.MvpPresenter;
import serg.chuprin.mvp_core.PresenterHelper;
import serg.chuprin.mvp_core.view.MvpView;


@SuppressWarnings({"unchecked", "unused"})
public abstract class MvpDialogFragment<PRESENTER extends MvpPresenter> extends DialogFragment
        implements MvpView, ComponentHolder {

    private final CompositeSubscription compositeSubscription = new CompositeSubscription();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private PresenterHelper<PRESENTER> helper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper = new PresenterHelper<>(this, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        helper.attachView();
    }

    @Override
    public void onResume() {
        super.onResume();
        helper.resume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        helper.saveState(outState);
    }

    @Override
    public void onStop() {
        super.onStop();
        helper.stop(getActivity().isChangingConfigurations());
        compositeSubscription.clear();
        compositeDisposable.clear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        helper = null;
    }

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
