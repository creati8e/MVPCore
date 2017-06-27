package serg.chuprin.mvp_core.viewstate.strategy;

import java.util.Queue;

import serg.chuprin.mvp_core.view.MvpView;
import serg.chuprin.mvp_core.viewstate.ViewCommand;

public class SingleStateStrategy implements StateStrategy {

    @Override
    public <V extends MvpView> void beforeApply(Queue<ViewCommand<V>> currentCommands, ViewCommand<V> command) {
        currentCommands.clear();
        currentCommands.add(command);
    }

    @Override
    public <V extends MvpView> void afterApply(Queue<ViewCommand<V>> currentCommands, ViewCommand<V> command) {

    }
}
