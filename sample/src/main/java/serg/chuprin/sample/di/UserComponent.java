package serg.chuprin.sample.di;

import dagger.Subcomponent;
import serg.chuprin.sample.view.UserActivity;

@Subcomponent(modules = UserModule.class)
@PerView
public interface UserComponent {

    void inject(UserActivity activity);
}
