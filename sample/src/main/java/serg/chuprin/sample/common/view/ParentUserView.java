package serg.chuprin.sample.common.view;

import serg.chuprin.mvp_core.annotations.StateStrategyType;
import serg.chuprin.mvp_core.view.MvpView;
import serg.chuprin.mvp_core.viewstate.strategy.AddToEndSingleStrategy;
import serg.chuprin.mvp_core.viewstate.strategy.SingleStateStrategy;
import serg.chuprin.mvp_core.viewstate.strategy.SkipStrategy;

@StateStrategyType(SkipStrategy.class)
public interface ParentUserView extends MvpView {

    void doSomething();

    @StateStrategyType(AddToEndSingleStrategy.class)
    void doElse();

    @StateStrategyType(SingleStateStrategy.class)
    void doMore();
}
