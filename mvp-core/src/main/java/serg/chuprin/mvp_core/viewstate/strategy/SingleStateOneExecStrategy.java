package serg.chuprin.mvp_core.viewstate.strategy;

import java.util.Queue;

import serg.chuprin.mvp_core.view.MvpView;
import serg.chuprin.mvp_core.viewstate.ViewCommand;

@SuppressWarnings("unused")
public class SingleStateOneExecStrategy extends SingleStateStrategy {

    @Override
    public <V extends MvpView> void afterExecute(Queue<ViewCommand<V>> currentCommands, ViewCommand<V> command) {
        StateStrategyUtils.removeCommand(currentCommands, command);
    }
}
