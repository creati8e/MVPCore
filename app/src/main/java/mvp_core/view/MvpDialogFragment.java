package mvp_core.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import mvp_core.ComponentHolder;
import mvp_core.MvpPresenter;
import mvp_core.PresenterHelper;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;


public abstract class MvpDialogFragment<PRESENTER extends MvpPresenter> extends DialogFragment
        implements MvpView, ComponentHolder {

    private PresenterHelper<PRESENTER> helper;
    private CompositeSubscription compositeSubscription;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
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
