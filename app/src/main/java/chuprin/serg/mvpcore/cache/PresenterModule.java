package chuprin.serg.mvpcore.cache;

import android.os.Bundle;

import chuprin.serg.mvpcore.MvpPresenter;
import chuprin.serg.mvpcore.view.MvpView;
import dagger.Module;
import rx.functions.Func0;

@Module
public class PresenterModule {
    protected Bundle bundle;
    protected PresenterCache cache = PresenterCache.getInstance();

    public PresenterModule(Bundle bundle) {
        this.bundle = bundle;
    }

    private MvpPresenter<? extends MvpView> getCached() {
        if (bundle == null) {
            return null;
        }
        return cache.get(bundle);
    }

    @SuppressWarnings("unchecked")
    protected <P extends MvpPresenter<? extends MvpView>> P getPresenter(Func0<P> func0, Class<P> pClass) {
        MvpPresenter<? extends MvpView> cached = getCached();
        if (bundle == null || cached == null) {
            return func0.call();
        }
        try {
            return pClass.cast(cached);
        } catch (ClassCastException e) {
            return func0.call();
        }
    }
}
