package fr.univlr.debathon.application.viewmodel.mainwindow.debate;

import fr.univlr.debathon.application.viewmodel.ViewModel_SceneCycle;
import fr.univlr.debathon.language.LanguageBundle;
import fr.univlr.debathon.log.generate.CustomLogger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.util.ResourceBundle;

public class InscriptionStatViewModel extends ViewModel_SceneCycle {
    private static final CustomLogger LOGGER = CustomLogger.create(InscriptionStatViewModel.class.getName());
    private final ObjectProperty<ResourceBundle> resBundle_ = LanguageBundle.getInstance().bindResourceBundle("properties.language.mainwindow.debate.lg_inscription");

    //Text
    private final StringProperty lblSendMail_label = new SimpleStringProperty(this.resBundle_.get().getString("lblSendMail"));
    private final StringProperty lblIrreversible_label = new SimpleStringProperty(this.resBundle_.get().getString("lblIrreversible"));

    //Value
    private final StringProperty tfMail_value = new SimpleStringProperty();

    private ChangeListener<ResourceBundle> listener_ChangedValue_bundleLanguage_;


    public InscriptionStatViewModel() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][constructor] Creation of the InscriptionStatViewModel() object.");
        }

        //ResourceBundle Listener
        this.listener_ChangedValue_bundleLanguage_ = this::listener_bundleLanguage;
        this.resBundle_.addListener(this.listener_ChangedValue_bundleLanguage_);
    }


    public void actvm_btnValid() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][method] Usage of the InscriptionStatViewModel.actvm_btnValid()");
        }

        //TODO Inscription Valid
    }


    /**
     * Property of the variable lblSendMail_label.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link StringProperty} - return the property of the variable lblSendMail_label.
     */
    public StringProperty lblSendMail_labelProperty() {
        return lblSendMail_label;
    }

    /**
     * Property of the variable lblIrreversible_label.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link StringProperty} - return the property of the variable lblIrreversible_label.
     */
    public StringProperty lblIrreversible_labelProperty() {
        return lblIrreversible_label;
    }

    /**
     * Property of the variable tfMail_value.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link StringProperty} - return the property of the variable tfMail_value.
     */
    public StringProperty tfMail_valueProperty() {
        return tfMail_value;
    }


    /**
     * Listener for the resource bundle.
     *
     * @param observable - {@link ObservableValue} - the value observed
     * @param oldValue - {@link ResourceBundle} - the old value that are replaced
     * @param newValue - {@link ResourceBundle} - the new value
     */
    private void listener_bundleLanguage(ObservableValue<? extends ResourceBundle> observable, ResourceBundle oldValue, ResourceBundle newValue) {
        this.lblSendMail_label.set(this.resBundle_.get().getString("lblSendMail"));
        this.lblIrreversible_label.set(this.resBundle_.get().getString("lblIrreversible"));
    }


    @Override
    public void onViewAdded_Cycle() {
    }

    @Override
    public void onViewRemoved_Cycle() {
    }
}
