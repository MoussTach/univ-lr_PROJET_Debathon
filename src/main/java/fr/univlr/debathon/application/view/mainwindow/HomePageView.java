package fr.univlr.debathon.application.view.mainwindow;

import de.saxsys.mvvmfx.InjectViewModel;
import fr.univlr.debathon.application.view.FxmlView_SceneCycle;
import fr.univlr.debathon.application.viewmodel.mainwindow.HomePageViewModel;
import fr.univlr.debathon.log.generate.CustomLogger;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.WeakListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import org.controlsfx.control.textfield.CustomTextField;

import java.net.URL;
import java.util.ResourceBundle;

public class HomePageView extends FxmlView_SceneCycle<HomePageViewModel> implements Initializable {

    private static final CustomLogger LOGGER = CustomLogger.create(HomePageView.class.getName());

    @FXML private TitledPane tPaneParameters;

    //Header organizer
    @FXML private Label lblOrganizer;
    @FXML private CheckBox chkShowCreatedDebate;
    @FXML private Button btnCreateNewDebate;

    //Header parameters
    @FXML private Button btnAddTag;
    @FXML private FlowPane flowTag;
    @FXML private CustomTextField tfSearch;

    @FXML private ScrollPane scrollPane;
    @FXML private FlowPane flowDebate;


    @InjectViewModel
    private HomePageViewModel homePageViewModel;


    @FXML
    private void act_btnCreateNewDebate() {
        LOGGER.input(String.format("Press the button %s", btnCreateNewDebate.getId()));
    }

    @FXML
    private void act_btnAddTag() {
        LOGGER.input(String.format("Press the button %s", btnAddTag.getId()));

        this.homePageViewModel.loadTag();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.setViewModel(homePageViewModel);

        //Text
        tPaneParameters.textProperty().bind(this.homePageViewModel.tPaneParameters_labelProperty());

        lblOrganizer.textProperty().bind(this.homePageViewModel.lblOrganizer_labelProperty());
        chkShowCreatedDebate.textProperty().bind(this.homePageViewModel.chkShowCreatedDebate_labelProperty());
        btnCreateNewDebate.textProperty().bind(this.homePageViewModel.btnCreateNewDebate_labelProperty());

        btnAddTag.textProperty().bind(this.homePageViewModel.btnAddTag_labelProperty());

        //Value
        chkShowCreatedDebate.selectedProperty().bindBidirectional(this.homePageViewModel.chkShowCreatedDebate_valueProperty());
        Platform.runLater(() ->
                this.homePageViewModel.listTag_selected_valueProperty().addListener(new WeakListChangeListener<>(change -> {
                    while (change.next()) {
                        if (change.wasAdded()) {
                            change.getAddedSubList().forEach(item -> Platform.runLater(() -> {
                                if (!flowTag.getChildren().contains(item.getView()))
                                    flowTag.getChildren().add(item.getView());
                            }));
                        } else if (change.wasRemoved()) {
                            change.getRemoved().forEach(item -> flowTag.getChildren().remove(item.getView()));
                        }
                    }
                })));
        tfSearch.textProperty().bindBidirectional(this.homePageViewModel.tfSearch_valueProperty());

        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        Platform.runLater(() ->
                this.homePageViewModel.listDebate_node_valueProperty().addListener(new WeakListChangeListener<>(change -> {
                    while (change.next()) {
                        if (change.wasAdded()) {
                            change.getAddedSubList().forEach(item -> Platform.runLater(() -> {
                                if (!flowDebate.getChildren().contains(item))
                                    flowDebate.getChildren().add(item);
                            }));
                        } else if (change.wasRemoved()) {
                            change.getRemoved().forEach(item -> Platform.runLater(() -> flowDebate.getChildren().remove(item)));
                        }
                    }
                })));

        Bindings.bindContent(flowDebate.getChildren(), this.homePageViewModel.getFilteredData());
    }

    @Override
    public void onViewAdded_Cycle() {
    }

    @Override
    public void onViewRemoved_Cycle() {
        //Text
        tPaneParameters.textProperty().unbind();

        lblOrganizer.textProperty().unbind();
        chkShowCreatedDebate.textProperty().unbind();
        btnCreateNewDebate.textProperty().unbind();

        btnAddTag.textProperty().unbind();

        //Value
        chkShowCreatedDebate.selectedProperty().unbindBidirectional(this.homePageViewModel.chkShowCreatedDebate_valueProperty());

        tfSearch.textProperty().unbindBidirectional(this.homePageViewModel.tfSearch_valueProperty());
    }
}
