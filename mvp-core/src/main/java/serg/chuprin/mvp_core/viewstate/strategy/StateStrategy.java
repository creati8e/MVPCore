package serg.chuprin.mvp_core.viewstate.strategy;

import java.util.Queue;

import serg.chuprin.mvp_core.view.MvpView;
import serg.chuprin.mvp_core.viewstate.ViewCommand;

public interface StateStrategy {

    <V extends MvpView> void beforeExecute(Queue<ViewCommand<V>> currentCommands, ViewCommand<V> command);

    @SuppressWarnings({"unused"})
    <V extends MvpView> void afterExecute(Queue<ViewCommand<V>> currentCommands, ViewCommand<V> command);
}
