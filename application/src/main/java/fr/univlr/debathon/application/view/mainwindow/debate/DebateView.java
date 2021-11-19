package fr.univlr.debathon.application.view.mainwindow.debate;

import de.saxsys.mvvmfx.InjectViewModel;
import de.saxsys.mvvmfx.ViewTuple;
import fr.univlr.debathon.application.view.FxmlView_SceneCycle;
import fr.univlr.debathon.application.view.mainwindow.debate.items.TagView;
import fr.univlr.debathon.application.view.mainwindow.debate.question.QuestionView;
import fr.univlr.debathon.application.viewmodel.mainwindow.debate.DebateViewModel;
import fr.univlr.debathon.application.viewmodel.mainwindow.debate.items.TagViewModel;
import fr.univlr.debathon.application.viewmodel.mainwindow.debate.question.QuestionViewModel;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

import java.net.URL;
import java.util.ResourceBundle;

public class DebateView extends FxmlView_SceneCycle<DebateViewModel> implements Initializable {

    @FXML private BorderPane borderPane;

    @FXML private FlowPane flowCategory;
    @FXML private FlowPane flowTag;

    @FXML private Label lblTitle;

    @FXML private FlowPane flowQuestion;

    @InjectViewModel
    private DebateViewModel debateViewModel;

    private ListChangeListener<ViewTuple<TagView, TagViewModel>> listChangeListener_tag;

    private ListChangeListener<ViewTuple<QuestionView, QuestionViewModel>> listChangeListener_question;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.setViewModel(debateViewModel);

        this.debateViewModel.borderPaneProperty().set(borderPane);

        //Text
        this.lblTitle.textProperty().bind(this.debateViewModel.lblTitle_labelProperty());

        //Value
        this.debateViewModel.listTag_valueProperty().forEach(item -> flowTag.getChildren().add(item.getView()));
        this.listChangeListener_tag = change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().stream().filter(item -> !flowTag.getChildren().contains(item.getView())).forEach(item -> flowTag.getChildren().add(item.getView()));
                } else if (change.wasRemoved()) {
                    change.getRemoved().forEach(item -> flowTag.getChildren().remove(item.getView()));
                }
            }
        };
        this.debateViewModel.listTag_valueProperty().addListener(this.listChangeListener_tag);

        this.debateViewModel.listQuestion_valueProperty().forEach(item -> flowQuestion.getChildren().add(item.getView()));
        this.listChangeListener_question = change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().stream().filter(item -> !flowQuestion.getChildren().contains(item.getView())).forEach(item -> flowQuestion.getChildren().add(item.getView()));
                } else if (change.wasRemoved()) {
                    change.getRemoved().forEach(item -> flowQuestion.getChildren().remove(item.getView()));
                }
            }
        };
        this.debateViewModel.listQuestion_valueProperty().addListener(this.listChangeListener_question);
    }


    @Override
    public void onViewAdded_Cycle() {
    }

    @Override
    public void onViewRemoved_Cycle() {
        if (this.listChangeListener_tag != null) {
            this.debateViewModel.listTag_valueProperty().removeListener(this.listChangeListener_tag);
            this.listChangeListener_tag = null;
        }

        if (this.listChangeListener_question != null) {
            this.debateViewModel.listQuestion_valueProperty().removeListener(this.listChangeListener_question);
            this.listChangeListener_question = null;
        }

        //Text
        this.lblTitle.textProperty().unbind();

        //Value
    }
}
