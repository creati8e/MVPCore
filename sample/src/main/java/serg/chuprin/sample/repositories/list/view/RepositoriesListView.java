package serg.chuprin.sample.repositories.list.view;

import java.util.List;

import serg.chuprin.mvp_core.view.MvpView;

public interface RepositoriesListView extends MvpView {

    void showRepositories(List<String> repositories);

}
