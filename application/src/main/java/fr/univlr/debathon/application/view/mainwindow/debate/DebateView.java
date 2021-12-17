package fr.univlr.debathon.application.view.mainwindow.debate;

import de.saxsys.mvvmfx.InjectViewModel;
import de.saxsys.mvvmfx.ViewTuple;
import fr.univlr.debathon.application.view.FxmlView_SceneCycle;
import fr.univlr.debathon.application.view.mainwindow.debate.items.CategoryView;
import fr.univlr.debathon.application.view.mainwindow.debate.items.TagView;
import fr.univlr.debathon.application.view.mainwindow.debate.question.QuestionView;
import fr.univlr.debathon.application.viewmodel.mainwindow.debate.DebateViewModel;
import fr.univlr.debathon.application.viewmodel.mainwindow.debate.items.CategoryViewModel;
import fr.univlr.debathon.application.viewmodel.mainwindow.debate.items.TagViewModel;
import fr.univlr.debathon.application.viewmodel.mainwindow.debate.question.QuestionViewModel;
import fr.univlr.debathon.log.generate.CustomLogger;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;

public class DebateView extends FxmlView_SceneCycle<DebateViewModel> implements Initializable {

    private static final CustomLogger LOGGER = CustomLogger.create(DebateView.class.getName());

    @FXML private BorderPane borderPane;

    @FXML private Button btnDeleteDebate;
    @FXML private Label lblTitle;
    @FXML private Button btnShowCreateQuestion;
    @FXML private Button btnShowStatMail;

    @FXML private FlowPane flowCategory;
    @FXML private FlowPane flowTag;
    @FXML private WebView webDebate;

    @FXML private VBox vboxQuestion;

    @InjectViewModel
    private DebateViewModel debateViewModel;

    private ChangeListener<String> changeListener_key;
    private ChangeListener<String> changeListener_htmlDesc;
    private ChangeListener<ViewTuple<CategoryView, CategoryViewModel>> changeListener_category;
    private ListChangeListener<ViewTuple<TagView, TagViewModel>> listChangeListener_tag;

    private ListChangeListener<ViewTuple<QuestionView, QuestionViewModel>> listChangeListener_question;

    @FXML
    public void act_btnDeleteDebate() {
        LOGGER.input(String.format("Press the button %s", btnDeleteDebate.getId()));

        this.debateViewModel.actvm_DeleteDebate();
    }


    @FXML
    public void act_btnShowCreateQuestion() {
        LOGGER.input(String.format("Press the button %s", btnShowCreateQuestion.getId()));

        this.debateViewModel.actvm_showCreateQuestion(btnShowCreateQuestion);
    }

    @FXML
    public void act_btnShowStatMail() {
        LOGGER.input(String.format("Press the button %s", btnShowStatMail.getId()));

        this.debateViewModel.actvm_showStatMail(btnShowStatMail);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.setViewModel(debateViewModel);

        this.debateViewModel.borderPaneProperty().set(borderPane);
        WebEngine webEngine = this.webDebate.getEngine();

        //Text
        this.lblTitle.textProperty().bind(this.debateViewModel.lblTitle_labelProperty());

        //Value
        boolean valueInit = this.debateViewModel.key_valueProperty().get() == null || this.debateViewModel.key_valueProperty().isEmpty().get();
        this.btnDeleteDebate.setVisible(valueInit);
        this.btnDeleteDebate.setManaged(valueInit);
        this.changeListener_key = (observableValue, oldValue, newValue) -> {
            boolean value = newValue == null || newValue.isEmpty();
            this.btnDeleteDebate.setVisible(value);
            this.btnDeleteDebate.setManaged(value);
        };
        this.debateViewModel.key_valueProperty().addListener(this.changeListener_key);

        if (!this.debateViewModel.category_valueProperty().isNull().get())
            this.flowCategory.getChildren().add(this.debateViewModel.category_valueProperty().get().getView());
        this.changeListener_category = (observableValue, oldValue, newValue) -> {
            if (newValue != null)
                flowCategory.getChildren().add(newValue.getView());
            else if (oldValue != null)
                flowCategory.getChildren().remove(oldValue.getView());
        };
        this.debateViewModel.category_valueProperty().addListener(this.changeListener_category);

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

        if (!this.debateViewModel.description_htmlTextProperty().isNull().get()) {
            String htmlText = this.debateViewModel.description_htmlTextProperty().get();
            if(htmlText.contains("contenteditable=\"true\"")){
                htmlText = htmlText.replace("contenteditable=\"true\"", "contenteditable=\"false\"");
            }
            webEngine.loadContent(htmlText);
        }
        this.changeListener_htmlDesc = (observableValue, oldValue, newValue) -> {
            if (newValue != null) {
                if(newValue.contains("contenteditable=\"true\"")){
                    newValue = newValue.replace("contenteditable=\"true\"", "contenteditable=\"false\"");
                }
                webEngine.loadContent(newValue);
            } else if (oldValue != null) {
                webEngine.loadContent("");
            }
        };

        this.debateViewModel.listQuestion_valueProperty().forEach(item -> vboxQuestion.getChildren().add(item.getView()));
        this.listChangeListener_question = change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().stream().filter(item -> !vboxQuestion.getChildren().contains(item.getView())).forEach(item -> vboxQuestion.getChildren().add(item.getView()));
                } else if (change.wasRemoved()) {
                    change.getRemoved().forEach(item -> vboxQuestion.getChildren().remove(item.getView()));
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
        if (this.changeListener_key != null) {
            this.debateViewModel.key_valueProperty().removeListener(this.changeListener_key);
            this.changeListener_key = null;
        }

        if (this.changeListener_category != null) {
            this.debateViewModel.category_valueProperty().removeListener(this.changeListener_category);
            this.changeListener_category = null;
        }

        if (this.listChangeListener_tag != null) {
            this.debateViewModel.listTag_valueProperty().removeListener(this.listChangeListener_tag);
            this.listChangeListener_tag = null;
        }

        if (this.changeListener_htmlDesc != null) {
            this.debateViewModel.description_htmlTextProperty().removeListener(this.changeListener_htmlDesc);
            this.changeListener_htmlDesc = null;
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
