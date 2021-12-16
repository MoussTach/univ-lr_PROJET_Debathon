package fr.univlr.debathon.application.view.mainwindow.debate.question;

import de.saxsys.mvvmfx.InjectViewModel;
import fr.univlr.debathon.application.view.FxmlView_SceneCycle;
import fr.univlr.debathon.application.viewmodel.mainwindow.debate.question.ResponseViewModel;
import fr.univlr.debathon.job.db_project.jobclass.Question;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.TextAlignment;

import java.net.URL;
import java.util.ResourceBundle;

public class ResponseView extends FxmlView_SceneCycle<ResponseViewModel> implements Initializable {

    @FXML private BorderPane borderPane;

    private ButtonBase nodeResponse;

    @InjectViewModel
    private ResponseViewModel responseViewModel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.setViewModel(responseViewModel);

        if (this.responseViewModel.getQuestionView().getQuestion().getType().equals(Question.Type.UNIQUE.text)) {
            nodeResponse = new RadioButton();
            ((RadioButton) nodeResponse).setToggleGroup(this.responseViewModel.getGroup());
            this.responseViewModel.responseValueProperty().bind(((RadioButton) nodeResponse).selectedProperty());
            ((RadioButton) nodeResponse).setSelected(this.responseViewModel.getResponse().getNb_votes() < 0);

        } else if (this.responseViewModel.getQuestionView().getQuestion().getType().equals(Question.Type.MULTIPLE.text)) {
            nodeResponse = new CheckBox();
            this.responseViewModel.responseValueProperty().bind(((CheckBox) nodeResponse).selectedProperty());
            ((CheckBox) nodeResponse).setSelected(this.responseViewModel.getResponse().getNb_votes() < 0);
        } //Libre

        if (nodeResponse != null) {
            nodeResponse.textProperty().bind(this.responseViewModel.lblResponse_labelProperty());

            BorderPane.setMargin(nodeResponse, new Insets(0, 10, 0, 0));
            nodeResponse.setTextAlignment(TextAlignment.CENTER);
            nodeResponse.setMaxWidth(Double.MAX_VALUE);
            nodeResponse.setAlignment(Pos.CENTER_LEFT);
            borderPane.setCenter(nodeResponse);
        }

        //Text

        //Value
    }


    @Override
    public void onViewAdded_Cycle() {
    }

    @Override
    public void onViewRemoved_Cycle() {
        //Text
        if (nodeResponse != null) {
            this.nodeResponse.textProperty().unbind();
        }

        //Value
        this.responseViewModel.responseValueProperty().unbind();
    }
}
