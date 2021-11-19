package fr.univlr.debathon.application.viewmodel.mainwindow.debate.question;

import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.InjectScope;
import de.saxsys.mvvmfx.ScopeProvider;
import de.saxsys.mvvmfx.ViewTuple;
import fr.univlr.debathon.application.view.mainwindow.debate.question.ResponseView;
import fr.univlr.debathon.application.viewmodel.ViewModel_SceneCycle;
import fr.univlr.debathon.job.db_project.jobclass.Mcq;
import fr.univlr.debathon.job.db_project.jobclass.Question;
import fr.univlr.debathon.log.generate.CustomLogger;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;

@ScopeProvider(scopes= {ResponseScope.class})
public class QuestionViewModel extends ViewModel_SceneCycle {

    private static final CustomLogger LOGGER = CustomLogger.create(QuestionViewModel.class.getName());

    private final Question question;

    //Text
    private final StringProperty lblQuestion_label = new SimpleStringProperty("/");

    //Value
    private final ListProperty<ViewTuple<ResponseView, ResponseViewModel> > listResponses = new SimpleListProperty<>(FXCollections.observableArrayList());

    private ListChangeListener<Mcq> listChangeListener_response = null;

    @InjectScope
    private ResponseScope responseScope;


    public QuestionViewModel(Question question) {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][constructor] Creation of the QuestionViewModel() object.");
        }

        this.question = question;

        bindQuestion();
    }

    private void bindQuestion() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][method] Usage of the QuestionViewModel.bindQuestion()");
        }

        if (this.question != null) {
            lblQuestion_label.bind(this.question.labelProperty());

            this.listChangeListener_response = change -> {
                while (change.next()) {
                    if (change.wasAdded()) {
                        change.getAddedSubList().forEach(item ->
                                Platform.runLater(() -> {
                                    if (item != null) {
                                        ResponseViewModel responseViewModel = new ResponseViewModel(item);
                                        final ViewTuple<ResponseView, ResponseViewModel> responseViewTuple = FluentViewLoader.fxmlView(ResponseView.class)
                                                .viewModel(responseViewModel)
                                                .load();

                                        listResponses.add(responseViewTuple);
                                    }
                                })
                        );
                    } else if (change.wasRemoved()) {
                        change.getRemoved().forEach(item -> Platform.runLater(() -> listResponses.removeIf(response -> response.getViewModel().getResponse().equals(item))));
                    }
                }
            };

            //TODO Response
            //this.question.listResponse().addListener(this.listChangeListener_response);
        }
    }

    private void unbindQuestion() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][method] Usage of the QuestionViewModel.unbindQuestion()");
        }

        if (this.question != null) {
            lblQuestion_label.unbind();

            /*TODO Response
            if (this.listChangeListener_response != null) {
                this.question.listResponse().removeListener(this.listChangeListener_response);
                this.listChangeListener_response = null;
            }*/
        }
    }


    /**
     * Create a new window that display the comments of this question
     *
     * @author Gaetan Brenckle
     */
    public void actvm_btnComments() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][method] usage of QuestionViewModel.actvm_btnComments().");
        }

        //TODO window comments
    }


    /**
     * Property of the variable lblQuestion_label.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link StringProperty} - return the property of the variable lblQuestion_label.
     */
    public StringProperty lblQuestion_labelProperty() {
        return lblQuestion_label;
    }

    /**
     * Property of the variable listResponses.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link ListProperty} - return the property of the variable listResponses.
     */
    public ListProperty<ViewTuple<ResponseView, ResponseViewModel> > listResponsesProperty() {
        return listResponses;
    }

    /**
     * Getter for the variable question.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link Question} - return the variable question.
     */
    public Question getQuestion() {
        return this.question;
    }


    @Override
    public void onViewAdded_Cycle() {
    }

    @Override
    public void onViewRemoved_Cycle() {
        unbindQuestion();
    }
}
