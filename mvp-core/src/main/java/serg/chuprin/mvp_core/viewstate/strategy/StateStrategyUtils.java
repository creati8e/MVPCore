package serg.chuprin.mvp_core.viewstate.strategy;

import java.util.Iterator;
import java.util.Queue;

import serg.chuprin.mvp_core.view.MvpView;
import serg.chuprin.mvp_core.viewstate.ViewCommand;

class StateStrategyUtils {

    static <V extends MvpView> void removeCommand(Queue<ViewCommand<V>> currentCommands, ViewCommand<V> command) {
        Iterator<ViewCommand<V>> iterator = currentCommands.iterator();

        while (iterator.hasNext()) {

            if (iterator.next().getClass() == command.getClass()) {
                iterator.remove();
                break;
            }
        }
    }
}
