package fr.univlr.debathon.application.viewmodel.mainwindow.debate.question;

import de.saxsys.mvvmfx.InjectScope;
import fr.univlr.debathon.application.viewmodel.ViewModel_SceneCycle;
import fr.univlr.debathon.job.db_project.jobclass.Mcq;
import fr.univlr.debathon.log.generate.CustomLogger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.ToggleGroup;

public class ResponseViewModel extends ViewModel_SceneCycle {

    private static final CustomLogger LOGGER = CustomLogger.create(ResponseViewModel.class.getName());

    private final QuestionViewModel questionView;
    private final Mcq response;

    //Text
    private final StringProperty lblResponse_label = new SimpleStringProperty("/");

    //Value
    private final BooleanProperty responseValue = new SimpleBooleanProperty(false);

    private ChangeListener<Boolean> listener_ChangedValue_;

    @InjectScope
    private ResponseScope responseScope;

    public ResponseViewModel(QuestionViewModel questionView, Mcq response) {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][constructor] Creation of the ResponseViewModel() object.");
        }

        this.questionView = questionView;
        this.response = response;

        bindResponse();
    }

    private void bindResponse() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][method] Usage of the ResponseViewModel.bindResponse()");
        }

        if (this.response != null) {
            this.lblResponse_label.bind(this.response.labelProperty());

            //Init
            this.listener_ChangedValue_ = (observableValue, oldValue, newValue) -> {
                if (Boolean.TRUE.equals(newValue)) {
                    this.questionView.listSelected_mcqProperty().add(this.response);
                } else {
                    this.questionView.listSelected_mcqProperty().remove(this.response);
                }
            };
            this.responseValue.addListener(this.listener_ChangedValue_);
        }
    }

    private void unbindResponse() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][method] Usage of the ResponseViewModel.unbindResponse()");
        }

        if (this.response != null) {
            this.lblResponse_label.unbind();

            if (this.listener_ChangedValue_ != null) {
                this.responseValue.removeListener(this.listener_ChangedValue_);
                this.listener_ChangedValue_ = null;
            }
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

    /**
     * Getter for the variable questionView.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link QuestionViewModel} - return the variable questionView.
     */
    public QuestionViewModel getQuestionView() {
        return this.questionView;
    }


    @Override
    public void onViewAdded_Cycle() {
    }

    @Override
    public void onViewRemoved_Cycle() {
        unbindResponse();
    }
}
