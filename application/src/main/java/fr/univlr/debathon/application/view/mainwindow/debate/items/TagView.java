package fr.univlr.debathon.application.view.mainwindow.debate.items;

import de.saxsys.mvvmfx.InjectViewModel;
import fr.univlr.debathon.application.view.FxmlView_SceneCycle;
import fr.univlr.debathon.application.viewmodel.mainwindow.debate.items.TagViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class TagView extends FxmlView_SceneCycle<TagViewModel> implements Initializable {

    @FXML private BorderPane bPaneTag;
    @FXML private Label lblTag;

    @InjectViewModel
    private TagViewModel tagViewModel;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.setViewModel(tagViewModel);

        //Text
        lblTag.textProperty().bind(this.tagViewModel.lblTag_labelProperty());

        this.tagViewModel.colorProperty().addListener((observableValue, oldValue, newValue) -> {
            bPaneTag.setStyle(
                    String.format("-fx-background-color:%s;-fx-border-radius:%s;-fx-background-radius:%s;-fx-border-width:%s;-fx-border-color:%s;",
                            newValue,
                            "10",
                            "10",
                            "2",
                            "LIGHTGRAY"
                    ));
        });
    }


    @Override
    public void onViewAdded_Cycle() {
    }

    @Override
    public void onViewRemoved_Cycle() {
        //Text
        lblTag.textProperty().unbind();
    }
}
