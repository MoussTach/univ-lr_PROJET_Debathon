package fr.univlr.debathon.application.view.mainwindow;

import de.saxsys.mvvmfx.InjectViewModel;
import de.saxsys.mvvmfx.ViewTuple;
import fr.univlr.debathon.application.view.FxmlView_SceneCycle;
import fr.univlr.debathon.application.view.mainwindow.debate.items.CategorySelectView;
import fr.univlr.debathon.application.view.mainwindow.debate.items.TagSelectView;
import fr.univlr.debathon.application.view.mainwindow.debate.items.TagView;
import fr.univlr.debathon.application.viewmodel.mainwindow.debate.SelectWindowViewModel;
import fr.univlr.debathon.application.viewmodel.mainwindow.debate.items.CategorySelectViewModel;
import fr.univlr.debathon.application.viewmodel.mainwindow.debate.items.TagSelectViewModel;
import fr.univlr.debathon.application.viewmodel.mainwindow.debate.items.TagViewModel;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;

import java.net.URL;
import java.util.ResourceBundle;

public class SelectWindowView extends FxmlView_SceneCycle<SelectWindowViewModel> implements Initializable {

    @FXML private Label lblCategory;
    @FXML private FlowPane flowCategories;

    @FXML private Label lblTags;
    @FXML private FlowPane flowTags;

    @InjectViewModel
    private SelectWindowViewModel selectWindowViewModel;

    private ListChangeListener<ViewTuple<TagSelectView, TagSelectViewModel>> listChangeListener_tag;
    private ListChangeListener<ViewTuple<CategorySelectView, CategorySelectViewModel>> listChangeListener_category;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.setViewModel(selectWindowViewModel);

        //Text
        lblCategory.textProperty().bind(this.selectWindowViewModel.lblCategory_labelProperty());
        lblTags.textProperty().bind(this.selectWindowViewModel.lblTags_labelProperty());

        //Values
        this.selectWindowViewModel.listTags_valueProperty().forEach(item -> flowTags.getChildren().add(item.getView()));
        this.listChangeListener_tag = change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().stream().filter(item -> !flowTags.getChildren().contains(item.getView())).forEach(item -> flowTags.getChildren().add(item.getView()));
                } else if (change.wasRemoved()) {
                    change.getRemoved().forEach(item -> flowTags.getChildren().remove(item.getView()));
                }
            }
        };
        this.selectWindowViewModel.listTags_valueProperty().addListener(this.listChangeListener_tag);


        this.selectWindowViewModel.listCategories_valueProperty().forEach(item -> flowCategories.getChildren().add(item.getView()));
        this.listChangeListener_category = change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().stream().filter(item -> !flowCategories.getChildren().contains(item.getView())).forEach(item -> flowTags.getChildren().add(item.getView()));
                } else if (change.wasRemoved()) {
                    change.getRemoved().forEach(item -> flowCategories.getChildren().remove(item.getView()));
                }
            }
        };
        this.selectWindowViewModel.listCategories_valueProperty().addListener(this.listChangeListener_category);
    }


    @Override
    public void onViewAdded_Cycle() {
    }

    @Override
    public void onViewRemoved_Cycle() {
        if (this.listChangeListener_category != null) {
            this.selectWindowViewModel.listCategories_valueProperty().removeListener(this.listChangeListener_category);
            this.listChangeListener_category = null;
        }

        if (this.listChangeListener_tag != null) {
            this.selectWindowViewModel.listTags_valueProperty().removeListener(this.listChangeListener_tag);
            this.listChangeListener_tag = null;
        }

        //Text
        lblCategory.textProperty().unbind();
        lblTags.textProperty().unbind();

        //Values
    }
}
