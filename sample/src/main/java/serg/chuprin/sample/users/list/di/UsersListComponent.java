package serg.chuprin.sample.users.list.di;

import dagger.Subcomponent;
import serg.chuprin.sample.common.di.PerView;
import serg.chuprin.sample.users.list.view.UsersListActivity;

@Subcomponent(modules = UsersListModule.class)
@PerView
public interface UsersListComponent {

    void inject(UsersListActivity activity);

}
