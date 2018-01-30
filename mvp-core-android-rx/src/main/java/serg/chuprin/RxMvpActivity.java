package serg.chuprin;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import serg.chuprin.mvp_core.MvpPresenter;
import serg.chuprin.mvp_core.android.MvpActivity;

@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class RxMvpActivity<PRESENTER extends MvpPresenter> extends MvpActivity<PRESENTER> {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onStop() {
        super.onStop();
        unsubscribeAll();
    }

    protected final void addDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    protected final boolean removeDisposable(Disposable disposable) {
        return compositeDisposable.remove(disposable);
    }

    protected final void unsubscribeAll() {
        compositeDisposable.clear();
    }

    protected final CompositeDisposable getCompositeDisposable() {
        return compositeDisposable;
    }
}
