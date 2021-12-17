package fr.univlr.debathon.application.viewmodel.sidewindow.comments;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.ViewTuple;
import fr.univlr.debathon.application.communication.Debathon;
import fr.univlr.debathon.application.view.sidewindow.comments.CommentsView;
import fr.univlr.debathon.application.viewmodel.ViewModel_SceneCycle;
import fr.univlr.debathon.job.db_project.jobclass.Comment;
import fr.univlr.debathon.job.db_project.jobclass.Question;
import fr.univlr.debathon.job.db_project.jobclass.Room;
import fr.univlr.debathon.log.generate.CustomLogger;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

import java.util.Optional;

public class CommentsWindowsViewModel extends ViewModel_SceneCycle {

    private static final CustomLogger LOGGER = CustomLogger.create(CommentsWindowsViewModel.class.getName());

    private final Room debate;
    private final Question question;

    //Text
    private final StringProperty lblDebate_label = new SimpleStringProperty("/");
    private final StringProperty lblQuestion_label = new SimpleStringProperty("/");

    //Value
    private final StringProperty tfCommentary_value = new SimpleStringProperty("");
    private final ListProperty<ViewTuple<CommentsView, CommentsViewModel> > listComments_value = new SimpleListProperty<>(FXCollections.synchronizedObservableList(FXCollections.observableArrayList()));
    private final ListProperty<BorderPane> listComments_node = new SimpleListProperty<>(FXCollections.synchronizedObservableList(FXCollections.observableArrayList()));

    private ListChangeListener<Comment> changeListener_comment = null;

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

        this.bindCommentary();
    }


    private void bindCommentary() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[private][method] Usage of the CommentsWindowsViewModel.bindCommentary()");
        }

        if (this.debate != null) {
            this.lblDebate_label.set(this.debate.getLabel());
        }

        if (this.question != null) {
            this.lblQuestion_label.set(this.question.getLabel());

            this.question.listCommentProperty().forEach(comment ->
                    Platform.runLater(() -> {
                        if (comment != null) {
                            CommentsViewModel commentsViewModel = new CommentsViewModel(comment);
                            final ViewTuple<CommentsView, CommentsViewModel> commentViewTuple = FluentViewLoader.fxmlView(CommentsView.class)
                                    .viewModel(commentsViewModel)
                                    .load();

                            listComments_value.add(commentViewTuple);

                            BorderPane pane = new BorderPane();
                            pane.setId(String.valueOf(comment.getId()));
                            pane.setCenter(commentViewTuple.getView());

                            //Same user
                            if (Debathon.getInstance().getUser() != null
                                    && Debathon.getInstance().getUser().getId() == comment.getId()) {
                                ((StackPane) commentViewTuple.getView()).getChildren().get(0).setStyle(
                                        "-fx-background-radius:40;" +
                                                "-fx-border-color:RED;" +
                                                "-fx-border-radius:40;"
                                );
                            }
                            ((Label)((BorderPane)((StackPane) commentViewTuple.getView()).getChildren().get(0)).getCenter()).setMinHeight(Region.USE_PREF_SIZE);

                            pane.setMaxHeight(Double.MAX_VALUE);
                            pane.setMaxWidth(Double.MAX_VALUE);
                            pane.setStyle("-fx-vgrow-policy:always;");
                            listComments_node.add(pane);
                        }
                    }));
            this.changeListener_comment = change -> {
                while (change.next()) {
                    if (change.wasAdded()) {
                        change.getAddedSubList().forEach(item ->
                                Platform.runLater(() -> {
                                    if (item != null) {
                                        CommentsViewModel commentsViewModel = new CommentsViewModel(item);
                                        final ViewTuple<CommentsView, CommentsViewModel> commentViewTuple = FluentViewLoader.fxmlView(CommentsView.class)
                                                .viewModel(commentsViewModel)
                                                .load();

                                        this.listComments_value.add(commentViewTuple);

                                        BorderPane pane = new BorderPane();
                                        pane.setId(String.valueOf(item.getId()));
                                        pane.setCenter(commentViewTuple.getView());

                                        //Same user
                                        if (Debathon.getInstance().getUser() != null
                                                && Debathon.getInstance().getUser().getId() == item.getId()) {
                                            ((StackPane) commentViewTuple.getView()).getChildren().get(0).setStyle(
                                                    "-fx-background-radius:40;" +
                                                            "-fx-border-color:RED;" +
                                                            "-fx-border-radius:40;"
                                            );
                                        }
                                        pane.setMaxHeight(Double.MAX_VALUE);
                                        pane.setMaxWidth(Double.MAX_VALUE);
                                        pane.setStyle("-fx-vgrow-policy:always;");
                                        listComments_node.add(pane);
                                    }
                                })
                        );
                    } else if (change.wasRemoved()) {
                        change.getRemoved().forEach(item ->
                                Platform.runLater(() -> {
                                    Optional<ViewTuple<CommentsView, CommentsViewModel>> optional = listComments_value.stream().filter(comment -> comment.getViewModel().getComment().equals(item)).findAny();
                                    if (optional.isPresent()) {
                                        listComments_node.removeIf(borderPane -> borderPane.getId().equals(String.valueOf(optional.get().getViewModel().getComment().getId())));
                                        listComments_value.remove(optional.get());
                                    }
                                })
                        );
                    }
                }
            };
            this.question.listCommentProperty().addListener(this.changeListener_comment);
        }
    }


    public void actvm_btnCommentaryValid() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][method] Usage of the CommentsWindowsViewModel.actvm_btnCommentaryValid()");
        }
        try {
            if (!this.tfCommentary_value.isEmpty().get()) {
                Comment newCom = new Comment(this.tfCommentary_value.get(), null, this.question, this.debate, Debathon.getInstance().getUser());

                Debathon.getInstance().getAppCommunication().requestInsertNewComment(newCom);
            }
        } catch (JsonProcessingException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Error when sending a new comment", e);
            }
        }
        this.tfCommentary_value.set("");
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

    /**
     * Property of the variable listComments_value.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link ListProperty} - return the property of the variable listComments_value.
     */
    public ListProperty<ViewTuple<CommentsView, CommentsViewModel>> listComments_valueProperty() {
        return listComments_value;
    }

    /**
     * Property of the variable listComments_node.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link ListProperty} - return the property of the variable listComments_node.
     */
    public ListProperty<BorderPane> listComments_nodeProperty() {
        return listComments_node;
    }


    @Override
    public void onViewAdded_Cycle() {
    }

    @Override
    public void onViewRemoved_Cycle() {
    }
}
