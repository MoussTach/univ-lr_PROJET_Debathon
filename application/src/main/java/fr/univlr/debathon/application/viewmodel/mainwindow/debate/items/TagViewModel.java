package fr.univlr.debathon.application.viewmodel.mainwindow.debate.items;

import fr.univlr.debathon.application.viewmodel.ViewModel_SceneCycle;
import fr.univlr.debathon.job.db_project.jobclass.Tag;
import fr.univlr.debathon.log.generate.CustomLogger;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TagViewModel extends ViewModel_SceneCycle implements Comparable<TagViewModel> {

    private static final CustomLogger LOGGER = CustomLogger.create(TagViewModel.class.getName());

    private final Tag tag;

    //Text
    private final StringProperty lblTag_label = new SimpleStringProperty("");

    //Value
    private final StringProperty color = new SimpleStringProperty("#FFA07A");


    public TagViewModel(Tag tag) {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][constructor] Creation of the TagViewModel() object.");
        }

        this.tag = tag;

        bindTag();
    }

    private void bindTag() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][method] Usage of the TagViewModel.bindTag()");
        }

        if (tag != null) {
            lblTag_label.bind(this.tag.labelProperty());

            Platform.runLater(() -> {
                color.set(this.tag.getColor());
                color.bind(this.tag.colorProperty());
            });
        }
    }

    private void unbindTag() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][method] Usage of the TagViewModel.unbindTag()");
        }

        if (tag != null) {
            lblTag_label.unbind();
            color.unbind();
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


    /**
     * Getter for the variable tag.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link Tag} - return the variable tag.
     */
    public Tag getTag() {
        return this.tag;
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
        unbindTag();
    }
}
