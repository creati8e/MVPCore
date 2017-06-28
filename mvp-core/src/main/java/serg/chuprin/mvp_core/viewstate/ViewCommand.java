package serg.chuprin.mvp_core.viewstate;

import serg.chuprin.mvp_core.view.MvpView;
import serg.chuprin.mvp_core.viewstate.strategy.StateStrategy;

public abstract class ViewCommand<V extends MvpView> {

    private final StateStrategy stateStrategy;

    protected ViewCommand(StateStrategy stateStrategy) {
        this.stateStrategy = stateStrategy;
    }

    public StateStrategy getStateStrategy() {
        return stateStrategy;
    }

    public abstract void execute(V view);

}
