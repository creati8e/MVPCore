package serg.chuprin.sample.view;

import android.util.Pair;

import java.util.Set;
import java.util.concurrent.Future;

import serg.chuprin.mvp_core.view.MvpView;


public interface ProgressView<BAR, FOO> extends MvpView {

    void showProgress(Set<BAR> progress, Pair<BAR, Future<FOO>> g);


}
