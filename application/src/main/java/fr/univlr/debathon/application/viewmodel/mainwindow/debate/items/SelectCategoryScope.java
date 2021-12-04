package fr.univlr.debathon.application.viewmodel.mainwindow.debate.items;

import de.saxsys.mvvmfx.Scope;
import fr.univlr.debathon.job.db_project.jobclass.Category;
import fr.univlr.debathon.job.db_project.jobclass.Mcq;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ToggleGroup;

public class SelectCategoryScope implements Scope {

    private final ToggleGroup tGroup = new ToggleGroup();
    private ListProperty<Category> selectedCategories = new SimpleListProperty<>(FXCollections.observableArrayList());

    public ToggleGroup gettGroup() {
        return tGroup;
    }


    /**
     * Property of the variable selectedCategories.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link ListProperty} - return the property of the variable selectedCategories.
     */
    public ListProperty<Category> selectedCategoriesProperty() {
        return selectedCategories;
    }
}
