package serg.chuprin.sample.users.list.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import javax.inject.Inject;

import serg.chuprin.mvp_core.android.MvpActivity;
import serg.chuprin.sample.R;
import serg.chuprin.sample.SampleApplication;
import serg.chuprin.sample.users.info.view.UserActivity;
import serg.chuprin.sample.users.list.di.UsersListComponent;
import serg.chuprin.sample.users.list.di.UsersListModule;
import serg.chuprin.sample.users.list.model.UsersListPresenter;

public class UsersListActivity extends MvpActivity<UsersListPresenter> implements UsersListView {

    @Inject
    UsersListPresenter presenter;

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        findViewById(R.id.goToUserButton).setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), UserActivity.class));
        });
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_users_list;
    }

    @Override
    public Object createComponent() {
        return SampleApplication.appComponent.usersListComponent(new UsersListModule());
    }

    @Override
    public Class<?> componentClass() {
        return UsersListComponent.class;
    }

}
