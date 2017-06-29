package serg.chuprin.sample;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import serg.chuprin.mvp_core.annotations.InjectViewState;
import serg.chuprin.sample.model.User;
import serg.chuprin.sample.model.UserInteractor;
import serg.chuprin.sample.view.UserView;

@InjectViewState()
public class UserPresenter extends BasePresenter<UserView> {

    private final UserInteractor interactor;

    @Inject
    public UserPresenter(UserInteractor interactor) {
        super();
        this.interactor = interactor;
    }

    @Override
    protected void onViewAttached() {
        if (isFirstAttach()) {
            interactor.getUser()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<User>() {
                        @Override
                        public void call(User user) {
                            getView().showUsername(user.getUsername());
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    });
        }
    }
}
