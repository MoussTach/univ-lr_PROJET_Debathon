package fr.univlr.debathon.application.view.mainwindow.debate.question;

import de.saxsys.mvvmfx.InjectViewModel;
import fr.univlr.debathon.application.view.FxmlView_SceneCycle;
import fr.univlr.debathon.application.viewmodel.mainwindow.debate.question.ResponseViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class ResponseView extends FxmlView_SceneCycle<ResponseViewModel> implements Initializable {

    @FXML private BorderPane borderPane;
    @FXML private Label lblResponse;

    @InjectViewModel
    private ResponseViewModel responseViewModel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.setViewModel(responseViewModel);

        //TODO need response Model
        Node nodeResponse = null;
        if (true) {
            nodeResponse = new RadioButton();
            ((RadioButton) nodeResponse).setToggleGroup(this.responseViewModel.getGroup());
        } else if (true) {
            nodeResponse = new CheckBox();
        }

        if (nodeResponse != null) {
            BorderPane.setMargin(nodeResponse, new Insets(0, 10, 0, 0));
            borderPane.setLeft(nodeResponse);
        }

        //Text
        this.lblResponse.textProperty().bind(this.responseViewModel.lblResponse_labelProperty());

        //Value
    }


    @Override
    public void onViewAdded_Cycle() {
    }

    @Override
    public void onViewRemoved_Cycle() {
        //Text
        this.lblResponse.textProperty().unbind();

        //Value
    }
}
