package serg.chuprin.mvp_core.viewstate;


import java.util.LinkedList;
import java.util.Queue;

import serg.chuprin.mvp_core.view.MvpView;

public class MvpViewState<V extends MvpView> {
    private final Queue<ViewCommand<V>> commands = new LinkedList<>();

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

    public void destroyView() {
        commands.clear();
    }

    @SuppressWarnings({"unused"})
    protected void executeCommand(ViewCommand<V> command) {
        beforeExecute(command);
        if (view == null) {
            return;
        }
        command.execute(view);
        afterExecute(command);
    }

    private void beforeExecute(ViewCommand<V> command) {
        command.getStateStrategy().beforeExecute(commands, command);
    }

    private void afterExecute(ViewCommand<V> command) {
        command.getStateStrategy().beforeExecute(commands, command);
    }

    private void restoreView() {
        for (ViewCommand<V> command : commands) {
            command.execute(view);
        }
    }
}
