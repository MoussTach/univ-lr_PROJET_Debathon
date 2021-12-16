package fr.univlr.debathon.application.viewmodel.sidewindow.comments;

import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.ViewTuple;
import fr.univlr.debathon.application.view.mainwindow.debate.items.CategoryView;
import fr.univlr.debathon.application.view.sidewindow.comments.CommentsView;
import fr.univlr.debathon.application.viewmodel.ViewModel_SceneCycle;
import fr.univlr.debathon.application.viewmodel.mainwindow.debate.DebateThumbnailViewModel;
import fr.univlr.debathon.application.viewmodel.mainwindow.debate.items.CategoryViewModel;
import fr.univlr.debathon.job.db_project.jobclass.Question;
import fr.univlr.debathon.job.db_project.jobclass.Room;
import fr.univlr.debathon.log.generate.CustomLogger;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.scene.layout.BorderPane;

public class CommentsWindowsViewModel extends ViewModel_SceneCycle {

    private static final CustomLogger LOGGER = CustomLogger.create(CommentsWindowsViewModel.class.getName());

    private final Room debate;
    private final Question question;

    //Text
    private final StringProperty lblDebate_label = new SimpleStringProperty("/");
    private final StringProperty lblQuestion_label = new SimpleStringProperty("/");

    //Value
    private final StringProperty tfCommentary_value = new SimpleStringProperty("");
    private final ListProperty<BorderPane> listComments = new SimpleListProperty<>(FXCollections.observableArrayList());

    /**
     * Default constructor
     *
     * @param debate {@link Room} - the debate associated to this comments window.
     * @param question {@link Question} - the question associated to this comments window.
     *
     * @author Gaetan Brenckle
     */
    public CommentsWindowsViewModel(Room debate, Question question) {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][constructor] Creation of the CommentsWindowsViewModel() object.");
        }

        this.debate = debate;
        this.question = question;

        //TODO print
        if (this.question != null) {
            this.question.getListComment().forEach(System.out::println);
        }

        this.bindCommentary();
    }


    private void bindCommentary() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][method] Usage of the CommentsWindowsViewModel.bindCommentary()");
        }

        if (this.debate != null) {
            this.lblDebate_label.set(this.debate.getLabel());
        }

        if (this.question != null) {
            this.lblQuestion_label.set(this.question.getLabel());
        }
    }


    public void actvm_btnCommentaryValid() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][method] Usage of the CommentsWindowsViewModel.actvm_btnCommentaryValid()");
        }

        if (!this.tfCommentary_value.isEmpty().get()) {
            CommentsViewModel commentsViewModel = new CommentsViewModel(this.tfCommentary_value.get());
            final ViewTuple<CommentsView, CommentsViewModel> commentsViewTuple = FluentViewLoader.fxmlView(CommentsView.class)
                    .viewModel(commentsViewModel)
                    .load();

            //listComments.add();
        }
    }


    /**
     * Property of the variable lblDebate_label.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link StringProperty} - return the property of the variable lblDebate_label.
     */
    public StringProperty lblDebate_labelProperty() {
        return lblDebate_label;
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
     * Property of the variable tfCommentary_value.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link StringProperty} - return the property of the variable tfCommentary_value.
     */
    public StringProperty tfCommentary_valueProperty() {
        return tfCommentary_value;
    }


    @Override
    public void onViewAdded_Cycle() {
    }

    @Override
    public void onViewRemoved_Cycle() {
    }
}
