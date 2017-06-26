package serg.chuprin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import serg.chuprin.mvp_core.ComponentHolder;
import serg.chuprin.mvp_core.MvpPresenter;
import serg.chuprin.mvp_core.PresenterHelper;
import serg.chuprin.mvp_core.view.MvpView;


public abstract class MvpFragment<PRESENTER extends MvpPresenter> extends Fragment
        implements MvpView, ComponentHolder {

    private PresenterHelper<PRESENTER> helper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(getLayoutRes(), container, false);
    }

    protected abstract int getLayoutRes();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper = new PresenterHelper<>(this, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedState) {
        super.onViewCreated(view, savedState);
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
    public void onResume() {
        super.onResume();
        helper.resume();
    }

    protected PRESENTER getPresenter() {
        return helper.getPresenter();
    }
}
