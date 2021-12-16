package fr.univlr.debathon.application.view.mainwindow.debate;

import de.saxsys.mvvmfx.InjectViewModel;
import de.saxsys.mvvmfx.ViewTuple;
import de.saxsys.mvvmfx.utils.validation.visualization.ControlsFxVisualizer;
import fr.univlr.debathon.application.view.FxmlView_SceneCycle;
import fr.univlr.debathon.application.view.mainwindow.debate.items.CategoryView;
import fr.univlr.debathon.application.view.mainwindow.debate.items.TagView;
import fr.univlr.debathon.application.viewmodel.mainwindow.CreateDebateViewModel;
import fr.univlr.debathon.application.viewmodel.mainwindow.debate.CreateQuestionViewModel;
import fr.univlr.debathon.application.viewmodel.mainwindow.debate.items.CategoryViewModel;
import fr.univlr.debathon.application.viewmodel.mainwindow.debate.items.TagViewModel;
import fr.univlr.debathon.custom.remastered.controls.tabview.CustomEditingCell_String;
import fr.univlr.debathon.custom.remastered.controls.tabview.CustomTableView;
import fr.univlr.debathon.job.db_project.jobclass.Mcq;
import fr.univlr.debathon.job.db_project.jobclass.Question;
import fr.univlr.debathon.log.generate.CustomLogger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.controlsfx.validation.decoration.StyleClassValidationDecoration;

import java.net.URL;
import java.util.ResourceBundle;

public class CreateQuestionView extends FxmlView_SceneCycle<CreateQuestionViewModel> implements Initializable {

    private static final CustomLogger LOGGER = CustomLogger.create(CreateQuestionView.class.getName());

    @FXML private TitledPane titledCreate;

    @FXML private Label lblQuestion;
    @FXML private TextArea taQuestion;

    @FXML private Label lblQuestionType;
    @FXML private ComboBox<Question.Type> cbQuestionType;

    @FXML private VBox vboxReponses;
    @FXML private Label lblReponses;
    @FXML private TableView<Mcq> tableResponses;
    @FXML private TableColumn<Mcq, String> tabResponse;
    @FXML private Button btnResponsesAdd;
    @FXML private Button btnResponsesDelete;

    @FXML private Button btnValid;

    @InjectViewModel
    private CreateQuestionViewModel createQuestionViewModel;

    private ChangeListener<Boolean> listener_canCreateQuestion = null;
    private ChangeListener<Question.Type> listenerQuestion = null;

    @FXML
    private void act_btnResponsesAdd() {
        LOGGER.input(String.format("Press the button %s", btnResponsesAdd.getId()));

        //Verify if the last response is completed or not.
        if (!tableResponses.getItems().isEmpty()) {
            Mcq lastMcq = this.tableResponses.getItems().get(tableResponses.getItems().size() - 1);
            if (lastMcq.getLabel() == null || lastMcq.getLabel().isEmpty()) {
                if (LOGGER.isWarnEnabled()) {
                    LOGGER.warn("Unable to add a response row: the last row is not completed");
                }
                return;
            }
        }

        this.createQuestionViewModel.actvm_ResponsesAdd();
        this.tableResponses.getSelectionModel().selectLast();
    }

    @FXML
    private void act_btnResponsesDelete() {
        LOGGER.input(String.format("Press the button %s", btnResponsesDelete.getId()));

        if (!tableResponses.getItems().isEmpty()) {
            Mcq selectedMcq = tableResponses.getSelectionModel().getSelectedItem();
            if (selectedMcq != null) {
                this.createQuestionViewModel.actvm_ResponsesDelete(selectedMcq);
                this.tableResponses.refresh();
            }
        }
    }

    @FXML
    private void act_btnValid() {
        LOGGER.input(String.format("Press the button %s", btnValid.getId()));

        this.createQuestionViewModel.actvm_ValidCreateDebate();
    }

    /**
     * Initialize the tablerow
     *
     * @author Gaetan Brenckle
     */
    private void initialize_tab() {
        this.tabResponse.setCellValueFactory(cellData -> cellData.getValue().labelProperty());
        this.tabResponse.setCellFactory(param -> new CustomEditingCell_String<>());
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.setViewModel(createQuestionViewModel);

        initialize_tab();
        ControlsFxVisualizer visualizer = new ControlsFxVisualizer();
        //StyleSheet added directly on fxml
        visualizer.setDecoration(new StyleClassValidationDecoration());
        visualizer.initVisualization(this.createQuestionViewModel.rule_Question(), taQuestion, true);
        visualizer.initVisualization(this.createQuestionViewModel.rule_QuestionType(), cbQuestionType, true);

        //Text
        this.titledCreate.textProperty().bind(this.createQuestionViewModel.titledCreate_labelProperty());
        this.lblQuestion.textProperty().bindBidirectional(this.createQuestionViewModel.lblQuestion_labelProperty());
        this.lblQuestionType.textProperty().bind(this.createQuestionViewModel.lblQuestionType_labelProperty());
        this.lblReponses.textProperty().bind(this.createQuestionViewModel.lblReponses_labelProperty());
        this.tabResponse.textProperty().bind(this.createQuestionViewModel.tabReponses_labelProperty());
        this.btnValid.textProperty().bind(this.createQuestionViewModel.btnValid_labelProperty());

        //Value
        this.taQuestion.textProperty().bindBidirectional(this.createQuestionViewModel.taQuestion_valueProperty());
        this.cbQuestionType.itemsProperty().bind(this.createQuestionViewModel.cbQuestionType_valueProperty());
        this.cbQuestionType.valueProperty().bindBidirectional(this.createQuestionViewModel.selectedQuestionType_valueProperty());

        this.listenerQuestion = (observableValue, oldValue, newValue) -> {
            if (newValue != null && newValue.equals(Question.Type.LIBRE)) {
                this.createQuestionViewModel.isShowedResponsesProperty().set(false);
                vboxReponses.setManaged(false);
                vboxReponses.setVisible(false);
            } else {
                this.createQuestionViewModel.isShowedResponsesProperty().set(true);
                vboxReponses.setManaged(true);
                vboxReponses.setVisible(true);
            }
        };
        this.createQuestionViewModel.selectedQuestionType_valueProperty().addListener(this.listenerQuestion);

        this.tableResponses.setItems(this.createQuestionViewModel.listResponses_valueProperty());

        this.btnValid.disableProperty().bind(this.createQuestionViewModel.getValidator_CreateQuestion().getValidationStatus().validProperty().not());
    }


    @Override
    public void onViewAdded_Cycle() {
    }

    @Override
    public void onViewRemoved_Cycle() {
    }
}
