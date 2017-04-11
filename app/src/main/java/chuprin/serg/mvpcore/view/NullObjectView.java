package chuprin.serg.mvpcore.view;

import java.lang.ref.WeakReference;

public class NullObjectView<VIEW extends MvpView> {
    private WeakReference<VIEW> viewRef;
    private VIEW nullView;

    public final void setView(VIEW view) {
        viewRef = new WeakReference<>(view);
        if (nullView == null) {
            nullView = (VIEW) NullView.of(view.getClass());
        }
    }

    public final void removeView() {
        if (viewRef != null) {
            viewRef.clear();
            viewRef = null;
        }
    }

    public final VIEW get() {
        if (viewRef == null || viewRef.get() == null) {
            return nullView;
        }
        return viewRef.get();
    }

}
