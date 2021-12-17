package fr.univlr.debathon.application.view.mainwindow.debate.items;

import de.saxsys.mvvmfx.InjectViewModel;
import fr.univlr.debathon.application.view.FxmlView_SceneCycle;
import fr.univlr.debathon.application.viewmodel.mainwindow.debate.items.CategoryViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class CategoryView extends FxmlView_SceneCycle<CategoryViewModel> implements Initializable {

    @FXML private BorderPane bPaneCategory;
    @FXML private Label lblCategory;

    @InjectViewModel
    private CategoryViewModel categoryViewModel;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.setViewModel(categoryViewModel);

        //Text
        lblCategory.textProperty().bind(this.categoryViewModel.lblCategory_labelProperty());

        this.categoryViewModel.colorProperty().addListener((observableValue, oldValue, newValue) ->
                bPaneCategory.setStyle(
                        String.format("-fx-background-color:%s;-fx-border-radius:%s;-fx-background-radius:%s;-fx-border-width:%s;-fx-border-color:%s;",
                                newValue,
                                "10",
                                "10",
                                "2",
                                "LIGHTGRAY"
                        )));
    }


    @Override
    public void onViewAdded_Cycle() {
    }

    @Override
    public void onViewRemoved_Cycle() {
        //Text
        lblCategory.textProperty().unbind();
    }
}
