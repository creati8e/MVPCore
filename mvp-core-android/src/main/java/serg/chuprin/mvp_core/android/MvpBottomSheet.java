package serg.chuprin.mvp_core.android;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import serg.chuprin.mvp_core.ComponentHolder;
import serg.chuprin.mvp_core.MvpDelegate;
import serg.chuprin.mvp_core.MvpPresenter;
import serg.chuprin.mvp_core.view.MvpView;


@SuppressWarnings({"unchecked", "unused"})
public abstract class MvpBottomSheet<PRESENTER extends MvpPresenter> extends BottomSheetDialogFragment
        implements MvpView, ComponentHolder {

    private final CompositeSubscription compositeSubscription = new CompositeSubscription();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private MvpDelegate<PRESENTER> mvpDelegate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mvpDelegate = new MvpDelegate<>(this, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        mvpDelegate.attachView();
    }

    @Override
    public void onResume() {
        super.onResume();
        mvpDelegate.resume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mvpDelegate.saveState(outState);
    }

    @Override
    public void onStop() {
        super.onStop();
        mvpDelegate.stop(getActivity().isChangingConfigurations());
        compositeSubscription.clear();
//        compositeDisposable.clear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mvpDelegate = null;
    }

    protected MvpDelegate<PRESENTER> getMvpDelegate() {
        return mvpDelegate;
    }

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
