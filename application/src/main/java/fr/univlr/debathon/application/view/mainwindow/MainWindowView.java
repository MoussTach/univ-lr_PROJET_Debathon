package fr.univlr.debathon.application.view.mainwindow;

import de.saxsys.mvvmfx.InjectViewModel;
import fr.univlr.debathon.application.Launch;
import fr.univlr.debathon.application.view.FxmlView_SceneCycle;
import fr.univlr.debathon.application.viewmodel.mainwindow.MainWindowViewModel;
import fr.univlr.debathon.language.LanguageBundle;
import fr.univlr.debathon.log.generate.CustomLogger;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import org.controlsfx.control.StatusBar;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * View of the main window.
 *
 * @author Gaetan Brenckle
 */
public class MainWindowView extends FxmlView_SceneCycle<MainWindowViewModel> implements Initializable {

    private final ObjectProperty<ResourceBundle> resBundleWindow_ = LanguageBundle.getInstance().bindResourceBundle("properties.language.lg_window");
    private static final CustomLogger LOGGER = CustomLogger.create(MainWindowView.class.getName());

    @FXML private Button btnPrev;
    @FXML private Button btnHome;

    @FXML
    public BorderPane bPaneMainView;

    @FXML
    public StatusBar statusBarMainView;

    @InjectViewModel
    private MainWindowViewModel mainWindowViewModel;


    /**
     * action of the button btnPrev when pressed.
     * Charge to the previous view.
     *
     * @author Gaetan Brenckle
     */
    @FXML
    public void act_btnPrev() {
        LOGGER.input(String.format("Press the button %s", btnPrev.getId()));

        this.mainWindowViewModel.actvm_btnPrev();
    }

    /**
     * action of the button btnHome when pressed.
     * Charge the home view.
     *
     * @author Gaetan Brenckle
     */
    @FXML
    public void act_btnHome() {
        LOGGER.input(String.format("Press the button %s", btnHome.getId()));

        this.mainWindowViewModel.actvm_btnHome();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.setViewModel(mainWindowViewModel);

        //Value
        mainWindowViewModel.setbPaneMainProperty(bPaneMainView);
        mainWindowViewModel.bindProgressProperty(statusBarMainView.progressProperty());
        mainWindowViewModel.bindProgress_labelProperty(statusBarMainView.textProperty());

        Launch.PRIMARYSTAGE.setOnCloseRequest(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initOwner(Launch.PRIMARYSTAGE);
            alert.initModality(Modality.APPLICATION_MODAL);

            alert.setHeaderText(this.resBundleWindow_.get().getString("application_Exit_Header"));

            alert.getDialogPane().getButtonTypes().setAll(ButtonType.NO, ButtonType.YES);
            Optional<ButtonType> optional = alert.showAndWait();

            if (optional.isPresent() && optional.get() == ButtonType.YES) {
                return;
            }
            event.consume();
        });

        this.btnPrev.disableProperty().bind(this.mainWindowViewModel.isPrevCommandExecutableProperty());
        this.btnHome.disableProperty().bind(this.mainWindowViewModel.isHomeCommandExecutableProperty());
    }


    @Override
    public void onViewAdded_Cycle() {
        // Default implementation
    }

    @Override
    public void onViewRemoved_Cycle() {
        //Value
        statusBarMainView.progressProperty().unbind();
        statusBarMainView.textProperty().unbind();

        this.btnPrev.disableProperty().unbind();
        this.btnHome.disableProperty().unbind();
    }
}
