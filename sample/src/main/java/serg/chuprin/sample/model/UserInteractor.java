package serg.chuprin.sample.model;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
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
                .doOnNext(new Consumer<User>() {
                    @Override
                    public void accept(User user) throws Exception {
                        cachedUser = user;
                    }
                })
                .delay(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread());
    }
}
