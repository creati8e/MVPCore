package serg.chuprin.mvp_core.android;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

import serg.chuprin.mvp_core.ComponentHolder;
import serg.chuprin.mvp_core.MvpDelegate;
import serg.chuprin.mvp_core.MvpPresenter;
import serg.chuprin.mvp_core.view.MvpView;


@SuppressWarnings({"unchecked", "unused"})
public abstract class MvpDialogFragment<PRESENTER extends MvpPresenter> extends DialogFragment
        implements MvpView, ComponentHolder {

    private MvpDelegate<PRESENTER> mvpDelegate;
    private boolean mIsStateSaved;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mvpDelegate = new MvpDelegate<>(this, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        mIsStateSaved = false;
        mvpDelegate.attachView();
    }

    @Override
    public void onResume() {
        super.onResume();
        mIsStateSaved = false;
        mvpDelegate.resume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mIsStateSaved = true;
        mvpDelegate.saveState(outState);
    }

    @Override
    public void onStop() {
        super.onStop();
        mvpDelegate.stop(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getActivity().isFinishing()) {
            mvpDelegate.stop(false);
            mvpDelegate = null;
            return;
        }
        if (mIsStateSaved) {
            mIsStateSaved = false;
            return;
        }
        boolean anyParentIsRemoving = false;

        Fragment parent = getParentFragment();
        while (!anyParentIsRemoving && parent != null) {
            anyParentIsRemoving = parent.isRemoving();
            parent = parent.getParentFragment();
        }

        if (isRemoving() || anyParentIsRemoving) {
            mvpDelegate.stop(false);
            mvpDelegate = null;
            return;
        }
        mvpDelegate.stop(true);
    }

    protected MvpDelegate<PRESENTER> getMvpDelegate() {
        return mvpDelegate;
    }

    protected final PRESENTER getPresenter() {
        return mvpDelegate.getPresenter();
    }

}
