package serg.chuprin.mvp_core.viewstate;

import serg.chuprin.mvp_core.view.MvpView;
import serg.chuprin.mvp_core.viewstate.strategy.SingleStateStrategy;

public class SampleCommand extends ViewCommand<MvpView> {

    private final String message;

    public SampleCommand(String message) {
        super(new SingleStateStrategy());
        this.message = message;
    }

    @Override
    public void execute(MvpView view) {
        //do something
    }
}
