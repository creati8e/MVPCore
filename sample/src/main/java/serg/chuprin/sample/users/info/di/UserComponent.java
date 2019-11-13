package serg.chuprin.sample.users.info.di;

import dagger.Subcomponent;
import serg.chuprin.sample.common.di.PerView;
import serg.chuprin.sample.users.info.view.UserActivity;

@Subcomponent(modules = UserModule.class)
@PerView
public interface UserComponent {

    void inject(UserActivity activity);

}
