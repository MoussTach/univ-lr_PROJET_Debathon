package fr.univlr.debathon.application.viewmodel.mainwindow.debate.question;

import de.saxsys.mvvmfx.InjectScope;
import fr.univlr.debathon.application.viewmodel.ViewModel_SceneCycle;
import fr.univlr.debathon.log.generate.CustomLogger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.ToggleGroup;

public class ResponseViewModel extends ViewModel_SceneCycle {

    private static final CustomLogger LOGGER = CustomLogger.create(ResponseViewModel.class.getName());

    //TODO Model
    //private Response

    //Text
    private final StringProperty lblResponse_label = new SimpleStringProperty("/");

    //Value
    private final BooleanProperty responseValue = new SimpleBooleanProperty();

    @InjectScope
    private ResponseScope responseScope;

    public ResponseViewModel() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][constructor] Creation of the ResponseViewModel() object.");
        }

    }


    /**
     * Property of the variable lblResponse_value.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link StringProperty} - return the property of the variable lblResponse_value.
     */
    public StringProperty lblResponse_labelProperty() {
        return lblResponse_label;
    }

    /**
     * Property of the variable responseValue.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link BooleanProperty} - return the property of the variable responseValue.
     */
    public BooleanProperty responseValueProperty() {
        return responseValue;
    }

    public ToggleGroup getGroup() {
        return this.responseScope.gettGroup();
    }


    @Override
    public void onViewAdded_Cycle() {
    }

    @Override
    public void onViewRemoved_Cycle() {
    }
}
