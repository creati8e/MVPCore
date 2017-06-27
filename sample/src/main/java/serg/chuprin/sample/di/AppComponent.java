package serg.chuprin.sample.di;

import javax.inject.Singleton;

import dagger.Component;

@Component
@Singleton
public interface AppComponent {

    UserComponent userComponent(UserModule module);
}
