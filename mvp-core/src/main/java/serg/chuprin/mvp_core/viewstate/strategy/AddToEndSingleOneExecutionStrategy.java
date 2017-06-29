package serg.chuprin.mvp_core.viewstate.strategy;

import java.util.Queue;

import serg.chuprin.mvp_core.view.MvpView;
import serg.chuprin.mvp_core.viewstate.ViewCommand;

public class AddToEndSingleOneExecutionStrategy extends AddToEndSingleStrategy implements StateStrategy {

    @Override
    public <V extends MvpView> void afterExecute(Queue<ViewCommand<V>> currentCommands, ViewCommand<V> command) {
        currentCommands.remove();
    }
}
