package fr.univlr.debathon.application.view.mainwindow.debate;

import de.saxsys.mvvmfx.InjectViewModel;
import fr.univlr.debathon.application.view.FxmlView_SceneCycle;
import fr.univlr.debathon.application.view.mainwindow.HomePageView;
import fr.univlr.debathon.application.viewmodel.mainwindow.HomePageViewModel;
import fr.univlr.debathon.application.viewmodel.mainwindow.debate.DebateThumbnailViewModel;
import fr.univlr.debathon.log.generate.CustomLogger;
import javafx.application.Platform;
import javafx.collections.WeakListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;

import java.net.URL;
import java.util.ResourceBundle;

public class DebateThumbnailView extends FxmlView_SceneCycle<DebateThumbnailViewModel> implements Initializable {

    private static final CustomLogger LOGGER = CustomLogger.create(DebateThumbnailView.class.getName());

    @FXML private Label lblTitle;
    @FXML private FlowPane flowTag;
    @FXML private Label lblNbPeople;

    @FXML private Button btnOpenDebate;

    @InjectViewModel
    private DebateThumbnailViewModel debateThumbnailViewModel;


    @FXML
    public void act_btnOpenDebate() {
        LOGGER.input(String.format("Press the button %s", btnOpenDebate.getId()));

        System.out.println(String.format("Open debate [%s]", lblTitle.getText()));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.setViewModel(debateThumbnailViewModel);

        //Value
        this.lblTitle.textProperty().bind(this.debateThumbnailViewModel.lblTitle_valueProperty());
        this.debateThumbnailViewModel.listTag_selected_valueProperty().forEach(item -> flowTag.getChildren().add(item.getView()));
        Platform.runLater(() ->
                this.debateThumbnailViewModel.listTag_selected_valueProperty().addListener(new WeakListChangeListener<>(change -> {
                    while (change.next()) {
                        if (change.wasAdded()) {
                            change.getAddedSubList().forEach(item -> Platform.runLater(() -> flowTag.getChildren().add(item.getView())));
                        } else if (change.wasRemoved()) {
                            change.getRemoved().forEach(item -> flowTag.getChildren().remove(item.getView()));
                        }
                    }
                })));
        this.lblNbPeople.textProperty().bind(this.debateThumbnailViewModel.lblNbPeople_valueProperty());
    }


    @Override
    public void onViewAdded_Cycle() {
    }

    @Override
    public void onViewRemoved_Cycle() {
    }
}
