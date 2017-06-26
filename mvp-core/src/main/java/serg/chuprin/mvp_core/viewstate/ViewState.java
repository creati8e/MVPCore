package serg.chuprin.mvp_core.viewstate;


import java.util.LinkedList;
import java.util.Queue;

import serg.chuprin.mvp_core.view.MvpView;
import serg.chuprin.mvp_core.viewstate.strategy.StateStrategy;

public class ViewState<V extends MvpView> {
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
            commands.add(command);
            return;
        }
        //call sampleCommand on view
        afterApply(command);
    }

    protected void beforeApply(ViewCommand<V> command) {
        StateStrategy stateStrategy = command.getStateStrategy();
        stateStrategy.beforeApply(commands, command);
    }

    protected void afterApply(ViewCommand<V> command) {
        StateStrategy stateStrategy = command.getStateStrategy();
        stateStrategy.beforeApply(commands, command);
    }
}
