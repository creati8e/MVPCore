package serg.chuprin.sample.view;

import android.util.Pair;

public interface FakeView<G, F> extends ProgressView<Pair<G, F>> {

    void fake(G fake);
}
