package fr.univlr.debathon.application.viewmodel.sidewindow.options.category;

import de.saxsys.mvvmfx.InjectScope;
import de.saxsys.mvvmfx.utils.commands.Action;
import de.saxsys.mvvmfx.utils.commands.Command;
import de.saxsys.mvvmfx.utils.commands.DelegateCommand;
import de.saxsys.mvvmfx.utils.validation.CompositeValidator;
import fr.univlr.debathon.custom.remastered.mvvm.utils.CompositeCommand_Remastered;
import fr.univlr.debathon.language.LanguageBundle;
import fr.univlr.debathon.log.generate.CustomLogger;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;

import java.util.ResourceBundle;


/**
 * ViewModel of the view {@link fr.univlr.debathon.application.view.sidewindow.options.category.OptionsCategory_GeneralView}.
 * This ViewModel process the accessibility of the general options when changed.
 *
 * @author Gaetan Brenckle
 */
public class OptionsCategory_GeneralViewModel extends CategoryValidator {

    private static final CustomLogger LOGGER = CustomLogger.create(OptionsCategory_GeneralViewModel.class.getName());

    private final ObjectProperty<ResourceBundle> resBundle_ = LanguageBundle.getInstance().bindResourceBundle("properties.language.sidewindow.options.lg_options");

    private final StringProperty tPaneGeneral_synchronizeLabel_ = new SimpleStringProperty(this.resBundle_.get().getString("titleGeneral_synchronizeRightsLabel"));

    private final StringProperty txtGeneral_synchronizeRightsLabel_ = new SimpleStringProperty(this.resBundle_.get().getString("txtGeneral_synchronizeRightsLabel"));


    private final StringProperty tPaneGeneral_languageLabel_ = new SimpleStringProperty(this.resBundle_.get().getString("titleGeneral_Language"));

    private final StringProperty txtGeneral_languageLabel_ = new SimpleStringProperty(this.resBundle_.get().getString("txtGeneral_languageLabel"));
    private final ObjectProperty<LanguageBundle.languages> selectCmdGeneral_language_ = new SimpleObjectProperty<>(LanguageBundle.getInstance().getCurrLanguage());
    private final ListProperty<LanguageBundle.languages> listLanguage_ = new SimpleListProperty<>(FXCollections.observableArrayList(LanguageBundle.languages.values()));

    private final Command languageCommand_ = new DelegateCommand(() -> new Action() {
        @Override
        protected void action() {

            actvm_changeLanguage();
        }
    }, true);
    private final CompositeValidator generalValidator_ = new CompositeValidator();

    private final CompositeCommand_Remastered general_Command_ = new CompositeCommand_Remastered(languageCommand_);

    private ChangeListener<ResourceBundle> listener_ChangedValue_bundleLanguage_ = null;

    @InjectScope
    private CategoryScope categoryScope;

    /**
     * Default Constructor.
     * Create a RessourceBundle listener.
     * Regroup all command on a compositeCommand.
     *
     * @author Gaetan Brenckle
     */
    public OptionsCategory_GeneralViewModel() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][constructor] Creation of the OptionsCategory_GeneralViewModel() object.");
        }

        //RessourceBundle listener
        if (this.listener_ChangedValue_bundleLanguage_ == null) {
            this.listener_ChangedValue_bundleLanguage_ = this::listener_bundleLanguage;
            this.resBundle_.addListener(this.listener_ChangedValue_bundleLanguage_);
        }
    }

    /**
     * action that will change the current language of the application with the language currently selectionned.
     *
     * @author Gaetan Brenckle
     */
    private void actvm_changeLanguage() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[private][method] usage of OptionsCategory_GeneralViewModel.actvm_changeLanguage().");
        }

        Platform.runLater(() -> LanguageBundle.getInstance().setCurrLocale(this.selectCmdGeneral_language_.get()));
    }


    /**
     * Property of the variable tPaneGeneral_synchronizeRightsLabel_.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link StringProperty} - return the property of the variable tPaneGeneral_synchronizeRightsLabel_.
     */
    public StringProperty tPaneGeneral_synchronizeRightsLabel_Property() {
        return tPaneGeneral_synchronizeLabel_;
    }

    /**
     * Property of the variable txtGeneral_synchronizeRightsLabel_.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link StringProperty} - return the property of the variable txtGeneral_synchronizeRightsLabel_.
     */
    public StringProperty txtGeneral_synchronizeRightsLabelProperty() {
        return txtGeneral_synchronizeRightsLabel_;
    }

    /**
     * Property of the variable listLanguage_.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link ListProperty} - return the property of the variable listLanguage_.
     */
    public ListProperty<LanguageBundle.languages> getlistLanguage() {
        return this.listLanguage_;
    }

    /**
     * Property of the variable tPaneGeneral_languageLabel_.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link StringProperty} - return the property of the variable tPaneGeneral_languageLabel_.
     */
    public StringProperty tPaneGeneral_languageLabelProperty() {
        return this.tPaneGeneral_languageLabel_;
    }

    /**
     * Property of the variable txtGeneral_languageLabel_.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link StringProperty} - return the property of the variable txtGeneral_languageLabel_.
     */
    public StringProperty txtGeneral_languageLabelProperty() {
        return this.txtGeneral_languageLabel_;
    }

    /**
     * Property of the variable selectCmdGeneral_language_.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link ObjectProperty}{@literal <}{@link LanguageBundle.languages}{@literal >} - return the property of the variable selectCmdGeneral_language_.
     */
    public ObjectProperty<LanguageBundle.languages> selectCmdGeneral_language_Property() {
        return this.selectCmdGeneral_language_;
    }

    /**
     * Getter overrided for the CompositeValidator of the class.
     *
     * @author Gaetan Brenckle
     *
     * @return - {@link CompositeValidator} - return the CompositeValidator of the class.
     */
    @Override
    public CompositeValidator getCompositeValidator() {
        return this.generalValidator_;
    }

    /**
     * Getter overrided for the compositeCommand of the class.
     *
     * @author Gaetan Brenckle
     *
     * @return - {@link CompositeCommand_Remastered} - return the CompositeCommand_Remastered of the class.
     */
    @Override
    public CompositeCommand_Remastered getComposite_Command() {
        return this.general_Command_;
    }


    /**
     * Listener for the ressource bundle.
     *
     * @param observable - {@link ObservableValue} - the value observed
     * @param oldValue - {@link ResourceBundle} - the old value that are remplaced
     * @param newValue - {@link ResourceBundle} - the new value
     */
    private void listener_bundleLanguage(ObservableValue<? extends ResourceBundle> observable, ResourceBundle oldValue, ResourceBundle newValue) {
        this.tPaneGeneral_synchronizeLabel_.set(this.resBundle_.get().getString("titleGeneral_synchronizeRightsLabel"));
        this.txtGeneral_synchronizeRightsLabel_.set(this.resBundle_.get().getString("txtGeneral_synchronizeRightsLabel"));

        this.tPaneGeneral_languageLabel_.set(this.resBundle_.get().getString("titleGeneral_Language"));
        this.txtGeneral_languageLabel_.set(this.resBundle_.get().getString("txtGeneral_languageLabel"));
    }


    @Override
    public void onViewAdded_Cycle() {
        // default implementation
    }

    @Override
    public void onViewRemoved_Cycle() {
        if (this.listener_ChangedValue_bundleLanguage_ != null) {
            this.resBundle_.removeListener(this.listener_ChangedValue_bundleLanguage_);
            this.listener_ChangedValue_bundleLanguage_ = null;
        }
        LanguageBundle.getInstance().unbindRessourceBundle(this.resBundle_);
    }
}