package fr.univlr.debathon.application.view.sidewindow.comments;

import de.saxsys.mvvmfx.InjectViewModel;
import de.saxsys.mvvmfx.ViewTuple;
import fr.univlr.debathon.application.view.FxmlView_SceneCycle;
import fr.univlr.debathon.application.viewmodel.sidewindow.comments.CommentsViewModel;
import fr.univlr.debathon.application.viewmodel.sidewindow.comments.CommentsWindowsViewModel;
import fr.univlr.debathon.log.generate.CustomLogger;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class CommentsWindowsView extends FxmlView_SceneCycle<CommentsWindowsViewModel> implements Initializable {

    private static final CustomLogger LOGGER = CustomLogger.create(CommentsWindowsView.class.getName());

    @FXML private Label lblDebate;
    @FXML private Label lblQuestion;

    @FXML private ScrollPane scrollPane;
    @FXML private VBox vBoxComments;

    @FXML private TextArea tfCommentary;
    @FXML private Button btnCommentaryValid;

    @InjectViewModel
    private CommentsWindowsViewModel commentsWindowsViewModel;

    private ListChangeListener<BorderPane> listChangeListener_comments;

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

        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        this.commentsWindowsViewModel.listComments_nodeProperty().forEach(item -> vBoxComments.getChildren().add(item));
        this.listChangeListener_comments = change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().stream().filter(item -> !vBoxComments.getChildren().contains(item)).forEach(item -> vBoxComments.getChildren().add(item));
                } else if (change.wasRemoved()) {
                    change.getRemoved().forEach(item -> vBoxComments.getChildren().remove(item));
                }
            }
        };
        this.commentsWindowsViewModel.listComments_nodeProperty().addListener(this.listChangeListener_comments);
    }


    @Override
    public void onViewAdded_Cycle() {
    }

    @Override
    public void onViewRemoved_Cycle() {
    }
}
