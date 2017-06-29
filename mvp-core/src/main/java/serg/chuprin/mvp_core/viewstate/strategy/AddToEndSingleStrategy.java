package serg.chuprin.mvp_core.viewstate.strategy;

import java.util.Iterator;
import java.util.Queue;

import serg.chuprin.mvp_core.view.MvpView;
import serg.chuprin.mvp_core.viewstate.ViewCommand;

public class AddToEndSingleStrategy implements StateStrategy {

    @Override
    public <V extends MvpView> void beforeExecute(Queue<ViewCommand<V>> currentCommands, ViewCommand<V> command) {
        Iterator<ViewCommand<V>> iterator = currentCommands.iterator();

        while (iterator.hasNext()) {

            if (iterator.next().getClass() == command.getClass()) {
                iterator.remove();
                break;
            }
        }

        currentCommands.add(command);
    }

    @Override
    public <V extends MvpView> void afterExecute(Queue<ViewCommand<V>> currentCommands, ViewCommand<V> command) {
    }
}
