package fr.univlr.debathon.application.viewmodel.mainwindow.debate;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.saxsys.mvvmfx.utils.validation.CompositeValidator;
import de.saxsys.mvvmfx.utils.validation.ObservableRuleBasedValidator;
import de.saxsys.mvvmfx.utils.validation.ValidationMessage;
import de.saxsys.mvvmfx.utils.validation.ValidationStatus;
import fr.univlr.debathon.application.communication.Debathon;
import fr.univlr.debathon.application.viewmodel.ViewModel_SceneCycle;
import fr.univlr.debathon.job.db_project.jobclass.Room;
import fr.univlr.debathon.language.LanguageBundle;
import fr.univlr.debathon.log.generate.CustomLogger;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import org.apache.commons.validator.routines.EmailValidator;
import org.controlsfx.control.Notifications;

import java.util.ResourceBundle;

public class InscriptionStatViewModel extends ViewModel_SceneCycle {
    private static final CustomLogger LOGGER = CustomLogger.create(InscriptionStatViewModel.class.getName());
    private final ObjectProperty<ResourceBundle> resBundle_ = LanguageBundle.getInstance().bindResourceBundle("properties.language.mainwindow.debate.lg_inscription");

    private final Room debate;

    //Text
    private final StringProperty lblSendMail_label = new SimpleStringProperty(this.resBundle_.get().getString("lblSendMail"));
    private final StringProperty lblIrreversible_label = new SimpleStringProperty(this.resBundle_.get().getString("lblIrreversible"));

    //Value
    private final StringProperty tfMail_value = new SimpleStringProperty();

    private final ObservableRuleBasedValidator rule_Mail = new ObservableRuleBasedValidator();
    private final CompositeValidator validator_Mail = new CompositeValidator();

    private ChangeListener<ResourceBundle> listener_ChangedValue_bundleLanguage_;


    public InscriptionStatViewModel(Room debate) {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][constructor] Creation of the InscriptionStatViewModel() object.");
        }

        this.debate = debate;
        rule_Mail.addRule(createRule_Mail());
        validator_Mail.addValidators(rule_Mail);

        //ResourceBundle Listener
        this.listener_ChangedValue_bundleLanguage_ = this::listener_bundleLanguage;
        this.resBundle_.addListener(this.listener_ChangedValue_bundleLanguage_);
    }


    public void actvm_btnValid() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][method] Usage of the InscriptionStatViewModel.actvm_btnValid()");
        }

        try {
            Debathon.getInstance().getAppCommunication().sendEmail(this.debate.getId(), this.tfMail_value.get());

            Notifications.create()
                    .position(Pos.BOTTOM_RIGHT)
                    .title(this.resBundle_.get().getString("mail_title"))
                    .text(this.resBundle_.get().getString("mail_text"))
                    .showInformation();

        } catch (JsonProcessingException e) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(String.format("Mail register for statistics [%s]", this.tfMail_value.get()));
            }
        }

        this.tfMail_value.set("");
    }

    /**
     * Rule for the question.
     *
     * @return {@link ObjectBinding} - the rule
     */
    private ObjectBinding<ValidationMessage> createRule_Mail() {

        return Bindings.createObjectBinding(() -> {
            if (this.tfMail_value.get() == null || this.tfMail_value.isEmpty().get()) {
                return ValidationMessage.error(this.resBundle_.get().getString("rule_null"));
            }

            if (!EmailValidator.getInstance().isValid(tfMail_value.get())) {
                return ValidationMessage.error(this.resBundle_.get().getString("rule_invalid"));
            }
            return null;
        }, this.tfMail_value);
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
     * Current validation for the rule rule_Mail.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link ValidationStatus} - validation for the rule rule_Mail.
     */
    public ValidationStatus rule_Mail() {
        return rule_Mail.getValidationStatus();
    }

    /**
     * CompositeValidator of the variable validator_Mail.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link CompositeValidator} - CompositeValidator of the variable validator_Mail.
     */
    public CompositeValidator getValidator_Mail() {
        return validator_Mail;
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
