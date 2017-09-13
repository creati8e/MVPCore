package serg.chuprin.sample.view;

import serg.chuprin.mvp_core.annotations.StateStrategyType;
import serg.chuprin.mvp_core.viewstate.strategy.SkipStrategy;

public interface UserView extends ParentUserView {

    @StateStrategyType(value = SkipStrategy.class)
    void showUsername(String username);

}
