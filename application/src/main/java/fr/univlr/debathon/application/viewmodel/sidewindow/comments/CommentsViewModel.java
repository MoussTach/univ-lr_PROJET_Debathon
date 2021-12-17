package fr.univlr.debathon.application.viewmodel.sidewindow.comments;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.univlr.debathon.application.communication.Debathon;
import fr.univlr.debathon.application.viewmodel.ViewModel_SceneCycle;
import fr.univlr.debathon.job.db_project.jobclass.Comment;
import fr.univlr.debathon.log.generate.CustomLogger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CommentsViewModel extends ViewModel_SceneCycle {

    private static final CustomLogger LOGGER = CustomLogger.create(CommentsViewModel.class.getName());

    private final Comment comment;

    private final StringProperty lblCommentary_label = new SimpleStringProperty();


    public CommentsViewModel(Comment commentary) {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][constructor] Creation of the CommentsViewModel() object.");
        }

        this.comment = commentary;
        this.bindCommentary();
    }

    private void bindCommentary() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[private][method] Usage of the CommentsWindowsViewModel.bindCommentary()");
        }

        if (this.comment != null) {
            this.lblCommentary_label.bind(this.comment.commentProperty());
        }
    }

    public void actvm_like() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][method] Usage of the CommentsViewModel.actvm_like()");
        }

        try {
            Debathon.getInstance().getAppCommunication().methodsUPDATE_LIKE_COMMENT(this.comment.getId(), true);
        } catch (JsonProcessingException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Error when liking", e);
            }
        }
        this.comment.setNb_likes(-1);
    }

    public void actvm_dislike() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][method] Usage of the CommentsViewModel.actvm_dislike()");
        }

        try {
            Debathon.getInstance().getAppCommunication().methodsUPDATE_LIKE_COMMENT(this.comment.getId(), false);
        } catch (JsonProcessingException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Error when disliking", e);
            }
        }
        this.comment.setNb_dislikes(-1);
    }

    /**
     * Property of the variable lblCommentary_label.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link StringProperty} - return the property of the variable lblCommentary_label.
     */
    public StringProperty lblCommentary_labelProperty() {
        return lblCommentary_label;
    }

    /**
     * Getter for the variable comment.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link Comment} - return the variable comment.
     */
    public Comment getComment() {
        return comment;
    }


    @Override
    public void onViewAdded_Cycle() {
    }

    @Override
    public void onViewRemoved_Cycle() {
    }
}
