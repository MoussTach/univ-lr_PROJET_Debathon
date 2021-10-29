package fr.univlr.debathon.application.viewmodel.mainwindow;

import de.saxsys.mvvmfx.Scope;
import fr.univlr.debathon.log.generate.CustomLogger;
import javafx.beans.property.*;
import javafx.scene.layout.BorderPane;

/**
 * Main scope of the application, used to contain the loading information when the user want to access
 * on a other view.
 *
 * @author Gaetan Brenckle
 */
public class MainScope implements Scope {

    private final DoubleProperty progressProperty_ = new SimpleDoubleProperty();
    private final StringProperty progress_label_ = new SimpleStringProperty();

    /**
     * Default constructor.
     * Add a listener to log each time the progression of the loading is modified.
     *
     * @author Gaetan Brenckle
     */
    public MainScope() {
        // default implementation
    }


    /**
     * ProgressProprety to bind.
     *
     * @author Gaetan Brenckle
     * @return {@link DoubleProperty} - progress property to link
     */
    public DoubleProperty progressProperty() {
        return progressProperty_;
    }

    /**
     * text to bind.
     *
     * @author Gaetan Brenckle
     * @return {@link StringProperty} - String property to link
     */
    public StringProperty progress_labelProperty() {
        return progress_label_;
    }
}