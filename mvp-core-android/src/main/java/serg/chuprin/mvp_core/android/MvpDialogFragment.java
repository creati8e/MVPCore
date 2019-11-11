package serg.chuprin.mvp_core.android;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

import serg.chuprin.mvp_core.ComponentHolder;
import serg.chuprin.mvp_core.MvpDelegate;
import serg.chuprin.mvp_core.MvpPresenter;
import serg.chuprin.mvp_core.view.MvpView;


@SuppressWarnings({ "unused" })
public abstract class MvpDialogFragment<PRESENTER extends MvpPresenter> extends DialogFragment
        implements MvpView, ComponentHolder {

    private MvpDelegate<PRESENTER> mvpDelegate;
    private boolean isStateSaved;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mvpDelegate = new MvpDelegate<>(this, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        isStateSaved = false;
        mvpDelegate.attachView();
    }

    @Override
    public void onResume() {
        super.onResume();
        isStateSaved = false;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        isStateSaved = true;
        mvpDelegate.saveState(outState);
        mvpDelegate.detachView();
    }

    @Override
    public void onStop() {
        super.onStop();
        mvpDelegate.detachView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mvpDelegate.detachView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getActivity().isFinishing()) {
            mvpDelegate.destroy();
            mvpDelegate = null;
            return;
        }
        if (isStateSaved) {
            isStateSaved = false;
            return;
        }
        boolean anyParentIsRemoving = false;

        Fragment parent = getParentFragment();
        while (!anyParentIsRemoving && parent != null) {
            anyParentIsRemoving = parent.isRemoving();
            parent = parent.getParentFragment();
        }

        if (isRemoving() || anyParentIsRemoving) {
            mvpDelegate.destroy();
            mvpDelegate = null;
            return;
        }
        mvpDelegate.destroy();
    }

    protected MvpDelegate<PRESENTER> getMvpDelegate() {
        return mvpDelegate;
    }

    protected abstract int getLayoutRes();

    protected final PRESENTER getPresenter() {
        return mvpDelegate.getPresenter();
    }

}
