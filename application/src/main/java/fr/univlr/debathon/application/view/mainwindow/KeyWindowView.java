package fr.univlr.debathon.application.view.mainwindow;

import de.saxsys.mvvmfx.InjectViewModel;
import fr.univlr.debathon.application.view.FxmlView_SceneCycle;
import fr.univlr.debathon.application.viewmodel.mainwindow.KeyWindowViewModel;
import fr.univlr.debathon.log.generate.CustomLogger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class KeyWindowView extends FxmlView_SceneCycle<KeyWindowViewModel> implements Initializable {

    private static final CustomLogger LOGGER = CustomLogger.create(KeyWindowView.class.getName());

    @FXML private BorderPane borderPane_key;
    @FXML private Label lblKey;
    @FXML private TextField tfKey;
    @FXML private Button btnValidKey;

    @InjectViewModel
    private KeyWindowViewModel keyWindowViewModel;

    @FXML
    public void act_btnValidKey() {
        LOGGER.input(String.format("Press the button %s", btnValidKey.getId()));

        this.keyWindowViewModel.actvm_btnValidKey();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.setViewModel(keyWindowViewModel);


        //Text
        this.lblKey.textProperty().bind(this.keyWindowViewModel.lblKey_labelProperty());

        //Value
        this.keyWindowViewModel.tfKey_valueProperty().bind(this.tfKey.textProperty());
    }

    @Override
    public void onViewAdded_Cycle() {
    }

    @Override
    public void onViewRemoved_Cycle() {
    }
}
