package serg.chuprin.mvp_core.viewstate;


import java.util.LinkedList;
import java.util.Queue;

import serg.chuprin.mvp_core.view.MvpView;

public class ViewState<V extends MvpView> {
    private final Queue<? extends ViewCommand<V>> commands = new LinkedList<>();

    private V view;

    public void attachView(V view) {
        if (view == null) {
            throw new IllegalStateException("view is null");
        }
        this.view = view;
        restoreView();
    }

    public void detachView() {
        view = null;
    }

    private void restoreView() {
        for (ViewCommand<V> command : commands) {
            command.execute(view);
        }
    }
}
