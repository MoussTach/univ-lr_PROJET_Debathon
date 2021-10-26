package fr.univlr.debathon.application.viewmodel.mainwindow.debate.items;

import fr.univlr.debathon.application.viewmodel.ViewModel_SceneCycle;
import fr.univlr.debathon.log.generate.CustomLogger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TagViewModel extends ViewModel_SceneCycle implements Comparable<TagViewModel> {

    private static final CustomLogger LOGGER = CustomLogger.create(TagViewModel.class.getName());

    //Text
    private final StringProperty lblTag_label = new SimpleStringProperty("");

    //Value
    private final StringProperty color = new SimpleStringProperty("#FFA07A");


    public TagViewModel() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][constructor] Creation of the TagViewModel() object.");
        }
    }

    //Text
    /**
     * Property of the variable lblTag_label.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link StringProperty} - return the property of the variable lblTag_label.
     */
    public StringProperty lblTag_labelProperty() {
        return lblTag_label;
    }

    //Value
    /**
     * Property of the variable lblTag_label.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link StringProperty} - return the property of the variable lblTag_label.
     */
    public StringProperty colorProperty() {
        return color;
    }


    @Override
    public int compareTo(TagViewModel other) {
        return this.lblTag_label.get().compareTo(other.lblTag_label.get());
    }

    @Override
    public void onViewAdded_Cycle() {
    }

    @Override
    public void onViewRemoved_Cycle() {
    }
}
