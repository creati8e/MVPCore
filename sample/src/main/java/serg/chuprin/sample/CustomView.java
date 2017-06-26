package serg.chuprin.sample;

import java.util.List;
import java.util.concurrent.FutureTask;

import serg.chuprin.mvp_core.view.MvpView;


public interface CustomView extends MvpView {

    <LIST extends List<Boolean>> void fun(LIST... lists) throws NullPointerException;

    void hasType(FutureTask<List<Boolean>> futureTask);
}
