package fr.univlr.debathon.application.viewmodel.mainwindow.debate.items;

import fr.univlr.debathon.application.viewmodel.ViewModel_SceneCycle;
import fr.univlr.debathon.job.db_project.jobclass.Category;
import fr.univlr.debathon.log.generate.CustomLogger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CategoryViewModel extends ViewModel_SceneCycle implements Comparable<CategoryViewModel> {

    private static final CustomLogger LOGGER = CustomLogger.create(CategoryViewModel.class.getName());

    private final Category category;

    //Text
    private final StringProperty lblCategory_label = new SimpleStringProperty("");

    //Value
    private final StringProperty color = new SimpleStringProperty("#FFA07A");


    public CategoryViewModel(Category category) {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][constructor] Creation of the CategoryViewModel() object.");
        }

        this.category = category;

        bindCategory();
    }

    private void bindCategory() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[private][method] Usage of the CategoryViewModel.bindTag()");
        }

        if (category != null) {
            lblCategory_label.bind(this.category.labelProperty());

            /* Color
            Platform.runLater(() -> {
                color.set(this.category.getColor());
                color.bind(this.category.colorProperty());
            });
             */
        }
    }

    private void unbindCategory() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[private][method] Usage of the TagViewModel.unbindCategory()");
        }

        if (category != null) {
            lblCategory_label.unbind();
            color.unbind();
        }
    }


    //Text
    /**
     * Property of the variable lblCategory_label.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link StringProperty} - return the property of the variable lblCategory_label.
     */
    public StringProperty lblCategory_labelProperty() {
        return lblCategory_label;
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
     * Getter for the variable category.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link Category} - return the variable category.
     */
    public Category getCategory() {
        return this.category;
    }


    @Override
    public int compareTo(CategoryViewModel other) {
        return this.lblCategory_label.get().compareTo(other.lblCategory_label.get());
    }

    @Override
    public void onViewAdded_Cycle() {
    }

    @Override
    public void onViewRemoved_Cycle() {
        unbindCategory();
    }
}
