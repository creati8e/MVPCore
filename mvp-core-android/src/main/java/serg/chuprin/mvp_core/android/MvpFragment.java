package serg.chuprin.mvp_core.android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import serg.chuprin.mvp_core.ComponentHolder;
import serg.chuprin.mvp_core.MvpDelegate;
import serg.chuprin.mvp_core.MvpPresenter;
import serg.chuprin.mvp_core.view.MvpView;


@SuppressWarnings({"unchecked", "unused"})
public abstract class MvpFragment<PRESENTER extends MvpPresenter> extends Fragment
        implements MvpView, ComponentHolder {

    private MvpDelegate<PRESENTER> mvpDelegate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mvpDelegate = new MvpDelegate<>(this, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(getLayoutRes(), container, false);
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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mvpDelegate = null;
    }

    protected MvpDelegate<PRESENTER> getMvpDelegate() {
        return mvpDelegate;
    }

    protected abstract int getLayoutRes();

    protected final PRESENTER getPresenter() {
        return mvpDelegate.getPresenter();
    }
}
