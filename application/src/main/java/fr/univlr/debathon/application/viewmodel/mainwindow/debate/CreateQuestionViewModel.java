package fr.univlr.debathon.application.viewmodel.mainwindow.debate;

import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.InjectScope;
import de.saxsys.mvvmfx.ScopeProvider;
import de.saxsys.mvvmfx.ViewTuple;
import de.saxsys.mvvmfx.utils.validation.CompositeValidator;
import de.saxsys.mvvmfx.utils.validation.ObservableRuleBasedValidator;
import de.saxsys.mvvmfx.utils.validation.ValidationMessage;
import de.saxsys.mvvmfx.utils.validation.ValidationStatus;
import fr.univlr.debathon.application.Launch;
import fr.univlr.debathon.application.view.mainwindow.SelectWindowView;
import fr.univlr.debathon.application.view.mainwindow.debate.items.CategoryView;
import fr.univlr.debathon.application.view.mainwindow.debate.items.TagView;
import fr.univlr.debathon.application.viewmodel.ViewModel_SceneCycle;
import fr.univlr.debathon.application.viewmodel.mainwindow.HomePageViewModel;
import fr.univlr.debathon.application.viewmodel.mainwindow.debate.items.CategoryViewModel;
import fr.univlr.debathon.application.viewmodel.mainwindow.debate.items.SelectCategoryScope;
import fr.univlr.debathon.application.viewmodel.mainwindow.debate.items.SelectTagScope;
import fr.univlr.debathon.application.viewmodel.mainwindow.debate.items.TagViewModel;
import fr.univlr.debathon.job.db_project.jobclass.Mcq;
import fr.univlr.debathon.job.db_project.jobclass.Question;
import fr.univlr.debathon.language.LanguageBundle;
import fr.univlr.debathon.log.generate.CustomLogger;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.PopOver;

import java.util.Optional;
import java.util.ResourceBundle;

public class CreateQuestionViewModel extends ViewModel_SceneCycle {

    private final ObjectProperty<ResourceBundle> resBundle_ = LanguageBundle.getInstance().bindResourceBundle("properties.language.mainwindow.debate.lg_createQuestion");
    private static final CustomLogger LOGGER = CustomLogger.create(HomePageViewModel.class.getName());

    //Text
    private final StringProperty titledCreate_label = new SimpleStringProperty(this.resBundle_.get().getString("titledCreate"));
    private final StringProperty lblQuestion_label = new SimpleStringProperty(this.resBundle_.get().getString("lblQuestion"));

    private final StringProperty lblQuestionType_label = new SimpleStringProperty(this.resBundle_.get().getString("lblQuestionType"));

    private final StringProperty lblReponses_label = new SimpleStringProperty(this.resBundle_.get().getString("lblReponses"));
    private final StringProperty tabReponses_label = new SimpleStringProperty(this.resBundle_.get().getString("tabResponse"));

    private final StringProperty btnValid_label = new SimpleStringProperty(this.resBundle_.get().getString("btnValid"));

    //Value
    private final StringProperty taQuestion_value = new SimpleStringProperty();
    private final ListProperty<Question.Type> cbQuestionType_value = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final ObjectProperty<Question.Type> SelectedQuestionType_value = new SimpleObjectProperty<>(Question.Type.MULTIPLE);
    private final BooleanProperty isShowedResponses = new SimpleBooleanProperty(true);
    private final ListProperty<Mcq> listResponses_value = new SimpleListProperty<>(FXCollections.observableArrayList());

    private final ObservableRuleBasedValidator rule_Question = new ObservableRuleBasedValidator();
    private final ObservableRuleBasedValidator rule_QuestionType = new ObservableRuleBasedValidator();
    private final ObservableRuleBasedValidator rule_Responses = new ObservableRuleBasedValidator();
    private final CompositeValidator validator_CreateQuestion = new CompositeValidator();

    private ChangeListener<ResourceBundle> listener_ChangedValue_bundleLanguage_;


