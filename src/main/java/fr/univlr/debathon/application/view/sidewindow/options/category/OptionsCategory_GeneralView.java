package fr.univlr.debathon.application.view.sidewindow.options.category;

import de.saxsys.mvvmfx.InjectViewModel;
import fr.univlr.debathon.application.view.FxmlView_SceneCycle;
import fr.univlr.debathon.application.viewmodel.sidewindow.options.category.OptionsCategory_GeneralViewModel;
import fr.univlr.debathon.custom.remastered.controls.list.CustomComboBox_R;
import fr.univlr.debathon.language.LanguageBundle;
import fr.univlr.debathon.log.generate.CustomLogger;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TitledPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * View of the category general.
 * This view represent the accessibility of general options
 *
 * @author Gaetan Brenckle
 */
public class OptionsCategory_GeneralView extends FxmlView_SceneCycle<OptionsCategory_GeneralViewModel> implements Initializable {

    private static final CustomLogger LOGGER = CustomLogger.create(OptionsCategory_GeneralView.class.getName());

    @FXML
    private TitledPane tPaneGeneral_language;

    @FXML
    private Text txtGeneral_languageLabel;
    @FXML
    private CustomComboBox_R<LanguageBundle.languages> cmdGeneral_language;

    private ChangeListener<LanguageBundle.languages> listener_ChangedValue_logger_Language_ = null;

    @InjectViewModel
    private OptionsCategory_GeneralViewModel optionsCategory_generalViewModel;


    /**
     * Method used to setup the field to log each change.
     *
     * @author Gaetan Brenckle
     */
    private void setup_logger_outfocus() {

        if (this.listener_ChangedValue_logger_Language_ == null) {
            this.listener_ChangedValue_logger_Language_ = (observable, oldValue, newValue) ->
                    LOGGER.input(String.format("Change value of the comboBox %s to -> %s", this.cmdGeneral_language.getId(), (newValue == null) ? "null" : newValue.toString()));
            this.cmdGeneral_language.valueProperty().addListener(this.listener_ChangedValue_logger_Language_);
        }
    }

    public void initialize(URL location, ResourceBundle resources) {
        this.setViewModel(optionsCategory_generalViewModel);
        setup_logger_outfocus();

        this.tPaneGeneral_language.textProperty().bind(this.optionsCategory_generalViewModel.tPaneGeneral_languageLabelProperty());
        this.txtGeneral_languageLabel.textProperty().bind(this.optionsCategory_generalViewModel.txtGeneral_languageLabelProperty());
        this.cmdGeneral_language.listItemsProperty().bind(this.optionsCategory_generalViewModel.getlistLanguage());
        this.cmdGeneral_language.valueProperty().bindBidirectional(this.optionsCategory_generalViewModel.selectCmdGeneral_language_Property());
    }


    @Override
    public void onViewAdded_Cycle() {
    }

    @Override
    public void onViewRemoved_Cycle() {
        if (this.listener_ChangedValue_logger_Language_ != null) {
            this.cmdGeneral_language.valueProperty().removeListener(this.listener_ChangedValue_logger_Language_);
            this.listener_ChangedValue_logger_Language_ = null;
        }

        this.tPaneGeneral_language.textProperty().unbind();
        this.txtGeneral_languageLabel.textProperty().unbind();
        this.cmdGeneral_language.listItemsProperty().unbind();
        this.cmdGeneral_language.valueProperty().unbindBidirectional(this.optionsCategory_generalViewModel.selectCmdGeneral_language_Property());
    }
}
