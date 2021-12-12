package fr.univlr.debathon.application.view.sidewindow.comments;

import de.saxsys.mvvmfx.InjectViewModel;
import fr.univlr.debathon.application.view.FxmlView_SceneCycle;
import fr.univlr.debathon.application.view.mainwindow.debate.InscriptionStatView;
import fr.univlr.debathon.application.viewmodel.mainwindow.debate.DebateThumbnailViewModel;
import fr.univlr.debathon.application.viewmodel.sidewindow.comments.CommentsWindowsViewModel;
import fr.univlr.debathon.log.generate.CustomLogger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.FlowPane;

import java.net.URL;
import java.util.ResourceBundle;

public class CommentsWindowsView extends FxmlView_SceneCycle<CommentsWindowsViewModel> implements Initializable {

    private static final CustomLogger LOGGER = CustomLogger.create(CommentsWindowsView.class.getName());

    @FXML private Label lblDebate;
    @FXML private Label lblQuestion;

    @FXML private FlowPane flowComments;

    @FXML private TextArea tfCommentary;
    @FXML private Button btnCommentaryValid;

    @InjectViewModel
    private CommentsWindowsViewModel commentsWindowsViewModel;


    @FXML
    public void act_btnCommentaryValid() {
        LOGGER.input(String.format("Press the button %s", btnCommentaryValid.getId()));

        this.commentsWindowsViewModel.actvm_btnCommentaryValid();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.setViewModel(commentsWindowsViewModel);

        //Text
        this.lblDebate.textProperty().bind(this.commentsWindowsViewModel.lblDebate_labelProperty());
        this.lblQuestion.textProperty().bind(this.commentsWindowsViewModel.lblQuestion_labelProperty());

        //Value
        this.tfCommentary.textProperty().bindBidirectional(this.commentsWindowsViewModel.tfCommentary_valueProperty());
    }


    @Override
    public void onViewAdded_Cycle() {
    }

    @Override
    public void onViewRemoved_Cycle() {
        //Text
        this.lblDebate.textProperty().unbind();
        this.lblQuestion.textProperty().unbind();

        //Value
        this.tfCommentary.textProperty().unbindBidirectional(this.commentsWindowsViewModel.tfCommentary_valueProperty());
    }
}
