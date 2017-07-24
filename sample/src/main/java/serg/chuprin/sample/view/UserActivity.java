package serg.chuprin.sample.view;

import android.os.Bundle;

import java.util.List;

import javax.inject.Inject;

import serg.chuprin.mvp_core.android.MvpActivity;
import serg.chuprin.sample.R;
import serg.chuprin.sample.SampleApplication;
import serg.chuprin.sample.UserPresenter;
import serg.chuprin.sample.di.UserComponent;
import serg.chuprin.sample.di.UserModule;
import serg.chuprin.sample.model.User;

public class UserActivity extends MvpActivity<UserPresenter> implements UserView {

    @Inject UserPresenter presenter;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_user;
    }

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        findViewById(R.id.text).setSaveEnabled(false);
    }

    @Override
    public Object createComponent() {
        return SampleApplication.appComponent.userComponent(new UserModule());
    }

    @Override
    public Class<?> componentClass() {
        return UserComponent.class;
    }

    @Override
    public void showUsers(List<? extends User> users) {

    }
}
