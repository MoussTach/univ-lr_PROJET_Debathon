package fr.univlr.debathon.application.view.mainwindow;

import de.saxsys.mvvmfx.InjectViewModel;
import de.saxsys.mvvmfx.ViewTuple;
import fr.univlr.debathon.application.view.FxmlView_SceneCycle;
import fr.univlr.debathon.application.view.mainwindow.debate.items.CategoryView;
import fr.univlr.debathon.application.view.mainwindow.debate.items.TagView;
import fr.univlr.debathon.application.viewmodel.mainwindow.HomePageViewModel;
import fr.univlr.debathon.application.viewmodel.mainwindow.debate.items.CategoryViewModel;
import fr.univlr.debathon.application.viewmodel.mainwindow.debate.items.TagViewModel;
import fr.univlr.debathon.log.generate.CustomLogger;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import org.controlsfx.control.textfield.CustomTextField;

import java.net.URL;
import java.util.ResourceBundle;

public class HomePageView extends FxmlView_SceneCycle<HomePageViewModel> implements Initializable {

    private static final CustomLogger LOGGER = CustomLogger.create(HomePageView.class.getName());

    @FXML private BorderPane borderPane;
    @FXML private TitledPane tPaneParameters;
    @FXML private Button btnShowKey;

    //Header organizer
    @FXML private StackPane stackCreateDebate;
    @FXML private Label lblOrganizer;
    @FXML private Button btnCreateNewDebate;

    //Header parameters
    @FXML private Button btnAddItem;
    @FXML private FlowPane flowCategory;
    @FXML private FlowPane flowTag;
    @FXML private CustomTextField tfSearch;

    @FXML private ScrollPane scrollPane;
    @FXML private FlowPane flowDebate;

    private ChangeListener<String> changeListener_key;
    private ChangeListener<ViewTuple<CategoryView, CategoryViewModel> > changeListener_category;
    private ListChangeListener<ViewTuple<TagView, TagViewModel> > listChangeListener_tag;
    private ListChangeListener<Node> listChangeListener_DebateThumbnail;

    @InjectViewModel
    private HomePageViewModel homePageViewModel;

    @FXML
    private void act_showKey() {
        LOGGER.input(String.format("Press the button %s", btnShowKey.getId()));

        this.homePageViewModel.actvm_showKeyWindow(btnShowKey);
    }

    @FXML
    private void act_btnCreateNewDebate() {
        LOGGER.input(String.format("Press the button %s", btnCreateNewDebate.getId()));

        this.homePageViewModel.actvm_CreateNewDebate(btnCreateNewDebate);
    }

    @FXML
    private void act_btnAddItem() {
        LOGGER.input(String.format("Press the button %s", btnAddItem.getId()));

        this.homePageViewModel.actvm_createAddItem(btnAddItem);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.setViewModel(homePageViewModel);

        this.homePageViewModel.borderPaneProperty().set(borderPane);

        //Text
        tPaneParameters.textProperty().bind(this.homePageViewModel.tPaneParameters_labelProperty());

        lblOrganizer.textProperty().bind(this.homePageViewModel.lblOrganizer_labelProperty());
        btnCreateNewDebate.textProperty().bind(this.homePageViewModel.btnCreateNewDebate_labelProperty());

        btnAddItem.textProperty().bind(this.homePageViewModel.btnAddTag_labelProperty());

        //Value
        this.stackCreateDebate.setVisible(false);
        this.stackCreateDebate.setManaged(false);

        this.btnShowKey.setVisible(true);
        this.btnShowKey.setManaged(true);
        this.changeListener_key = (observableValue, oldValue, newValue) -> {
            boolean value = newValue == null || newValue.isEmpty();
            this.stackCreateDebate.setVisible(value);
            this.stackCreateDebate.setManaged(value);

            this.btnShowKey.setVisible(!value);
            this.btnShowKey.setManaged(!value);
        };
        this.homePageViewModel.key_valueProperty().addListener(this.changeListener_key);

        this.changeListener_category = (observableValue, oldValue, newValue) -> {
            if (oldValue != null)
                flowCategory.getChildren().remove(oldValue.getView());
            if (newValue != null)
                flowCategory.getChildren().add(newValue.getView());
        };
        this.homePageViewModel.category_selected_valueProperty().addListener(this.changeListener_category);

        this.homePageViewModel.listTag_selected_valueProperty().forEach(item -> flowTag.getChildren().add(item.getView()));
        this.listChangeListener_tag = change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().stream().filter(item -> !flowTag.getChildren().contains(item.getView())).forEach(item -> flowTag.getChildren().add(item.getView()));
                } else if (change.wasRemoved()) {
                    change.getRemoved().forEach(item -> flowTag.getChildren().remove(item.getView()));
                }
            }
        };
        this.homePageViewModel.listTag_selected_valueProperty().addListener(this.listChangeListener_tag);

        tfSearch.textProperty().bindBidirectional(this.homePageViewModel.tfSearch_valueProperty());

        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        this.homePageViewModel.listDebate_node_valueProperty().forEach(item -> flowTag.getChildren().add(item));
        this.listChangeListener_DebateThumbnail = change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().stream().filter(item -> !flowDebate.getChildren().contains(item)).forEach(item -> flowDebate.getChildren().add(item));
                } else if (change.wasRemoved()) {
                    change.getRemoved().forEach(item -> flowDebate.getChildren().remove(item));
                }
            }
        };
        this.homePageViewModel.listDebate_node_valueProperty().addListener(this.listChangeListener_DebateThumbnail);

        Bindings.bindContent(flowDebate.getChildren(), this.homePageViewModel.getFilteredData());
    }

    @Override
    public void onViewAdded_Cycle() {
    }

    @Override
    public void onViewRemoved_Cycle() {
        if (this.changeListener_category != null) {
            this.homePageViewModel.category_selected_valueProperty().removeListener(this.changeListener_category);
            this.changeListener_category = null;
        }

        if (this.listChangeListener_tag != null) {
            this.homePageViewModel.listTag_selected_valueProperty().removeListener(this.listChangeListener_tag);
            this.listChangeListener_tag = null;
        }

        if (this.listChangeListener_DebateThumbnail != null) {
            this.homePageViewModel.listDebate_node_valueProperty().removeListener(this.listChangeListener_DebateThumbnail);
            this.listChangeListener_DebateThumbnail = null;
        }

        //Text
        tPaneParameters.textProperty().unbind();

        lblOrganizer.textProperty().unbind();
        btnCreateNewDebate.textProperty().unbind();

        btnAddItem.textProperty().unbind();

        //Value
        tfSearch.textProperty().unbindBidirectional(this.homePageViewModel.tfSearch_valueProperty());
    }
}
