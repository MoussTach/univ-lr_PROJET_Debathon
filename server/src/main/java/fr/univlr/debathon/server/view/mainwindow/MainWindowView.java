package fr.univlr.debathon.server.view.mainwindow;

import de.saxsys.mvvmfx.InjectViewModel;
import fr.univlr.debathon.log.generate.CustomLogger;
import fr.univlr.debathon.server.Launch;
import fr.univlr.debathon.server.view.FxmlView_SceneCycle;
import fr.univlr.debathon.server.viewmodel.mainwindow.MainWindowViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * View of the main window.
 *
 * @author Gaetan Brenckle
 */
public class MainWindowView extends FxmlView_SceneCycle<MainWindowViewModel> implements Initializable {
    private static final CustomLogger LOGGER = CustomLogger.create(MainWindowView.class.getName());

    @FXML private BorderPane bPaneMainView;
    @FXML private TextField tfKey;

    @InjectViewModel
    private MainWindowViewModel mainWindowViewModel;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.setViewModel(mainWindowViewModel);

        //Value
        this.tfKey.textProperty().bind(this.mainWindowViewModel.tfKey_valueProperty());

        Launch.PRIMARYSTAGE.setOnCloseRequest(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initOwner(Launch.PRIMARYSTAGE);
            alert.initModality(Modality.APPLICATION_MODAL);

            alert.setHeaderText("Do you want to stop the server ?");

            alert.getDialogPane().getButtonTypes().setAll(ButtonType.NO, ButtonType.YES);
            Optional<ButtonType> optional = alert.showAndWait();

            if (optional.isPresent() && optional.get() == ButtonType.YES) {
                return;
            }
            event.consume();
        });

    }


    @Override
    public void onViewAdded_Cycle() {
        // Default implementation
    }

    @Override
    public void onViewRemoved_Cycle() {
        this.tfKey.textProperty().unbind();
    }
}
