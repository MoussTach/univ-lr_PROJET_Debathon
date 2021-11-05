package fr.univlr.debathon.application.view;

import de.saxsys.mvvmfx.InjectViewModel;
import fr.univlr.debathon.application.Launch;
import fr.univlr.debathon.application.viewmodel.PreloadWindowViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Preload class
 *
 * @author Gaetan Brenckle
 */
public class PreloadWindowView extends FxmlView_SceneCycle<PreloadWindowViewModel> implements Initializable {

    @FXML
    public ProgressBar progressBar_Preloader;

    @InjectViewModel
    private PreloadWindowViewModel preloadWindowViewModel;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.setViewModel(preloadWindowViewModel);

        progressBar_Preloader.progressProperty().bind(Launch.progressLaunching);
    }


    @Override
    public void onViewAdded_Cycle() {
        // default implementation
    }

    @Override
    public void onViewRemoved_Cycle() {
        progressBar_Preloader.progressProperty().unbind();
    }
}
