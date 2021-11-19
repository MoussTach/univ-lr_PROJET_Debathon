package fr.univlr.debathon.application.viewmodel.mainwindow.debate.question;

import de.saxsys.mvvmfx.InjectScope;
import fr.univlr.debathon.application.viewmodel.ViewModel_SceneCycle;
import fr.univlr.debathon.job.db_project.jobclass.Mcq;
import fr.univlr.debathon.log.generate.CustomLogger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.ToggleGroup;

public class ResponseViewModel extends ViewModel_SceneCycle {

    private static final CustomLogger LOGGER = CustomLogger.create(ResponseViewModel.class.getName());

    private final Mcq response;

    //Text
    private final StringProperty lblResponse_label = new SimpleStringProperty("/");

    //Value
    private final BooleanProperty responseValue = new SimpleBooleanProperty();

    @InjectScope
    private ResponseScope responseScope;

    public ResponseViewModel(Mcq response) {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][constructor] Creation of the ResponseViewModel() object.");
        }

        this.response = response;

        bindResponse();
    }

    private void bindResponse() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][method] Usage of the ResponseViewModel.bindResponse()");
        }

        if (this.response != null) {
            this.lblResponse_label.bind(this.response.labelProperty());

            //TODO value
        }
    }

    private void unbindResponse() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][method] Usage of the ResponseViewModel.unbindResponse()");
        }

        if (this.response != null) {
            this.lblResponse_label.unbind();

            //TODO value
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


    /**
     * Getter for the variable response.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link Mcq} - return the variable response.
     */
    public Mcq getResponse() {
        return this.response;
    }



    @Override
    public void onViewAdded_Cycle() {
    }

    @Override
    public void onViewRemoved_Cycle() {
        unbindResponse();
    }
}
