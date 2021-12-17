package fr.univlr.debathon.application.viewmodel.mainwindow.debate.question;

import de.saxsys.mvvmfx.Scope;
import fr.univlr.debathon.job.db_project.jobclass.Mcq;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.ToggleGroup;

public class ResponseScope implements Scope {

    private final ToggleGroup tGroup = new ToggleGroup();
    private final ObjectProperty<Mcq> selected = new SimpleObjectProperty<>();

    public ToggleGroup gettGroup() {
        return tGroup;
    }

    public Mcq getSelected() {
        return selected.get();
    }

    /**
     * Property of the variable selected.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link ObjectProperty} - return the property of the variable selected.
     */
    public ObjectProperty<Mcq> selectedProperty() {
        return selected;
    }
}