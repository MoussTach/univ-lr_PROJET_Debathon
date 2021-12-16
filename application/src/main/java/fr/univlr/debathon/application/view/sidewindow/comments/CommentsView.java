package fr.univlr.debathon.application.view.sidewindow.comments;

import de.saxsys.mvvmfx.InjectViewModel;
import fr.univlr.debathon.application.view.FxmlView_SceneCycle;
import fr.univlr.debathon.application.viewmodel.sidewindow.comments.CommentsViewModel;
import fr.univlr.debathon.log.generate.CustomLogger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class CommentsView extends FxmlView_SceneCycle<CommentsViewModel> implements Initializable {

    private static final CustomLogger LOGGER = CustomLogger.create(CommentsView.class.getName());

    @FXML private Label lblCommentary;
    @FXML private Button btnLike;
    @FXML private Button btnDislike;

    @InjectViewModel
    private CommentsViewModel commentsViewModel;

    @FXML
    private void act_btnLike() {
        LOGGER.input(String.format("Press the button %s", btnLike.getId()));

        this.commentsViewModel.actvm_like();
        this.btnLike.setDisable(true);
    }

    @FXML
    private void act_btnDislike() {
        LOGGER.input(String.format("Press the button %s", btnDislike.getId()));

        this.commentsViewModel.actvm_dislike();
        this.btnDislike.setDisable(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.setViewModel(commentsViewModel);

        //Text
        this.lblCommentary.textProperty().bind(this.commentsViewModel.lblCommentary_labelProperty());

        //Value
        this.btnLike.setDisable(this.commentsViewModel.getComment().getNb_likes() < 0);
        this.btnDislike.setDisable(this.commentsViewModel.getComment().getNb_dislikes() < 0);
    }


    @Override
    public void onViewAdded_Cycle() {
    }

    @Override
    public void onViewRemoved_Cycle() {
        //Text
        this.lblCommentary.textProperty().unbind();
    }
}
