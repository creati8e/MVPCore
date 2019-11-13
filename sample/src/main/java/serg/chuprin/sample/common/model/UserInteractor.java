package serg.chuprin.sample.common.model;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class UserInteractor {
    private User cachedUser = null;

    @Inject
    public UserInteractor() {

    }

    public Observable<User> getUser() {
        if (cachedUser != null) {
            return Observable.just(cachedUser);
        }
        return Observable.just(new User(UUID.randomUUID().toString().substring(0, 10)))
                .doOnNext(user -> cachedUser = user)
                .delay(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread());
    }
}
