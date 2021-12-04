package fr.univlr.debathon.application.view.mainwindow.debate.items;

import fr.univlr.debathon.application.view.FxmlView_SceneCycle;
import fr.univlr.debathon.application.viewmodel.mainwindow.debate.items.CategorySelectViewModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class CategorySelectView extends FxmlView_SceneCycle<CategorySelectViewModel> implements Initializable {

    @FXML private BorderPane bPaneCategory;
    @FXML private ToggleButton tbtnCategory;

    @FXML
    private CategorySelectViewModel categorySelectViewModel;

    private ChangeListener<Boolean> listenerValue = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.setViewModel(categorySelectViewModel);

        //Text
        tbtnCategory.textProperty().bind(this.categorySelectViewModel.tbtnTag_labelProperty());

        this.categorySelectViewModel.colorProperty().addListener((observableValue, oldValue, newValue) -> {
            bPaneCategory.setStyle(
                    String.format("-fx-background-color:%s;-fx-border-radius:%s;-fx-background-radius:%s;-fx-border-width:%s;-fx-border-color:%s;",
                            newValue,
                            "10",
                            "10",
                            "2",
                            "LIGHTGRAY"
                    ));
        });

        //Value
        this.listenerValue = (observableValue, oldValue, newValue) ->
                this.categorySelectViewModel.addToList(newValue);
        this.tbtnCategory.selectedProperty().addListener(this.listenerValue);
    }

    @Override
    public void onViewAdded_Cycle() {
    }

    @Override
    public void onViewRemoved_Cycle() {
        if (this.listenerValue == null) {
            this.tbtnCategory.selectedProperty().removeListener(this.listenerValue);
            this.listenerValue = null;
        }

        //Text
        tbtnCategory.textProperty().unbind();
    }
}
