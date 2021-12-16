package fr.univlr.debathon.application.viewmodel.mainwindow;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.InjectScope;
import de.saxsys.mvvmfx.ScopeProvider;
import de.saxsys.mvvmfx.ViewTuple;
import de.saxsys.mvvmfx.utils.validation.CompositeValidator;
import de.saxsys.mvvmfx.utils.validation.ObservableRuleBasedValidator;
import de.saxsys.mvvmfx.utils.validation.ValidationMessage;
import de.saxsys.mvvmfx.utils.validation.ValidationStatus;
import fr.univlr.debathon.application.Launch;
import fr.univlr.debathon.application.communication.Debathon;
import fr.univlr.debathon.application.view.mainwindow.SelectWindowView;
import fr.univlr.debathon.application.view.mainwindow.debate.items.CategoryView;
import fr.univlr.debathon.application.view.mainwindow.debate.items.TagView;
import fr.univlr.debathon.application.viewmodel.ViewModel_SceneCycle;
import fr.univlr.debathon.application.viewmodel.mainwindow.debate.items.CategoryViewModel;
import fr.univlr.debathon.application.viewmodel.mainwindow.debate.items.SelectCategoryScope;
import fr.univlr.debathon.application.viewmodel.mainwindow.debate.items.SelectTagScope;
import fr.univlr.debathon.application.viewmodel.mainwindow.debate.items.TagViewModel;
import fr.univlr.debathon.job.db_project.jobclass.Category;
import fr.univlr.debathon.job.db_project.jobclass.Room;
import fr.univlr.debathon.job.db_project.jobclass.Tag;
import fr.univlr.debathon.language.LanguageBundle;
import fr.univlr.debathon.log.generate.CustomLogger;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.PopOver;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

@ScopeProvider(scopes= {SelectCategoryScope.class, SelectTagScope.class})
public class CreateDebateViewModel extends ViewModel_SceneCycle {

    private final ObjectProperty<ResourceBundle> resBundle_ = LanguageBundle.getInstance().bindResourceBundle("properties.language.mainwindow.lg_createDebate");
    private static final CustomLogger LOGGER = CustomLogger.create(HomePageViewModel.class.getName());

    //Text
    private final StringProperty titledCreate_label = new SimpleStringProperty(this.resBundle_.get().getString("titledCreate"));
    private final StringProperty lblTitle_label = new SimpleStringProperty(this.resBundle_.get().getString("lblTitle"));

    private final StringProperty btnAddItem_label = new SimpleStringProperty(this.resBundle_.get().getString("btnAddItem"));
    private final StringProperty lblDescription_label = new SimpleStringProperty(this.resBundle_.get().getString("lblDescription"));

    private final StringProperty lblKey_label = new SimpleStringProperty(this.resBundle_.get().getString("lblKey"));

    private final StringProperty btnValid_label = new SimpleStringProperty(this.resBundle_.get().getString("btnValid"));

    //Value
    private final StringProperty tfTitle_value = new SimpleStringProperty();
    private final ObjectProperty<ViewTuple<CategoryView, CategoryViewModel>> category_selected_value = new SimpleObjectProperty<>();
    private final ListProperty<ViewTuple<TagView, TagViewModel> > listTag_selected_value = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final StringProperty htmlEditorDescription_value = new SimpleStringProperty();
    private final StringProperty tfKey_value = new SimpleStringProperty();

    private final ObservableRuleBasedValidator rule_title = new ObservableRuleBasedValidator();
    private final ObservableRuleBasedValidator rule_Key = new ObservableRuleBasedValidator();
    private final CompositeValidator validator_CreateDebate = new CompositeValidator();

    private ChangeListener<Category> changeListener_Category;
    private ListChangeListener<Tag> listChangeListener_Tag;
    private ChangeListener<ResourceBundle> listener_ChangedValue_bundleLanguage_;

    @InjectScope
    private SelectCategoryScope selectCategoryScope;

    @InjectScope
    private SelectTagScope selectTagScope;

