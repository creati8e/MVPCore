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

    private void restoreView() {
        for (ViewCommand<V> command : commands) {
            command.execute(view);
        }
    }

    protected void applyCommand(ViewCommand<V> command) {
        beforeApply(command);
        if (view == null) {
            return;
        }
        command.execute(view);
        afterApply(command);
    }

    private void beforeApply(ViewCommand<V> command) {
        command.getStateStrategy().beforeApply(commands, command);
    }

    private void afterApply(ViewCommand<V> command) {
        command.getStateStrategy().beforeApply(commands, command);
    }

    public void destroyView() {
        commands.clear();
    }
}
