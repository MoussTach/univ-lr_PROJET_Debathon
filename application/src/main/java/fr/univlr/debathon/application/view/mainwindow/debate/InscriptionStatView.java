package fr.univlr.debathon.application.view.mainwindow.debate;

import de.saxsys.mvvmfx.InjectViewModel;
import fr.univlr.debathon.application.view.FxmlView_SceneCycle;
import fr.univlr.debathon.application.view.mainwindow.debate.question.QuestionView;
import fr.univlr.debathon.application.viewmodel.mainwindow.debate.InscriptionStatViewModel;
import fr.univlr.debathon.application.viewmodel.mainwindow.debate.question.ResponseViewModel;
import fr.univlr.debathon.log.generate.CustomLogger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class InscriptionStatView extends FxmlView_SceneCycle<InscriptionStatViewModel> implements Initializable {

    private static final CustomLogger LOGGER = CustomLogger.create(InscriptionStatView.class.getName());

    @FXML private Label lblSendMail;
    @FXML private Label lblIrreversible;

    @FXML private TextField tfMail;

    @FXML private Button btnValid;

    @InjectViewModel
    private InscriptionStatViewModel inscriptionStatViewModel;

    @FXML
    public void act_btnValid() {
        LOGGER.input(String.format("Press the button %s", btnValid.getId()));

        this.inscriptionStatViewModel.actvm_btnValid();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.setViewModel(inscriptionStatViewModel);

        //Text
        this.lblSendMail.textProperty().bind(this.inscriptionStatViewModel.lblSendMail_labelProperty());
        this.lblIrreversible.textProperty().bind(this.inscriptionStatViewModel.lblIrreversible_labelProperty());

        //Value
        this.inscriptionStatViewModel.tfMail_valueProperty().bind(this.tfMail.textProperty());
    }


    @Override
    public void onViewAdded_Cycle() {
    }

    @Override
    public void onViewRemoved_Cycle() {
        //Text
        this.lblSendMail.textProperty().unbind();
        this.lblIrreversible.textProperty().unbind();

        //Value
        this.inscriptionStatViewModel.tfMail_valueProperty().unbind();
    }
}
