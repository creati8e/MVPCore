package serg.chuprin.sample.di;

import dagger.Module;
import dagger.Provides;
import serg.chuprin.sample.UserPresenter;
import serg.chuprin.sample.model.UserInteractor;

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
