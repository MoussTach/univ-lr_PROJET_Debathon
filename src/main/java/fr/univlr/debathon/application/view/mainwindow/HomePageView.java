package fr.univlr.debathon.application.view.mainwindow;

import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.InjectViewModel;
import de.saxsys.mvvmfx.ViewTuple;
import fr.univlr.debathon.application.view.FxmlView_SceneCycle;
import fr.univlr.debathon.application.view.mainwindow.debate.items.TagView;
import fr.univlr.debathon.application.viewmodel.mainwindow.HomePageViewModel;
import fr.univlr.debathon.application.viewmodel.mainwindow.MainWindowViewModel;
import fr.univlr.debathon.application.viewmodel.mainwindow.debate.items.TagViewModel;
import fr.univlr.debathon.log.generate.CustomLogger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import org.controlsfx.control.textfield.CustomTextField;

import java.net.URL;
import java.util.ResourceBundle;

public class HomePageView extends FxmlView_SceneCycle<HomePageViewModel> implements Initializable {

    private static final CustomLogger LOGGER = CustomLogger.create(HomePageView.class.getName());

    //Header organizer
    @FXML private Label lblOrganizer;
    @FXML private CheckBox chkShowCreatedDebate;
    @FXML private Button btnCreateNewDebate;

    //Header parameters
    @FXML private Button btnAddTag;
    @FXML private FlowPane flowTag;
    @FXML private CustomTextField tfSearch;

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
        lblOrganizer.textProperty().bind(this.homePageViewModel.lblOrganizer_labelProperty());
        chkShowCreatedDebate.textProperty().bind(this.homePageViewModel.chkShowCreatedDebate_labelProperty());
        btnCreateNewDebate.textProperty().bind(this.homePageViewModel.btnCreateNewDebate_labelProperty());

        btnAddTag.textProperty().bind(this.homePageViewModel.btnAddTag_labelProperty());

        //Value
        chkShowCreatedDebate.selectedProperty().bindBidirectional(this.homePageViewModel.chkShowCreatedDebate_valueProperty());
        this.homePageViewModel.listTag_selected_valueProperty().addListener(new WeakListChangeListener<>(change -> {
            while (change.next()) {
                //TODO
                System.out.println("[CHANGED] > list tag");
                if (change.wasAdded()) {
                    change.getAddedSubList().forEach(item -> Platform.runLater(() -> flowTag.getChildren().add(item.getView())));
                } else if (change.wasRemoved()) {
                    change.getRemoved().forEach(item -> flowTag.getChildren().remove(item.getView()));
                }
            }
        }));
        tfSearch.textProperty().bindBidirectional(this.homePageViewModel.tfSearch_valueProperty());
    }

    @Override
    public void onViewAdded_Cycle() {
    }

    @Override
    public void onViewRemoved_Cycle() {
        //Text
        lblOrganizer.textProperty().unbind();
        chkShowCreatedDebate.textProperty().unbind();
        btnCreateNewDebate.textProperty().unbind();

        btnAddTag.textProperty().unbind();

        //Value
        chkShowCreatedDebate.selectedProperty().unbindBidirectional(this.homePageViewModel.chkShowCreatedDebate_valueProperty());

        tfSearch.textProperty().unbindBidirectional(this.homePageViewModel.tfSearch_valueProperty());
    }
}
