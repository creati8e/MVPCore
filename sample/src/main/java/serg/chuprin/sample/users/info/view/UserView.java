package serg.chuprin.sample.users.info.view;

import serg.chuprin.mvp_core.annotations.StateStrategyType;
import serg.chuprin.mvp_core.viewstate.strategy.SkipStrategy;
import serg.chuprin.sample.common.view.ParentUserView;

public interface UserView extends ParentUserView {

    @StateStrategyType(value = SkipStrategy.class)
    void showUsername(String username);

}
