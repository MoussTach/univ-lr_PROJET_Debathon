package fr.univlr.debathon.application.viewmodel.mainwindow.debate;

import fr.univlr.debathon.application.viewmodel.ViewModel_SceneCycle;
import fr.univlr.debathon.application.viewmodel.mainwindow.HomePageViewModel;
import fr.univlr.debathon.language.LanguageBundle;
import fr.univlr.debathon.log.generate.CustomLogger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.util.ResourceBundle;

public class CreateDebateViewModel extends ViewModel_SceneCycle {

    private final ObjectProperty<ResourceBundle> resBundle_ = LanguageBundle.getInstance().bindResourceBundle("properties.language.mainwindow.lg_createDebate");
    private static final CustomLogger LOGGER = CustomLogger.create(HomePageViewModel.class.getName());

    //Text
    private final StringProperty titledCreate_label = new SimpleStringProperty(this.resBundle_.get().getString("titledCreate"));
    private final StringProperty lblTitle_label = new SimpleStringProperty(this.resBundle_.get().getString("lblTitle"));

    private final StringProperty btnAddItem_label = new SimpleStringProperty(this.resBundle_.get().getString("btnAddItem"));
    private final StringProperty lblDescription_label = new SimpleStringProperty(this.resBundle_.get().getString("lblDescription"));
    private final StringProperty btnValid_label = new SimpleStringProperty(this.resBundle_.get().getString("btnValid"));

    //Value
    private final StringProperty tfTitle_value = new SimpleStringProperty();
    private final StringProperty htmlEditorDescription_value = new SimpleStringProperty();

    private ChangeListener<ResourceBundle> listener_ChangedValue_bundleLanguage_;

    /**
     * Default constructor
     *
     * @author Gaetan Brenckle
     */
    public CreateDebateViewModel() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][constructor] Creation of the HomePageViewModel() object.");
        }

        //ResourceBundle Listener
        this.listener_ChangedValue_bundleLanguage_ = this::listener_bundleLanguage;
        this.resBundle_.addListener(this.listener_ChangedValue_bundleLanguage_);

    }

    /**
     * Property of the variable tPaneParameters_label.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link StringProperty} - return the property of the variable tPaneParameters_label.
     */
    public StringProperty titledCreate_labelProperty() {
        return titledCreate_label;
    }

    /**
     * Property of the variable tPaneParameters_label.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link StringProperty} - return the property of the variable tPaneParameters_label.
     */
    public StringProperty lblTitle_labelProperty() {
        return lblTitle_label;
    }

    /**
     * Property of the variable tPaneParameters_label.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link StringProperty} - return the property of the variable tPaneParameters_label.
     */
    public StringProperty btnAddItem_labelProperty() {
        return btnAddItem_label;
    }

    /**
     * Property of the variable tPaneParameters_label.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link StringProperty} - return the property of the variable tPaneParameters_label.
     */
    public StringProperty lblDescription_labelProperty() {
        return lblDescription_label;
    }

    /**
     * Property of the variable tPaneParameters_label.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link StringProperty} - return the property of the variable tPaneParameters_label.
     */
    public StringProperty btnValid_labelProperty() {
        return btnValid_label;
    }

    /**
     * Property of the variable tPaneParameters_label.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link StringProperty} - return the property of the variable tPaneParameters_label.
     */
    public StringProperty tfTitle_valueProperty() {
        return tfTitle_value;
    }

    /**
     * Property of the variable tPaneParameters_label.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link StringProperty} - return the property of the variable tPaneParameters_label.
     */
    public StringProperty htmlEditorDescription_valueProperty() {
        return htmlEditorDescription_value;
    }


    /**
     * Listener for the resource bundle.
     *
     * @param observable - {@link ObservableValue} - the value observed
     * @param oldValue - {@link ResourceBundle} - the old value that are replaced
     * @param newValue - {@link ResourceBundle} - the new value
     */
    private void listener_bundleLanguage(ObservableValue<? extends ResourceBundle> observable, ResourceBundle oldValue, ResourceBundle newValue) {
        this.titledCreate_label.set(this.resBundle_.get().getString("titledCreate"));
        this.lblTitle_label.set(this.resBundle_.get().getString("lblTitle"));

        this.btnAddItem_label.set(this.resBundle_.get().getString("btnAddItem"));
        this.lblDescription_label.set(this.resBundle_.get().getString("lblDescription"));
        this.btnValid_label.set(this.resBundle_.get().getString("btnValid"));
    }



    @Override
    public void onViewAdded_Cycle() {
    }

    @Override
    public void onViewRemoved_Cycle() {
        if (this.listener_ChangedValue_bundleLanguage_ != null) {
            this.resBundle_.removeListener(this.listener_ChangedValue_bundleLanguage_);
            this.listener_ChangedValue_bundleLanguage_ = null;
        }

        LanguageBundle.getInstance().unbindResourceBundle(this.resBundle_);
    }
}
