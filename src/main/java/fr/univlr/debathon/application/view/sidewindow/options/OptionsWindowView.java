package fr.univlr.debathon.application.view.sidewindow.options;

import de.saxsys.mvvmfx.InjectViewModel;
import de.saxsys.mvvmfx.utils.viewlist.CachedViewModelCellFactory;
import fr.univlr.debathon.application.view.FxmlView_SceneCycle;
import fr.univlr.debathon.application.view.sidewindow.options.category.CategoryListItemView;
import fr.univlr.debathon.application.viewmodel.sidewindow.options.OptionsWindowViewModel;
import fr.univlr.debathon.application.viewmodel.sidewindow.options.category.CategoryListItemViewModel;
import fr.univlr.debathon.log.generate.CustomLogger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * View for the options window.
 *
 * @author Gaetan Brenckle
 */
public class OptionsWindowView extends FxmlView_SceneCycle<OptionsWindowViewModel> implements Initializable {

    private static final CustomLogger LOGGER = CustomLogger.create(OptionsWindowView.class.getName());

    @FXML
    private BorderPane bPaneOptionRoot;

    @FXML
    private ListView<CategoryListItemViewModel> listOptions_category;

    @FXML
    private javafx.scene.control.ScrollPane sPaneOptions_detail;
    @FXML
    private BorderPane bPaneOptions_detail_content;

    @FXML
    private Button btnOptions_ok;
    @FXML
    private Button btnOptions_cancel;
    @FXML
    private Button btnOptions_apply;

    @InjectViewModel
    private OptionsWindowViewModel optionsWindowViewModel;

    /**
     * action when the list is clicked to change the current category
     *
     * @author Gaetan Brenckle
     */
    @FXML
    private void actMouseClicked_listCategory() {
        LOGGER.input(String.format("Press the Item on the listView %s to go on the category -> %s", listOptions_category.getId(), listOptions_category.getSelectionModel().getSelectedItem().categoryNameProperty().get()));
    }

    /**
     * action of the button btnOptions_ok.
     *
     * @author Gaetan Brenckle
     */
    @FXML
    private void act_Ok() {
        LOGGER.input(String.format("Press the button %s", btnOptions_ok.getId()));
        optionsWindowViewModel.act_btnOk();

        final Stage stage = (Stage)btnOptions_cancel.getScene().getWindow();
        stage.close();
    }

    /**
     * action of the button btnOptions_cancel.
     *
     * @author Gaetan Brenckle
     */
    @FXML
    private void act_Cancel() {
        LOGGER.input(String.format("Press the button %s", btnOptions_cancel.getId()));
        optionsWindowViewModel.act_btnCancel();

        final Stage stage = (Stage)btnOptions_cancel.getScene().getWindow();
        stage.close();
    }

    /**
     * action of the button btnOptions_apply.
     *
     * @author Gaetan Brenckle
     */
    @FXML
    private void act_Apply() {
        LOGGER.input(String.format("Press the button %s", btnOptions_apply.getId()));
        optionsWindowViewModel.act_btnApply();
    }

    public void initialize(URL location, ResourceBundle resources) {
        this.setViewModel(optionsWindowViewModel);

        optionsWindowViewModel.optionRootNodeProperty().set(bPaneOptionRoot);

        listOptions_category.setCellFactory(CachedViewModelCellFactory.createForFxmlView(CategoryListItemView.class));
        listOptions_category.setItems(optionsWindowViewModel.getOlistCategoryItemViews());

        optionsWindowViewModel.selectedCategoryProperty().bind(listOptions_category.getSelectionModel().selectedItemProperty());
        bPaneOptions_detail_content.centerProperty().bind(optionsWindowViewModel.currentSceneNode_Property());

        btnOptions_ok.disableProperty().bind(optionsWindowViewModel.getOptionValidator().getValidationStatus().validProperty().not());
        btnOptions_ok.textProperty().bind(optionsWindowViewModel.btnOptions_okLabelProperty());

        btnOptions_apply.disableProperty().bind(optionsWindowViewModel.getCurrentSceneValidator().getValidationStatus().validProperty().not());
        btnOptions_apply.textProperty().bind(optionsWindowViewModel.btnOptions_applyLabelProperty());

        btnOptions_cancel.textProperty().bind(optionsWindowViewModel.btnOptions_cancelLabelProperty());

        listOptions_category.getSelectionModel().selectFirst();
    }


    @Override
    public void onViewAdded_Cycle() {
    }

    @Override
    public void onViewRemoved_Cycle() {
        optionsWindowViewModel.selectedCategoryProperty().unbind();
        bPaneOptions_detail_content.centerProperty().unbind();
        btnOptions_ok.disableProperty().unbind();
        btnOptions_ok.textProperty().unbind();
        btnOptions_apply.disableProperty().unbind();
        btnOptions_apply.textProperty().unbind();
        btnOptions_cancel.textProperty().unbind();
    }
}
