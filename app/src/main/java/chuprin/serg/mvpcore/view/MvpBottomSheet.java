package chuprin.serg.mvpcore.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;

import chuprin.serg.mvpcore.MvpPresenter;
import chuprin.serg.mvpcore.PresenterHelper;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;


public abstract class MvpBottomSheet<PRESENTER extends MvpPresenter> extends BottomSheetDialogFragment
        implements MvpView {

    private PresenterHelper<PRESENTER> helper;
    private CompositeSubscription mCompositeSubscription;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCompositeSubscription = new CompositeSubscription();
        helper = new PresenterHelper<>(this, createComponent(savedInstanceState));
        helper.attachView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        helper.saveState(outState);
    }

    @Override
    public void onStop() {
        super.onStop();
        mCompositeSubscription.clear();
    }

    @Override
    public void onResume() {
        super.onResume();
        helper.resume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        helper.stop(getActivity().isChangingConfigurations());
        helper = null;
    }

    @SuppressWarnings("unused")
    protected void addSubscription(Subscription subscription) {
        mCompositeSubscription.add(subscription);
    }

    protected abstract Object createComponent(@Nullable Bundle savedState);
}
