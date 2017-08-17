package serg.chuprin.mvp_core.viewstate.strategy;

import java.util.Queue;

import serg.chuprin.mvp_core.view.MvpView;
import serg.chuprin.mvp_core.viewstate.ViewCommand;

@SuppressWarnings("WeakerAccess")
public class AddToEndSingleStrategy implements StateStrategy {

    @Override
    public <V extends MvpView> void beforeExecute(Queue<ViewCommand<V>> currentCommands, ViewCommand<V> command) {
        StateStrategyUtils.removeCommand(currentCommands, command);
        currentCommands.add(command);
    }

    @Override
    public <V extends MvpView> void afterExecute(Queue<ViewCommand<V>> currentCommands, ViewCommand<V> command) {
    }
}
