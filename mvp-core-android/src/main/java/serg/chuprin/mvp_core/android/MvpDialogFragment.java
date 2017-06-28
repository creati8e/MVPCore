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

    private PresenterHelper<PRESENTER> helper;
    private CompositeSubscription compositeSubscription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        compositeSubscription = new CompositeSubscription();
        helper = new PresenterHelper<>(this, savedInstanceState);
        helper.attachView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        helper.saveState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        helper.stop(getActivity().isChangingConfigurations());
        helper = null;
    }

    @Override
    public void onStop() {
        super.onStop();
        compositeSubscription.clear();
    }

    @Override
    public void onResume() {
        super.onResume();
        helper.resume();
    }

    protected void addSubscription(Subscription subscription) {
        compositeSubscription.add(subscription);
    }
}
