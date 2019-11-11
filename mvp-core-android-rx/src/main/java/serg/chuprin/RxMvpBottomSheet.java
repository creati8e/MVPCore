package serg.chuprin;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import serg.chuprin.mvp_core.MvpPresenter;
import serg.chuprin.mvp_core.android.MvpBottomSheet;

@SuppressWarnings({ "unused" })
public abstract class RxMvpBottomSheet<PRESENTER extends MvpPresenter>
        extends MvpBottomSheet<PRESENTER> {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    public void onStop() {
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
