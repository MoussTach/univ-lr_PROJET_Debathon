package fr.univlr.debathon.application.view.mainwindow.debate;

import de.saxsys.mvvmfx.InjectViewModel;
import fr.univlr.debathon.application.view.FxmlView_SceneCycle;
import fr.univlr.debathon.application.view.mainwindow.HomePageView;
import fr.univlr.debathon.application.viewmodel.mainwindow.debate.CreateDebateViewModel;
import fr.univlr.debathon.application.viewmodel.sidewindow.comments.CommentsWindowsViewModel;
import fr.univlr.debathon.log.generate.CustomLogger;
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

    @FXML private Button btnValid;

    @InjectViewModel
    private CreateDebateViewModel createDebateViewModel;

    @FXML
    private void act_btnAddItem() {
        LOGGER.input(String.format("Press the button %s", btnAddItem.getId()));

        //TODO addItem
    }

    @FXML
    private void act_btnValid() {
        LOGGER.input(String.format("Press the button %s", btnValid.getId()));

        //TODO valid

        //htmlEditor doesn't have a binding
        this.createDebateViewModel.htmlEditorDescription_valueProperty().set(this.htmlEditorDescription.getHtmlText());
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.setViewModel(createDebateViewModel);

        //Text
        this.titledCreate.textProperty().bind(this.createDebateViewModel.titledCreate_labelProperty());
        this.lblTitle.textProperty().bind(this.createDebateViewModel.lblTitle_labelProperty());
        this.lblDescription.textProperty().bind(this.createDebateViewModel.lblDescription_labelProperty());

        //Value
        this.tfTitle.textProperty().bindBidirectional(this.createDebateViewModel.tfTitle_valueProperty());
    }


    @Override
    public void onViewAdded_Cycle() {
    }

    @Override
    public void onViewRemoved_Cycle() {
    }
}
