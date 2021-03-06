package serg.chuprin.sample.users.info.view;

import android.os.Bundle;
import android.widget.TextView;

import javax.inject.Inject;

import serg.chuprin.mvp_core.android.MvpActivity;
import serg.chuprin.sample.R;
import serg.chuprin.sample.SampleApplication;
import serg.chuprin.sample.users.info.di.UserComponent;
import serg.chuprin.sample.users.info.di.UserModule;
import serg.chuprin.sample.users.info.model.UserPresenter;

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
    public void showUsername(String username) {
        ((TextView) findViewById(R.id.text)).setText(username);
    }

    @Override
    public void doSomething() {

    }

    @Override
    public void doElse() {

    }

    @Override
    public void doMore() {

    }
}
