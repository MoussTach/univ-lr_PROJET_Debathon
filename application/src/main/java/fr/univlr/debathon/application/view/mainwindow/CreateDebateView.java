package fr.univlr.debathon.application.view.mainwindow;

import de.saxsys.mvvmfx.InjectViewModel;
import de.saxsys.mvvmfx.ViewTuple;
import fr.univlr.debathon.application.view.FxmlView_SceneCycle;
import fr.univlr.debathon.application.view.mainwindow.debate.items.CategoryView;
import fr.univlr.debathon.application.view.mainwindow.debate.items.TagView;
import fr.univlr.debathon.application.viewmodel.mainwindow.CreateDebateViewModel;
import fr.univlr.debathon.application.viewmodel.mainwindow.debate.items.CategoryViewModel;
import fr.univlr.debathon.application.viewmodel.mainwindow.debate.items.TagViewModel;
import fr.univlr.debathon.log.generate.CustomLogger;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.web.HTMLEditor;

import java.net.URL;
import java.util.ResourceBundle;

public class CreateDebateView extends FxmlView_SceneCycle<CreateDebateViewModel> implements Initializable {

    private static final CustomLogger LOGGER = CustomLogger.create(CreateDebateView.class.getName());

    @FXML private TitledPane titledCreate;

    @FXML private Label lblTitle;
    @FXML private TextField tfTitle;

    @FXML private FlowPane flowCategory;
    @FXML private FlowPane flowTag;
    @FXML private Button btnAddItem;

    @FXML private Label lblDescription;
    @FXML private HTMLEditor htmlEditorDescription;

    @FXML private Label lblKey;
    @FXML private TextField tfKey;

    @FXML private Button btnValid;

    @InjectViewModel
    private CreateDebateViewModel createDebateViewModel;

    private ChangeListener<ViewTuple<CategoryView, CategoryViewModel>> changeListener_category;
    private ListChangeListener<ViewTuple<TagView, TagViewModel> > listChangeListener_tag;

    @FXML
    private void act_btnAddItem() {
        LOGGER.input(String.format("Press the button %s", btnAddItem.getId()));

        this.createDebateViewModel.actvm_AddItem(btnAddItem);
    }

    @FXML
    private void act_btnValid() {
        LOGGER.input(String.format("Press the button %s", btnValid.getId()));

        //htmlEditor doesn't have a binding
        this.createDebateViewModel.htmlEditorDescription_valueProperty().set(this.htmlEditorDescription.getHtmlText());

        this.createDebateViewModel.actvm_ValidCreateDebate();

        this.htmlEditorDescription.setHtmlText("");
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.setViewModel(createDebateViewModel);

        //Text
        this.titledCreate.textProperty().bind(this.createDebateViewModel.titledCreate_labelProperty());
        this.lblTitle.textProperty().bindBidirectional(this.createDebateViewModel.lblTitle_labelProperty());
        this.btnAddItem.textProperty().bind(this.createDebateViewModel.btnAddItem_labelProperty());
        this.lblDescription.textProperty().bind(this.createDebateViewModel.lblDescription_labelProperty());
        this.btnValid.textProperty().bind(this.createDebateViewModel.btnValid_labelProperty());

        //Value
        this.tfTitle.textProperty().bindBidirectional(this.createDebateViewModel.tfTitle_valueProperty());
        this.changeListener_category = (observableValue, oldValue, newValue) -> {
            if (oldValue != null)
                flowCategory.getChildren().remove(oldValue.getView());
            if (newValue != null)
                flowCategory.getChildren().add(newValue.getView());
        };
        this.createDebateViewModel.category_selected_valueProperty().addListener(this.changeListener_category);

        this.createDebateViewModel.listTag_selected_valueProperty().forEach(item -> flowTag.getChildren().add(item.getView()));
        this.listChangeListener_tag = change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().stream().filter(item -> !flowTag.getChildren().contains(item.getView())).forEach(item -> flowTag.getChildren().add(item.getView()));
                } else if (change.wasRemoved()) {
                    change.getRemoved().forEach(item -> flowTag.getChildren().remove(item.getView()));
                }
            }
        };
        this.createDebateViewModel.listTag_selected_valueProperty().addListener(this.listChangeListener_tag);
    }


    @Override
    public void onViewAdded_Cycle() {
    }

    @Override
    public void onViewRemoved_Cycle() {
        if (this.changeListener_category != null) {
            this.createDebateViewModel.category_selected_valueProperty().removeListener(this.changeListener_category);
            this.changeListener_category = null;
        }

        if (this.listChangeListener_tag != null) {
            this.createDebateViewModel.listTag_selected_valueProperty().removeListener(this.listChangeListener_tag);
            this.listChangeListener_tag = null;
        }

        //Text
        this.titledCreate.textProperty().unbind();
        this.lblTitle.textProperty().unbind();
        this.btnAddItem.textProperty().unbind();
        this.lblDescription.textProperty().unbind();
        this.btnValid.textProperty().unbind();

        //Value
        this.tfTitle.textProperty().unbindBidirectional(this.createDebateViewModel.tfTitle_valueProperty());
    }
}
