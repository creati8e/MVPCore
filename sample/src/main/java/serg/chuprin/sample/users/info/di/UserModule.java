package serg.chuprin.sample.users.info.di;

import dagger.Module;
import dagger.Provides;
import serg.chuprin.sample.common.di.PerView;
import serg.chuprin.sample.users.info.model.UserPresenter;
import serg.chuprin.sample.common.model.UserInteractor;

@Module
public class UserModule {

    @Provides
    @PerView
    UserPresenter providePresenter(UserInteractor interactor) {
        return new UserPresenter(interactor);
    }

    @Provides
    @PerView
    UserInteractor provideInteractor() {
        return new UserInteractor();
    }
}
