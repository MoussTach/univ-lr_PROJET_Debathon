package fr.univlr.debathon.application.viewmodel.mainwindow.debate.items;

import de.saxsys.mvvmfx.Scope;
import fr.univlr.debathon.job.db_project.jobclass.Category;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.ToggleGroup;

public class SelectCategoryScope implements Scope {

    private final ToggleGroup tGroup = new ToggleGroup();
    private final ObjectProperty<Category> selectedCategory = new SimpleObjectProperty<>();

    public ToggleGroup gettGroup() {
        return tGroup;
    }


    /**
     * Property of the variable selectedCategory.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link ObjectProperty} - return the property of the variable selectedCategory.
     */
    public ObjectProperty<Category> selectedCategoryProperty() {
        return selectedCategory;
    }
}
