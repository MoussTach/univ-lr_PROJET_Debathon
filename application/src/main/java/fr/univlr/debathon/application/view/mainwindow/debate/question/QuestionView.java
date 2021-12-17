package fr.univlr.debathon.application.view.mainwindow.debate.question;

import de.saxsys.mvvmfx.InjectViewModel;
import de.saxsys.mvvmfx.ViewTuple;
import fr.univlr.debathon.application.view.FxmlView_SceneCycle;
import fr.univlr.debathon.application.viewmodel.mainwindow.debate.question.QuestionViewModel;
import fr.univlr.debathon.application.viewmodel.mainwindow.debate.question.ResponseViewModel;
import fr.univlr.debathon.job.db_project.jobclass.Mcq;
import fr.univlr.debathon.log.generate.CustomLogger;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class QuestionView extends FxmlView_SceneCycle<QuestionViewModel> implements Initializable {

    private static final CustomLogger LOGGER = CustomLogger.create(QuestionView.class.getName());

    @FXML private Label lblQuestion;
    @FXML private VBox vBoxQuestions;

    @FXML private Button btnComments;
    @FXML private Button btnValid;

    @InjectViewModel
    private QuestionViewModel questionViewModel;

    private ListChangeListener<ViewTuple<ResponseView, ResponseViewModel>> listChangeListener_Responses;


    @FXML
    public void act_btnComments() {
        LOGGER.input(String.format("Press the button %s", btnComments.getId()));

        this.questionViewModel.actvm_btnComments();
    }

    @FXML
    public void act_btnValid() {
        LOGGER.input(String.format("Press the button %s", btnValid.getId()));

        if (this.questionViewModel.actvm_btnValid()) {
            vBoxQuestions.setDisable(true);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.setViewModel(questionViewModel);

        //Text
        this.lblQuestion.textProperty().bind(this.questionViewModel.lblQuestion_labelProperty());

        //Value

        //Check if a mcq already have a check
        boolean alreadyAnwser = false;
        for (Mcq mcq : this.questionViewModel.getQuestion().listMcqProperty()) {
            if (mcq.getNb_votes() < 0) {
                alreadyAnwser = true;
                break;
            }
        }
        this.vBoxQuestions.setDisable(alreadyAnwser);

        this.questionViewModel.listResponsesProperty().forEach(item -> vBoxQuestions.getChildren().add(item.getView()));
        this.listChangeListener_Responses = change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().stream().filter(item -> !vBoxQuestions.getChildren().contains(item.getView())).forEach(item -> vBoxQuestions.getChildren().add(item.getView()));
                } else if (change.wasRemoved()) {
                    change.getRemoved().forEach(item -> vBoxQuestions.getChildren().remove(item.getView()));
                }
            }
        };
        this.questionViewModel.listResponsesProperty().addListener(this.listChangeListener_Responses);
    }


    @Override
    public void onViewAdded_Cycle() {
    }

    @Override
    public void onViewRemoved_Cycle() {
        if (this.listChangeListener_Responses != null) {
            this.questionViewModel.listResponsesProperty().removeListener(this.listChangeListener_Responses);
            this.listChangeListener_Responses = null;
        }


        //Text
        this.lblQuestion.textProperty().unbind();

        //Value
    }
}
