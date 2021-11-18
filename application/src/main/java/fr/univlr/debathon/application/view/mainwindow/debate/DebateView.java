package fr.univlr.debathon.application.view.mainwindow.debate;

import de.saxsys.mvvmfx.InjectViewModel;
import fr.univlr.debathon.application.view.FxmlView_SceneCycle;
import fr.univlr.debathon.application.viewmodel.mainwindow.debate.DebateThumbnailViewModel;
import fr.univlr.debathon.application.viewmodel.mainwindow.debate.DebateViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;

import java.net.URL;
import java.util.ResourceBundle;

public class DebateView extends FxmlView_SceneCycle<DebateViewModel> implements Initializable {

    @FXML private Label lblTitle;

    @FXML private FlowPane flowQuestion;

    @InjectViewModel
    private DebateViewModel debateViewModel;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.setViewModel(debateViewModel);

        //Text
        //Value
        this.lblTitle.textProperty().bind(this.debateViewModel.lblTitle_valueProperty());
    }


    @Override
    public void onViewAdded_Cycle() {
    }

    @Override
    public void onViewRemoved_Cycle() {

        //Text
        //Value
        this.lblTitle.textProperty().unbind();
    }
}
