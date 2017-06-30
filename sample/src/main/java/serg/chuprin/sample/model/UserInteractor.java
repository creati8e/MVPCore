package serg.chuprin.sample.model;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public class UserInteractor {

    @Inject
    public UserInteractor() {

    }

    public Single<User> getUser() {
        return Single.just(new User(UUID.randomUUID().toString().substring(0, 10)))
                .delay(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread());
    }
}
