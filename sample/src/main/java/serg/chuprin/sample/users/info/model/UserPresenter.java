package serg.chuprin.sample.users.info.model;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import serg.chuprin.mvp_core.MvpPresenter;
import serg.chuprin.mvp_core.annotations.InjectViewState;
import serg.chuprin.sample.common.model.UserInteractor;
import serg.chuprin.sample.users.info.view.UserView;

@InjectViewState
public class UserPresenter extends MvpPresenter<UserView> {

    private final UserInteractor interactor;

    @Inject
    public UserPresenter(UserInteractor interactor) {
        super();
        this.interactor = interactor;
    }

    @Override
    protected void onViewAttached() {
        interactor
                .getUser()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        user -> getView().showUsername(user.getUsername()),
                        throwable -> throwable.printStackTrace()
                );
    }
}
