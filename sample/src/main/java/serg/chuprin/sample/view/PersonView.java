package serg.chuprin.sample.view;

import java.util.List;

public interface PersonView<PU> extends ProgressView<List<PU>, PU> {

    void showUsers(PU models);

    void showUsers(List<PU> listPu);
}
