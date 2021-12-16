package fr.univlr.debathon.application.viewmodel.mainwindow;

import fr.univlr.debathon.application.viewmodel.ViewModel_SceneCycle;
import fr.univlr.debathon.language.LanguageBundle;
import fr.univlr.debathon.log.generate.CustomLogger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import org.controlsfx.control.Notifications;

import java.util.ResourceBundle;

public class KeyWindowViewModel extends ViewModel_SceneCycle {

    private final ObjectProperty<ResourceBundle> resBundle_ = LanguageBundle.getInstance().bindResourceBundle("properties.language.mainwindow.lg_keyManagement");
    private static final CustomLogger LOGGER = CustomLogger.create(KeyWindowViewModel.class.getName());

    private final StringProperty key = new SimpleStringProperty();

    //Text
    private final StringProperty lblKey_label = new SimpleStringProperty(this.resBundle_.get().getString("lblKey"));

    //Value
    private final StringProperty tfKey_value = new SimpleStringProperty();

    private ChangeListener<ResourceBundle> listener_ChangedValue_bundleLanguage_;

    public KeyWindowViewModel() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][constructor] Creation of the KeyWindowViewModel() object.");
        }

        //ResourceBundle Listener
        this.listener_ChangedValue_bundleLanguage_ = this::listener_bundleLanguage;
        this.resBundle_.addListener(this.listener_ChangedValue_bundleLanguage_);
    }


    public void actvm_btnValidKey() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][method] Usage of the CreateQuestionViewModel.actvm_btnValidKey()");
        }

        //TODO print
        System.out.println(this.key.get());
        if (this.tfKey_value.get().equals(this.key.get())) {
            this.key.set("");

            Notifications.create()
                    .position(Pos.BOTTOM_RIGHT)
                    .title(this.resBundle_.get().getString("key_title"))
                    .text(this.resBundle_.get().getString("key_text"))
                    .showInformation();
        } else {

            Notifications.create()
                    .position(Pos.BOTTOM_RIGHT)
                    .title(this.resBundle_.get().getString("key_title_bad"))
                    .text(this.resBundle_.get().getString("key_text_bad"))
                    .showWarning();
        }
    }

    /**
     * Property of the variable lblKey_label.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link StringProperty} - return the property of the variable lblKey_label.
     */
    public StringProperty keyProperty() {
        return key;
    }

    /**
     * Property of the variable lblKey_label.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link StringProperty} - return the property of the variable lblKey_label.
     */
    public StringProperty lblKey_labelProperty() {
        return lblKey_label;
    }

    /**
     * Property of the variable tfKey_value.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link StringProperty} - return the property of the variable tfKey_value.
     */
    public StringProperty tfKey_valueProperty() {
        return tfKey_value;
    }


    /**
     * Listener for the resource bundle.
     *
     * @param observable - {@link ObservableValue} - the value observed
     * @param oldValue - {@link ResourceBundle} - the old value that are replaced
     * @param newValue - {@link ResourceBundle} - the new value
     */
    private void listener_bundleLanguage(ObservableValue<? extends ResourceBundle> observable, ResourceBundle oldValue, ResourceBundle newValue) {
        this.lblKey_label.set(this.resBundle_.get().getString("lblKey"));
    }

    @Override
    public void onViewAdded_Cycle() {
    }

    @Override
    public void onViewRemoved_Cycle() {
    }
}
