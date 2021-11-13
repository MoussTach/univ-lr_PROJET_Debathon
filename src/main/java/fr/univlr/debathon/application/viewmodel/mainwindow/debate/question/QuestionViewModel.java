package fr.univlr.debathon.application.viewmodel.mainwindow.debate.question;

import de.saxsys.mvvmfx.InjectScope;
import de.saxsys.mvvmfx.ScopeProvider;
import de.saxsys.mvvmfx.ViewTuple;
import fr.univlr.debathon.application.view.mainwindow.debate.items.TagView;
import fr.univlr.debathon.application.view.mainwindow.debate.question.ResponseView;
import fr.univlr.debathon.application.viewmodel.ViewModel_SceneCycle;
import fr.univlr.debathon.application.viewmodel.mainwindow.MainScope;
import fr.univlr.debathon.application.viewmodel.mainwindow.MainViewScope;
import fr.univlr.debathon.application.viewmodel.mainwindow.debate.items.TagViewModel;
import fr.univlr.debathon.log.generate.CustomLogger;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

@ScopeProvider(scopes= {ResponseScope.class})
public class QuestionViewModel extends ViewModel_SceneCycle {

    private static final CustomLogger LOGGER = CustomLogger.create(QuestionViewModel.class.getName());

    //TODO Model
    //private Question

    //Text
    private final StringProperty lblQuestion_label = new SimpleStringProperty("/");

    //Value
    private final ListProperty<ViewTuple<ResponseView, ResponseViewModel> > listResponses = new SimpleListProperty<>(FXCollections.observableArrayList());


    @InjectScope
    private ResponseScope responseScope;


    private QuestionViewModel() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][constructor] Creation of the QuestionViewModel() object.");
        }

        //TODO charge listResponse with model
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


    @Override
    public void onViewAdded_Cycle() {
    }

    @Override
    public void onViewRemoved_Cycle() {
    }
}
