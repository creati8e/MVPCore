package serg.chuprin.mvp_core.android;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import serg.chuprin.mvp_core.ComponentHolder;
import serg.chuprin.mvp_core.MvpPresenter;
import serg.chuprin.mvp_core.PresenterHelper;
import serg.chuprin.mvp_core.view.MvpView;


public abstract class MvpDialogFragment<PRESENTER extends MvpPresenter> extends DialogFragment
        implements MvpView, ComponentHolder {

    private final CompositeSubscription compositeSubscription = new CompositeSubscription();
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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        helper = null;
    }

    protected void addSubscription(Subscription subscription) {
        compositeSubscription.add(subscription);
    }
}
