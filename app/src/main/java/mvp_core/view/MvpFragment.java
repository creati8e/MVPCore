package mvp_core.view;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mvp_core.ComponentHolder;
import mvp_core.MvpPresenter;
import mvp_core.PresenterHelper;


public abstract class MvpFragment<PRESENTER extends MvpPresenter> extends Fragment
        implements MvpView, ComponentHolder {

    private PresenterHelper<PRESENTER> helper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutRes(), container, false);
    }

    @LayoutRes
    protected abstract int getLayoutRes();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper = new PresenterHelper<>(this, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedState) {
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
