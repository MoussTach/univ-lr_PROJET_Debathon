package fr.univlr.debathon.application.viewmodel.mainwindow;

import de.saxsys.mvvmfx.Scope;
import de.saxsys.mvvmfx.utils.commands.CompositeCommand;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.BorderPane;

/**
 * Scope created to swap the main view of the application.
 *
 * @author Gaetan Brenckle
 */
public class MainViewScope implements Scope {

    private final ObjectProperty<BorderPane> basePane = new SimpleObjectProperty<>();

    private final ObjectProperty<CompositeCommand> prevCommand = new SimpleObjectProperty<>(null);
    private final ObjectProperty<CompositeCommand> currentCommand = new SimpleObjectProperty<>(null);
    private final ObjectProperty<CompositeCommand> homeCommand = new SimpleObjectProperty<>(new CompositeCommand());


    /**
     * Property of the variable basePane.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link ObjectProperty} - return the property of the variable basePane.
     */
    public ObjectProperty<BorderPane> basePaneProperty() {
        return basePane;
    }

    /**
     * Property of the variable prevCommand.
     * Use to set the execution the current view.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link ObjectProperty} - return the property of the variable prevCommand.
     */
    public ObjectProperty<CompositeCommand> prevCommandProperty() {
        return prevCommand;
    }

    /**
     * Property of the variable currentCommand.
     * Use to set the execution the current view.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link ObjectProperty} - return the property of the variable currentCommand.
     */
    public ObjectProperty<CompositeCommand> currentCommandProperty() {
        return currentCommand;
    }

    /**
     * Property of the variable homeCommand.
     * Use to set the execution the current view.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link ObjectProperty} - return the property of the variable homeCommand.
     */
    public ObjectProperty<CompositeCommand> homeCommandProperty() {
        return homeCommand;
    }
}
