package fr.univlr.debathon.application.viewmodel.mainwindow.debate.items;

import de.saxsys.mvvmfx.Scope;
import fr.univlr.debathon.job.db_project.jobclass.Tag;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

public class SelectTagScope implements Scope {

    private ListProperty<Tag> selectedTags = new SimpleListProperty<>(FXCollections.synchronizedObservableList(FXCollections.observableArrayList()));

    /**
     * Property of the variable selectedTags.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link ListProperty} - return the property of the variable selectedTags.
     */
    public ListProperty<Tag> selectedTagsProperty() {
        return selectedTags;
    }
}
