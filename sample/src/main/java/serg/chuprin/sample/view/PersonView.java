package serg.chuprin.sample.view;

import java.util.List;

import serg.chuprin.mvp_core.view.MvpView;

public interface PersonView<T> extends MvpView {

    void showUsers(List<? extends T> models);
}
