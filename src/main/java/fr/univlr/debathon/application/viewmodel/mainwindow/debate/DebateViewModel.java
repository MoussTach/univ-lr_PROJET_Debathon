package fr.univlr.debathon.application.viewmodel.mainwindow.debate;

import de.saxsys.mvvmfx.InjectScope;
import fr.univlr.debathon.application.viewmodel.ViewModel_SceneCycle;
import fr.univlr.debathon.application.viewmodel.mainwindow.MainViewScope;
import fr.univlr.debathon.log.generate.CustomLogger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DebateViewModel extends ViewModel_SceneCycle {

    private static final CustomLogger LOGGER = CustomLogger.create(DebateViewModel.class.getName());

    //Text
    //Value
    private final StringProperty lblTitle_value = new SimpleStringProperty("/");

    @InjectScope
    private MainViewScope mainViewScope;

    /**
     * Default constructor
     *
     * @author Gaetan Brenckle
     */
    public DebateViewModel() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][constructor] Creation of the DebateViewModel() object.");
        }

    }


    /**
     * Property of the variable lblTitle_value.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link StringProperty} - return the property of the variable lblTitle_value.
     */
    public StringProperty lblTitle_valueProperty() {
        return lblTitle_value;
    }


    @Override
    public void onViewAdded_Cycle() {
    }

    @Override
    public void onViewRemoved_Cycle() {
    }
}
