package serg.chuprin;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import serg.chuprin.mvp_core.MvpPresenter;
import serg.chuprin.mvp_core.view.MvpView;

@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class RxMvpPresenter<VIEW extends MvpView> extends MvpPresenter<VIEW> {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onViewDetached() {
        unsubscribeAll();
    }

    protected final void subscribeView(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    protected final void unsubscribeAll() {
        compositeDisposable.clear();
    }

    protected final boolean removeDisposable(Disposable disposable) {
        return compositeDisposable.remove(disposable);
    }

    protected final CompositeDisposable getCompositeDisposable() {
        return compositeDisposable;
    }
}