    private PopOver popOver_selectItems;


    /**
     * Default constructor
     *
     * @author Gaetan Brenckle
     */
    public CreateDebateViewModel() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][constructor] Creation of the HomePageViewModel() object.");
        }

        rule_title.addRule(createRule_title());
        rule_Key.addRule(createRule_key());
        validator_CreateDebate.addValidators(rule_title, rule_Key);

        //ResourceBundle Listener
        this.listener_ChangedValue_bundleLanguage_ = this::listener_bundleLanguage;
        this.resBundle_.addListener(this.listener_ChangedValue_bundleLanguage_);
    }

    public void initialize() {
        popOver_selectItems = new PopOver(FluentViewLoader.fxmlView(SelectWindowView.class)
                .providedScopes(selectCategoryScope, selectTagScope)
                .load().getView());
        popOver_selectItems.setDetachable(false);
        popOver_selectItems.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);

        Launch.APPLICATION_STOP.addListener(new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (popOver_selectItems != null) {
                    popOver_selectItems.hide(Duration.millis(0));
                }
                Launch.APPLICATION_STOP.removeListener(this);
            }
        });

        this.bindCreate();
    }

    private void bindCreate() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[private][method] Usage of the CreateDebateViewModel.bindCreate()");
        }

        this.changeListener_Category = (observableValue, oldValue, newValue) -> {
            if (newValue != null) {
                CategoryViewModel categoryViewModel = new CategoryViewModel(newValue);
                final ViewTuple<CategoryView, CategoryViewModel> categoryViewTuple = FluentViewLoader.fxmlView(CategoryView.class)
                        .viewModel(categoryViewModel)
                        .load();

                this.category_selected_value.set(categoryViewTuple);
            } else {
                this.category_selected_value.set(null);
            }
        };
        this.selectCategoryScope.selectedCategoryProperty().addListener(this.changeListener_Category);

        //-----------------------

        this.selectTagScope.selectedTagsProperty().forEach(tag -> {
            if (tag != null) {
                TagViewModel tagViewModel = new TagViewModel(tag);
                final ViewTuple<TagView, TagViewModel> tagViewTuple = FluentViewLoader.fxmlView(TagView.class)
                        .viewModel(tagViewModel)
                        .load();

                listTag_selected_value.add(tagViewTuple);
            }
        });

        this.listChangeListener_Tag = change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().forEach(item ->
                            Platform.runLater(() -> {
                                if (item != null) {
                                    TagViewModel tagViewModel = new TagViewModel(item);
                                    final ViewTuple<TagView, TagViewModel> tagViewTuple = FluentViewLoader.fxmlView(TagView.class)
                                            .viewModel(tagViewModel)
                                            .load();

                                    listTag_selected_value.add(tagViewTuple);
                                }
                            })
                    );
                } else if (change.wasRemoved()) {
                    change.getRemoved().forEach(item ->
                            Platform.runLater(() -> {
                                Optional<ViewTuple<TagView, TagViewModel>> optional = listTag_selected_value.stream().filter(tag -> tag.getViewModel().getTag().equals(item)).findAny();
                                optional.ifPresent(listTag_selected_value::remove);
                            })
                    );
                }
            }
        };
        this.selectTagScope.selectedTagsProperty().addListener(this.listChangeListener_Tag);
    }

    /**
     * Rule for the title.
     *
     * @return {@link ObjectBinding} - the rule
     */
    private ObjectBinding<ValidationMessage> createRule_title() {

        return Bindings.createObjectBinding(() -> {
            if (this.tfTitle_value.get() == null || this.tfTitle_value.isEmpty().get()) {
                return ValidationMessage.error(this.resBundle_.get().getString("rule_null"));
            }
            return null;
        }, this.tfTitle_value);
    }

    /**
     * Rule for the key.
     *
     * @return {@link ObjectBinding} - the rule
     */
    private ObjectBinding<ValidationMessage> createRule_key() {

        return Bindings.createObjectBinding(() -> {
            if (this.tfKey_value.get() == null || this.tfKey_value.isEmpty().get()) {
                return ValidationMessage.error(this.resBundle_.get().getString("rule_null"));
            }
            return null;
        }, this.tfKey_value);
    }

    /**
     * Show a popover to select categories and tags.
     *
     * @author Gaetan Brenckle
     * @param node - {@link Node} - node used to show the popover
     */
    public void actvm_AddItem(Node node) {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][method] Usage of the CreateDebateViewModel.actvm_AddItem()");
        }

        if (popOver_selectItems.isShowing()) {
            popOver_selectItems.hide();
        } else {
            popOver_selectItems.show(node);
        }
    }


    public void actvm_ValidCreateDebate() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][method] Usage of the CreateDebateViewModel.actvm_ValidCreateDebate()");
        }

        try {
            final List<Tag> listTag = new ArrayList<>();
            this.listTag_selected_value.forEach(tag -> {
                listTag.add(tag.getViewModel().getTag());
            });

            final Room newRoom = new Room(
                    this.tfTitle_value.get(),
                    this.htmlEditorDescription_value.get(),
                    this.tfKey_value.get(),
                    this.category_selected_value.get().getViewModel().getCategory(),
                    listTag
            );

            Debathon.getInstance().getAppCommunication().requestInsertNewRoom(newRoom);
        } catch (JsonProcessingException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Error when the user want to create a debate", e);
            }

            Notifications.create()
                    .position(Pos.BOTTOM_RIGHT)
                    .title(this.resBundle_.get().getString("createDebate_title_error"))
                    .text(this.resBundle_.get().getString("createDebate_text_error"))
                    .showInformation();
            return;
        }

        //Reset fields
        this.tfTitle_value.set("");
        this.selectCategoryScope.selectedCategoryProperty().set(null);
        this.selectTagScope.selectedTagsProperty().clear();

        //To reinitialize the selected values
        popOver_selectItems = new PopOver(FluentViewLoader.fxmlView(SelectWindowView.class)
                .providedScopes(selectCategoryScope, selectTagScope)
                .load().getView());
        popOver_selectItems.setDetachable(false);
        popOver_selectItems.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
        this.htmlEditorDescription_value.set("");
        this.tfKey_value.set("");

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
     * Property of the variable category_selected_value.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link ObjectProperty} - return the property of the variable category_selected_value.
     */
    public ObjectProperty<ViewTuple<CategoryView, CategoryViewModel>> category_selected_valueProperty() {
        return category_selected_value;
    }

    /**
     * Property of the variable listTag_selected_value.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link ListProperty} - return the property of the variable listTag_selected_value.
     */
    public ListProperty<ViewTuple<TagView, TagViewModel>> listTag_selected_valueProperty() {
        return listTag_selected_value;
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
     * Property of the variable tPaneParameters_label.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link StringProperty} - return the property of the variable tPaneParameters_label.
     */
    public StringProperty tfKey_valueProperty() {
        return tfKey_value;
    }

    /**
     * Current validation for the rule rule_title.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link ValidationStatus} - validation for the rule rule_title.
     */
    public ValidationStatus rule_title() {
        return rule_title.getValidationStatus();
    }

    /**
     * Current validation for the rule rule_Key.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link ValidationStatus} - validation for the rule rule_Key.
     */
    public ValidationStatus rule_Key() {
        return rule_Key.getValidationStatus();
    }

    /**
     * CompositeValidator of the variable validator_CreateDebate.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link CompositeValidator} - CompositeValidator of the variable validator_CreateDebate.
     */
    public CompositeValidator getValidator_CreateDebate() {
        return validator_CreateDebate;
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

        this.lblKey_label.set(this.resBundle_.get().getString("lblKey"));

        this.btnValid_label.set(this.resBundle_.get().getString("btnValid"));
    }



    @Override
    public void onViewAdded_Cycle() {
    }

    @Override
    public void onViewRemoved_Cycle() {
    }
}
