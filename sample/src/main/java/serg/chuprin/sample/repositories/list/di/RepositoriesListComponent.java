package serg.chuprin.sample.repositories.list.di;

import dagger.Subcomponent;
import serg.chuprin.sample.common.di.PerView;
import serg.chuprin.sample.repositories.list.view.RepositoriesListFragment;

@Subcomponent(modules = RepositoriesListModule.class)
@PerView
public interface RepositoriesListComponent {

    void inject(RepositoriesListFragment fragment);

}
