package fr.univlr.debathon.application.view.sidewindow.comments;

import de.saxsys.mvvmfx.InjectViewModel;
import fr.univlr.debathon.application.view.FxmlView_SceneCycle;
import fr.univlr.debathon.application.viewmodel.sidewindow.comments.CommentsViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class CommentsView extends FxmlView_SceneCycle<CommentsViewModel> implements Initializable {

    @FXML private Label lblCommentary;

    @InjectViewModel
    private CommentsViewModel commentsViewModel;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.setViewModel(commentsViewModel);

        //Text
        this.lblCommentary.textProperty().set(this.commentsViewModel.getLblCommentary_label());
    }


    @Override
    public void onViewAdded_Cycle() {
    }

    @Override
    public void onViewRemoved_Cycle() {
    }
}
