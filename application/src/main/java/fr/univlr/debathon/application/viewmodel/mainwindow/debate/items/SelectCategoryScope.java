package fr.univlr.debathon.application.viewmodel.mainwindow.debate.items;

import de.saxsys.mvvmfx.Scope;
import fr.univlr.debathon.job.db_project.jobclass.Category;
import fr.univlr.debathon.job.db_project.jobclass.Mcq;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ToggleGroup;

public class SelectCategoryScope implements Scope {

    private final ToggleGroup tGroup = new ToggleGroup();
    private ObjectProperty<Category> selectedCategory = new SimpleObjectProperty<>();

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
