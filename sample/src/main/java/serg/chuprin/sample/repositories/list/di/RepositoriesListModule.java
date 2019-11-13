package serg.chuprin.sample.repositories.list.di;

import dagger.Module;
import dagger.Provides;
import serg.chuprin.sample.common.di.PerView;
import serg.chuprin.sample.repositories.list.model.RepositoriesListPresenter;

@Module
public class RepositoriesListModule {

    @Provides
    @PerView
    RepositoriesListPresenter providePresenter() {
        return new RepositoriesListPresenter();
    }

}
