package fr.univlr.debathon.application.view.mainwindow.debate;

import de.saxsys.mvvmfx.InjectViewModel;
import de.saxsys.mvvmfx.ViewTuple;
import fr.univlr.debathon.application.view.FxmlView_SceneCycle;
import fr.univlr.debathon.application.view.mainwindow.debate.items.CategoryView;
import fr.univlr.debathon.application.view.mainwindow.debate.items.TagView;
import fr.univlr.debathon.application.viewmodel.mainwindow.debate.DebateThumbnailViewModel;
import fr.univlr.debathon.application.viewmodel.mainwindow.debate.items.CategoryViewModel;
import fr.univlr.debathon.application.viewmodel.mainwindow.debate.items.TagViewModel;
import fr.univlr.debathon.log.generate.CustomLogger;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

import java.net.URL;
import java.util.ResourceBundle;

public class DebateThumbnailView extends FxmlView_SceneCycle<DebateThumbnailViewModel> implements Initializable {

    private static final CustomLogger LOGGER = CustomLogger.create(DebateThumbnailView.class.getName());

    @FXML private BorderPane borderPane;
    @FXML private Label lblTitle;

    @FXML private FlowPane flowCategory;
    @FXML private FlowPane flowTag;

    @FXML private Label lblNbPeople;

    @FXML private Button btnOpenDebate;

    @InjectViewModel
    private DebateThumbnailViewModel debateThumbnailViewModel;

    private ChangeListener<ViewTuple<CategoryView, CategoryViewModel>> changeListener_category;
    private ListChangeListener<ViewTuple<TagView, TagViewModel>> listChangeListener_tag;

    @FXML
    public void act_btnOpenDebate() {
        LOGGER.input(String.format("Press the button %s", btnOpenDebate.getId()));

        this.debateThumbnailViewModel.actvm_btnOpenDebate();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Value
        this.lblTitle.textProperty().bind(this.debateThumbnailViewModel.lblTitle_labelProperty());

        if (this.debateThumbnailViewModel.category_valueProperty().get() != null) {
            flowCategory.getChildren().add(this.debateThumbnailViewModel.category_valueProperty().get().getView());
            borderPane.setStyle(
                    String.format("-fx-border-width:%s;-fx-border-color:%s;",
                            "2",
                            this.debateThumbnailViewModel.category_valueProperty().get().getViewModel().getCategory().getColor()
                    ));
        }
        this.changeListener_category = (observableValue, oldValue, newValue) -> {
            if (newValue != null) {
                flowCategory.getChildren().add(newValue.getView());
                borderPane.setStyle(
                        String.format("-fx-border-width:%s;-fx-border-color:%s;",
                                "3",
                                newValue.getViewModel().getCategory().getColor()
                        ));
            } else {
                borderPane.setStyle(
                        String.format("-fx-border-width:%s;-fx-border-color:%s;",
                                "3",
                                "LIGHTGRAY"
                        ));
            }

            if (oldValue != null) {
                flowCategory.getChildren().remove(oldValue.getView());
            }
        };

        this.debateThumbnailViewModel.listTag_selected_valueProperty().forEach(item -> flowTag.getChildren().add(item.getView()));
        this.listChangeListener_tag = change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().stream().filter(item -> !flowTag.getChildren().contains(item.getView())).forEach(item -> flowTag.getChildren().add(item.getView()));
                } else if (change.wasRemoved()) {
                    change.getRemoved().forEach(item -> flowTag.getChildren().remove(item.getView()));
                }
            }
        };
        this.debateThumbnailViewModel.listTag_selected_valueProperty().addListener(this.listChangeListener_tag);

        this.lblNbPeople.textProperty().bind(this.debateThumbnailViewModel.lblNbPeople_valueProperty());

        this.setViewModel(debateThumbnailViewModel);
    }


    @Override
    public void onViewAdded_Cycle() {
    }

    @Override
    public void onViewRemoved_Cycle() {
    }
}
