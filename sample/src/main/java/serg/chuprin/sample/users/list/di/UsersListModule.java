package serg.chuprin.sample.users.list.di;

import dagger.Module;
import dagger.Provides;
import serg.chuprin.sample.users.list.model.UsersListPresenter;
import serg.chuprin.sample.common.di.PerView;

@Module
public class UsersListModule {

    @Provides
    @PerView
    UsersListPresenter providePresenter() {
        return new UsersListPresenter();
    }

}
