package fr.univlr.debathon.application.viewmodel.mainwindow.debate.items;

import de.saxsys.mvvmfx.InjectScope;
import fr.univlr.debathon.application.viewmodel.ViewModel_SceneCycle;
import fr.univlr.debathon.job.db_project.jobclass.Tag;
import fr.univlr.debathon.log.generate.CustomLogger;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TagSelectViewModel extends ViewModel_SceneCycle {

    private static final CustomLogger LOGGER = CustomLogger.create(TagViewModel.class.getName());

    private final Tag tag;

    //Text
    private final StringProperty tbtnTag_label = new SimpleStringProperty("");

    //Value
    private final StringProperty color = new SimpleStringProperty("#FFA07A");

    @InjectScope
    private SelectTagScope selectTagScope;


    public TagSelectViewModel(Tag tag) {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][constructor] Creation of the TagSelectViewModel() object.");
        }

        this.tag = tag;

        bindTag();
    }

    private void bindTag() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][method] Usage of the TagSelectViewModel.bindTag()");
        }

        if (tag != null) {
            tbtnTag_label.bind(this.tag.labelProperty());

            Platform.runLater(() -> {
                color.set(this.tag.getColor());
                color.bind(this.tag.colorProperty());
            });
        }
    }

    private void unbindTag() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][method] Usage of the TagSelectViewModel.unbindTag()");
        }

        if (tag != null) {
            tbtnTag_label.unbind();
            color.unbind();
        }
    }


    public void addToList(boolean value) {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][method] Usage of the TagSelectViewModel.addToList()");
        }

        if (value) {
            this.selectTagScope.selectedTagsProperty().add(this.tag);
        } else {
            this.selectTagScope.selectedTagsProperty().remove(this.tag);
        }
    }


    //Text
    /**
     * Property of the variable tbtnTag_label.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link StringProperty} - return the property of the variable tbtnTag_label.
     */
    public StringProperty tbtnTag_labelProperty() {
        return tbtnTag_label;
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
    public void onViewAdded_Cycle() {
    }

    @Override
    public void onViewRemoved_Cycle() {
        unbindTag();
    }
}
