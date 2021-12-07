package fr.univlr.debathon.application.viewmodel.mainwindow.debate.items;

import de.saxsys.mvvmfx.InjectScope;
import fr.univlr.debathon.application.viewmodel.ViewModel_SceneCycle;
import fr.univlr.debathon.application.viewmodel.mainwindow.debate.question.ResponseScope;
import fr.univlr.debathon.job.db_project.jobclass.Category;
import fr.univlr.debathon.job.db_project.jobclass.Tag;
import fr.univlr.debathon.log.generate.CustomLogger;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.ToggleGroup;

public class CategorySelectViewModel extends ViewModel_SceneCycle {

    private static final CustomLogger LOGGER = CustomLogger.create(TagViewModel.class.getName());

    private final Category category;

    //Text
    private final StringProperty tbtnCategory_label = new SimpleStringProperty("");

    //Value
    private final StringProperty color = new SimpleStringProperty("#FFA07A");

    @InjectScope
    private SelectCategoryScope selectCategoryScope;

    public CategorySelectViewModel(Category category) {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][constructor] Creation of the CategorySelectViewModel() object.");
        }

        this.category = category;

        bindTag();
    }

    private void bindTag() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[private][method] Usage of the CategorySelectViewModel.bindTag()");
        }

        if (category != null) {
            tbtnCategory_label.bind(this.category.labelProperty());

            Platform.runLater(() -> {
                color.set(this.category.getColor());
                color.bind(this.category.colorProperty());
            });
        }
    }

    private void unbindTag() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[private][method] Usage of the CategorySelectViewModel.unbindTag()");
        }

        if (category != null) {
            tbtnCategory_label.unbind();
            color.unbind();
        }
    }


    public void addToList(boolean value) {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][method] Usage of the CategorySelectViewModel.addToList()");
        }

        if (value) {
            this.selectCategoryScope.selectedCategoryProperty().set(this.category);
        } else {
            if (this.selectCategoryScope.selectedCategoryProperty().get().equals(this.category)) {
                this.selectCategoryScope.selectedCategoryProperty().set(null);
            }
        }
    }


    //Text
    /**
     * Property of the variable tbtnCategory_label.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link StringProperty} - return the property of the variable tbtnCategory_label.
     */
    public StringProperty tbtnCategory_labelProperty() {
        return tbtnCategory_label;
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

    public ToggleGroup getGroup() {
        return this.selectCategoryScope.gettGroup();
    }


    @Override
    public void onViewAdded_Cycle() {
    }

    @Override
    public void onViewRemoved_Cycle() {
        unbindTag();
    }
}
