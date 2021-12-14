package fr.univlr.debathon.application.view.mainwindow.debate;

import de.saxsys.mvvmfx.InjectViewModel;
import de.saxsys.mvvmfx.ViewTuple;
import de.saxsys.mvvmfx.utils.validation.visualization.ControlsFxVisualizer;
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
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;
import org.controlsfx.validation.decoration.StyleClassValidationDecoration;

import java.net.URL;
import java.util.ResourceBundle;

public class CreateQuestionView extends FxmlView_SceneCycle<CreateDebateViewModel> implements Initializable {

    private static final CustomLogger LOGGER = CustomLogger.create(CreateQuestionView.class.getName());

    @FXML private TitledPane titledCreate;

    @FXML private Label lblQuestion;
    @FXML private TextArea taQuestion;

    @FXML private Label lblQuestionType;
    @FXML private ComboBox<?> cbQuestionType;

    @FXML private VBox vboxReponses;
    @FXML private Label lblReponses;
    @FXML private Button btnResponsesAdd;
    @FXML private Button btnResponsesDelete;
    @FXML private TableView<?> tableResponses;
    @FXML private TableColumn<?, ?> tabResponse;

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

        ControlsFxVisualizer visualizer = new ControlsFxVisualizer();
        //StyleSheet added directly on fxml
        visualizer.setDecoration(new StyleClassValidationDecoration());
        visualizer.initVisualization(this.createDebateViewModel.rule_title(), tfTitle, true);
        visualizer.initVisualization(this.createDebateViewModel.rule_Key(), tfKey, true);

        //Text
        this.titledCreate.textProperty().bind(this.createDebateViewModel.titledCreate_labelProperty());
        this.lblTitle.textProperty().bindBidirectional(this.createDebateViewModel.lblTitle_labelProperty());
        this.btnAddItem.textProperty().bind(this.createDebateViewModel.btnAddItem_labelProperty());
        this.lblDescription.textProperty().bind(this.createDebateViewModel.lblDescription_labelProperty());
        this.lblKey.textProperty().bind(this.createDebateViewModel.lblKey_labelProperty());
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
        this.tfKey.textProperty().bindBidirectional(this.createDebateViewModel.tfKey_valueProperty());

        this.btnValid.disableProperty().bind(this.createDebateViewModel.getValidator_CreateDebate().getValidationStatus().validProperty().not());
    }


    @Override
    public void onViewAdded_Cycle() {
    }

    @Override
    public void onViewRemoved_Cycle() {
    }
}