    /**
     * Default constructor
     *
     * @author Gaetan Brenckle
     */
    public CreateQuestionViewModel() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][constructor] Creation of the HomePageViewModel() object.");
        }

        rule_Question.addRule(createRule_Question());
        rule_QuestionType.addRule(createRule_QuestionType());
        rule_Responses.addRule(createRule_Responses());
        validator_CreateQuestion.addValidators(rule_Question, rule_QuestionType, rule_Responses);

        this.cbQuestionType_value.addAll(Question.Type.values());

        //ResourceBundle Listener
        this.listener_ChangedValue_bundleLanguage_ = this::listener_bundleLanguage;
        this.resBundle_.addListener(this.listener_ChangedValue_bundleLanguage_);
    }


    /**
     * Rule for the question.
     *
     * @return {@link ObjectBinding} - the rule
     */
    private ObjectBinding<ValidationMessage> createRule_Question() {

        return Bindings.createObjectBinding(() -> {
            if (this.taQuestion_value.get() == null || this.taQuestion_value.isEmpty().get()) {
                return ValidationMessage.error(this.resBundle_.get().getString("rule_null"));
            }
            return null;
        }, this.taQuestion_value);
    }

    /**
     * Rule for the type question.
     *
     *
     * @return {@link ObjectBinding} - the rule
     */
    private ObjectBinding<ValidationMessage> createRule_QuestionType() {

        return Bindings.createObjectBinding(() -> {
            if (this.cbQuestionType_value.get() == null || this.cbQuestionType_value.isEmpty()) {
                return ValidationMessage.error(this.resBundle_.get().getString("rule_null"));
            }
            if (this.selectedQuestionType_valueProperty().get().equals(Question.Type.LIBRE) && isShowedResponses.get()) {
                return ValidationMessage.error(this.resBundle_.get().getString("rule_Showed"));
            }
            return null;
        }, this.cbQuestionType_value);
    }

    /**
     * Rule for the responses.
     * Will fail if no responses, less that two or empty responses exists.
     *
     * @return {@link ObjectBinding} - the rule
     */
    private ObjectBinding<ValidationMessage> createRule_Responses() {

        return Bindings.createObjectBinding(() -> {
            if (this.listResponses_value.get() == null || this.listResponses_value.isEmpty()) {
                return ValidationMessage.error(this.resBundle_.get().getString("rule_null"));
            }
            if (this.listResponses_value.size() < 2) {
                return ValidationMessage.error(this.resBundle_.get().getString("rule_inferior"));
            }

            for (Mcq mcq : listResponses_value) {
                if (mcq.getLabel() == null || mcq.getLabel().isEmpty()) {
                    return ValidationMessage.error(this.resBundle_.get().getString("rule_ResponsesEmpty"));
                }
            }

            return null;
        }, this.listResponses_value);
    }


    public void actvm_ValidCreateDebate() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][method] Usage of the CreateDebateViewModel.actvm_ValidCreateDebate()");
        }

        //TODO valid Create

        //Reset fields
        this.taQuestion_value.set("");
        this.selectedQuestionType_valueProperty().set(Question.Type.MULTIPLE);
        this.listResponses_value.clear();

        Notifications.create()
                .position(Pos.BOTTOM_RIGHT)
                .title(this.resBundle_.get().getString("createDebate_title"))
                .text(this.resBundle_.get().getString("createDebate_text"))
                .showInformation();
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
     * Property of the variable lblQuestion_label.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link StringProperty} - return the property of the variable lblQuestion_label.
     */
    public StringProperty lblQuestion_labelProperty() {
        return lblQuestion_label;
    }

    /**
     * Property of the variable lblQuestionType_label.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link StringProperty} - return the property of the variable lblQuestionType_label.
     */
    public StringProperty lblQuestionType_labelProperty() {
        return lblQuestionType_label;
    }

    /**
     * Property of the variable lblReponses_label.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link StringProperty} - return the property of the variable lblReponses_label.
     */
    public StringProperty lblReponses_labelProperty() {
        return lblReponses_label;
    }

    /**
     * Property of the variable lblReponses_label.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link StringProperty} - return the property of the variable lblReponses_label.
     */
    public StringProperty tabReponses_labelProperty() {
        return tabReponses_label;
    }

    /**
     * Property of the variable btnValid_label.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link StringProperty} - return the property of the variable btnValid_label.
     */
    public StringProperty btnValid_labelProperty() {
        return btnValid_label;
    }

    /**
     * Property of the variable taQuestion_value.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link StringProperty} - return the property of the variable taQuestion_value.
     */
    public StringProperty taQuestion_valueProperty() {
        return taQuestion_value;
    }

    /**
     * Property of the variable cbQuestionType_value.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link ListProperty} - return the property of the variable cbQuestionType_value.
     */
    public ListProperty<Question.Type> cbQuestionType_valueProperty() {
        return cbQuestionType_value;
    }

    /**
     * Property of the variable SelectedQuestionType_value.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link ObjectProperty} - return the property of the variable SelectedQuestionType_value.
     */
    public ObjectProperty<Question.Type> selectedQuestionType_valueProperty() {
        return SelectedQuestionType_value;
    }

    /**
     * Property of the variable listResponses_value.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link ListProperty} - return the property of the variable listResponses_value.
     */
    public ListProperty<Mcq> listResponses_valueProperty() {
        return listResponses_value;
    }


    /**
     * Current validation for the rule rule_title.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link ValidationStatus} - validation for the rule rule_title.
     */
    public ValidationStatus rule_Question() {
        return rule_Question.getValidationStatus();
    }

    /**
     * Current validation for the rule rule_Key.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link ValidationStatus} - validation for the rule rule_Key.
     */
    public ValidationStatus rule_QuestionType() {
        return rule_QuestionType.getValidationStatus();
    }

    /**
     * CompositeValidator of the variable validator_CreateDebate.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link CompositeValidator} - CompositeValidator of the variable validator_CreateDebate.
     */
    public CompositeValidator getValidator_CreateQuestion() {
        return validator_CreateQuestion;
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
        this.lblQuestion_label.set(this.resBundle_.get().getString("lblQuestion"));

        this.lblQuestionType_label.set(this.resBundle_.get().getString("lblQuestionType"));

        this.lblReponses_label.set(this.resBundle_.get().getString("lblReponses"));
        this.tabReponses_label.set(this.resBundle_.get().getString("tabResponse"));

        this.btnValid_label.set(this.resBundle_.get().getString("btnValid"));

    }



    @Override
    public void onViewAdded_Cycle() {
    }

    @Override
    public void onViewRemoved_Cycle() {
    }
}
