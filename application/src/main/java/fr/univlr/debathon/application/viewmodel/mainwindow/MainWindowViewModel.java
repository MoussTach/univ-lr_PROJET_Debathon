package fr.univlr.debathon.application.viewmodel.mainwindow;

import de.saxsys.mvvmfx.InjectScope;
import de.saxsys.mvvmfx.ScopeProvider;
import de.saxsys.mvvmfx.utils.commands.CompositeCommand;
import fr.univlr.debathon.application.viewmodel.ViewModel_SceneCycle;
import fr.univlr.debathon.log.generate.CustomLogger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.BorderPane;

/**
 * ViewModel of the view {@link fr.univlr.debathon.application.view.mainwindow.MainWindowView}.
 * This ViewModel process some function for the whole application.
 *
 * @author Gaetan Brenckle
 */
@ScopeProvider(scopes= {MainScope.class, MainViewScope.class})
public class MainWindowViewModel extends ViewModel_SceneCycle {

    private static final CustomLogger LOGGER = CustomLogger.create(MainWindowViewModel.class.getName());

    //Value
    private final BooleanProperty isHomeCommandExecutable = new SimpleBooleanProperty(false);

    @InjectScope
    private MainScope mainScope;

    @InjectScope
    private MainViewScope mainViewScope;


    /**
     * Default constructor
     *
     * @author Gaetan Brenckle
     */
    public MainWindowViewModel() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][constructor] Creation of the MainWindowViewModel() object.");
        }
    }

    public void initialize() {
        this.isHomeCommandExecutable.bind(this.mainViewScope.homeCommandProperty().isNull());
    }

    /**
     * action when the button to go to the home view.
     *
     * @author Gaetan Brenckle
     */
    public void actvm_btnHome() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[private][method] usage of MainWindowViewModel.actvm_btnHome().");
        }

        CompositeCommand command = this.mainViewScope.homeCommandProperty().get();
        if (command != null && command.isExecutable())
            command.execute();
    }


    /**
     * Setter for the scope property mainScope.bPaneNodeProperty().
     *
     * @author Gaetan Brenckle
     * @param bPaneMain - borderPane to link
     */
    public void setbPaneMainProperty(BorderPane bPaneMain) {
        mainViewScope.basePaneProperty().set(bPaneMain);
    }

    /**
     * Bind the progress property with the scope.
     *
     * @author Gaetan Brenckle
     * @param doubleProperty - progress property to link
     */
    public void bindProgressProperty(DoubleProperty doubleProperty) {
        doubleProperty.bind(mainScope.progressProperty());
    }

    /**
     * Bind the progress_label property with the scope.
     *
     * @author Gaetan Brenckle
     * @param labelProperty - progress_label to link
     */
    public void bindProgress_labelProperty(StringProperty labelProperty) {
        labelProperty.bind(mainScope.progress_labelProperty());
    }

    /**
     * Property of the variable isHomeCommandExecutable.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link BooleanProperty} - return the property of the variable isHomeCommandExecutable.
     */
    public BooleanProperty isHomeCommandExecutableProperty() {
        return isHomeCommandExecutable;
    }


    @Override
    public void onViewAdded_Cycle() {
        // default implementation
    }

    @Override
    public void onViewRemoved_Cycle() {
        this.isHomeCommandExecutable.unbind();
    }
}