package serg.chuprin.sample.repositories.list.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import java.util.List;

import javax.inject.Inject;

import serg.chuprin.mvp_core.android.MvpFragment;
import serg.chuprin.sample.R;
import serg.chuprin.sample.SampleApplication;
import serg.chuprin.sample.repositories.info.view.RepositoryInfoFragment;
import serg.chuprin.sample.repositories.list.di.RepositoriesListComponent;
import serg.chuprin.sample.repositories.list.di.RepositoriesListModule;
import serg.chuprin.sample.repositories.list.model.RepositoriesListPresenter;

public class RepositoriesListFragment extends MvpFragment<RepositoriesListPresenter>
        implements RepositoriesListView {

    @Inject
    RepositoriesListPresenter presenter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.goToRepositoryButton)
                .setOnClickListener(v -> {
                    requireActivity()
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainer, new RepositoryInfoFragment())
                            .addToBackStack(null)
                            .commit();
                });
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_repositories_list;
    }

    @Override
    public Object createComponent() {
        RepositoriesListModule module = new RepositoriesListModule();
        return SampleApplication.appComponent.repositoriesListComponent(module);
    }

    @Override
    public Class<?> componentClass() {
        return RepositoriesListComponent.class;
    }

    @Override
    public void showRepositories(List<String> repositories) {

    }

}
