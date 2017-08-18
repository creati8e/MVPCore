package serg.chuprin.sample;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import serg.chuprin.RxMvpPresenter;
import serg.chuprin.mvp_core.annotations.InjectViewState;
import serg.chuprin.sample.model.User;
import serg.chuprin.sample.model.UserInteractor;
import serg.chuprin.sample.view.UserView;

@InjectViewState
public class UserPresenter extends RxMvpPresenter<UserView> {

    private final UserInteractor interactor;

    @Inject
    public UserPresenter(UserInteractor interactor) {
        super();
        this.interactor = interactor;
    }

    @Override
    protected void onViewAttached() {
        subscribeView(interactor.getUser()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<User>() {
                    @Override
                    public void accept(User user) throws Exception {
                        getView().showUsername(user.getUsername());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                }));
    }
}
