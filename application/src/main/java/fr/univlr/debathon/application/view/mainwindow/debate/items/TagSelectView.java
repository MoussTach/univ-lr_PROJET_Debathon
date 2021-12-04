package fr.univlr.debathon.application.view.mainwindow.debate.items;

import fr.univlr.debathon.application.view.FxmlView_SceneCycle;
import fr.univlr.debathon.application.viewmodel.mainwindow.debate.items.TagSelectViewModel;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class TagSelectView extends FxmlView_SceneCycle<TagSelectViewModel> implements Initializable {

    @FXML private BorderPane bPaneTag;
    @FXML private ToggleButton tbtnTag;

    @FXML
    private TagSelectViewModel tagSelectViewModel;

    private ChangeListener<Boolean> listenerValue = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.setViewModel(tagSelectViewModel);

        //Text
        tbtnTag.textProperty().bind(this.tagSelectViewModel.tbtnTag_labelProperty());

        this.tagSelectViewModel.colorProperty().addListener((observableValue, oldValue, newValue) -> {
            bPaneTag.setStyle(
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
                this.tagSelectViewModel.addToList(newValue);
        this.tbtnTag.selectedProperty().addListener(this.listenerValue);
    }


    @Override
    public void onViewAdded_Cycle() {
    }

    @Override
    public void onViewRemoved_Cycle() {
        if (this.listenerValue == null) {
            this.tbtnTag.selectedProperty().removeListener(this.listenerValue);
            this.listenerValue = null;
        }

        //Text
        tbtnTag.textProperty().unbind();
    }
}
