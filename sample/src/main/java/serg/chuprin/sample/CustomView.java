package serg.chuprin.sample;

import android.support.annotation.Nullable;

import java.util.List;
import java.util.concurrent.FutureTask;

import serg.chuprin.mvp_core.annotations.StateStrategyType;
import serg.chuprin.mvp_core.view.MvpView;
import serg.chuprin.mvp_core.viewstate.strategy.SkipStrategy;


public interface CustomView extends MvpView {

    void showDialogMode(int count, int DIALOG_MODE) throws NullPointerException;

    @StateStrategyType(value = SkipStrategy.class)
    void showType(@Nullable FutureTask<List<Boolean>> futureTask);
}
