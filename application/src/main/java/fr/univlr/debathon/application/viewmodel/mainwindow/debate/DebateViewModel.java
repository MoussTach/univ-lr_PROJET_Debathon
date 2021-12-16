package fr.univlr.debathon.application.viewmodel.mainwindow.debate;

import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.InjectScope;
import de.saxsys.mvvmfx.ViewTuple;
import de.saxsys.mvvmfx.utils.commands.Action;
import de.saxsys.mvvmfx.utils.commands.Command;
import de.saxsys.mvvmfx.utils.commands.CompositeCommand;
import de.saxsys.mvvmfx.utils.commands.DelegateCommand;
import fr.univlr.debathon.application.Launch;
import fr.univlr.debathon.application.communication.Debathon;
import fr.univlr.debathon.application.view.mainwindow.KeyWindowView;
import fr.univlr.debathon.application.view.mainwindow.debate.CreateQuestionView;
import fr.univlr.debathon.application.view.mainwindow.debate.InscriptionStatView;
import fr.univlr.debathon.application.view.mainwindow.debate.items.CategoryView;
import fr.univlr.debathon.application.view.mainwindow.debate.items.TagView;
import fr.univlr.debathon.application.view.mainwindow.debate.question.QuestionView;
import fr.univlr.debathon.application.viewmodel.ViewModel_SceneCycle;
import fr.univlr.debathon.application.viewmodel.mainwindow.KeyWindowViewModel;
import fr.univlr.debathon.application.viewmodel.mainwindow.MainViewScope;
import fr.univlr.debathon.application.viewmodel.mainwindow.debate.items.CategoryViewModel;
import fr.univlr.debathon.application.viewmodel.mainwindow.debate.items.TagViewModel;
import fr.univlr.debathon.application.viewmodel.mainwindow.debate.question.QuestionViewModel;
import fr.univlr.debathon.job.db_project.jobclass.Category;
import fr.univlr.debathon.job.db_project.jobclass.Question;
import fr.univlr.debathon.job.db_project.jobclass.Room;
import fr.univlr.debathon.job.db_project.jobclass.Tag;
import fr.univlr.debathon.language.LanguageBundle;
import fr.univlr.debathon.log.generate.CustomLogger;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.util.Duration;
import org.controlsfx.control.PopOver;

import java.util.Optional;
import java.util.ResourceBundle;

public class DebateViewModel extends ViewModel_SceneCycle {

    private final ObjectProperty<ResourceBundle> resBundle_ = LanguageBundle.getInstance().bindResourceBundle("properties.language.mainwindow.debate.lg_debate");
    private static final CustomLogger LOGGER = CustomLogger.create(DebateViewModel.class.getName());

    private final Room debate;
    private final StringProperty key_value = new SimpleStringProperty();

    private final ObjectProperty<BorderPane> borderPane = new SimpleObjectProperty<>(null);

    //Text
    private final StringProperty lblTitle_label = new SimpleStringProperty("/");
    private final StringProperty description_htmlText = new SimpleStringProperty("/");

    //Value
    private final ObjectProperty<ViewTuple<CategoryView, CategoryViewModel> > category_value = new SimpleObjectProperty<>();
    private final ListProperty<ViewTuple<TagView, TagViewModel> > listTag_value = new SimpleListProperty<>(FXCollections.observableArrayList());

    private final ListProperty<ViewTuple<QuestionView, QuestionViewModel> > listQuestion_value = new SimpleListProperty<>(FXCollections.observableArrayList());

    private final Command prevCommand = new DelegateCommand(() -> new Action() {
        @Override
        protected void action() {
            //TODO
            System.out.println("act prev Home");
            Platform.runLater(() -> {
                BorderPane mainBorderPane = mainViewScope.basePaneProperty().get();
                mainBorderPane.setCenter(borderPane.get());
            });
        }
    }, true);

    private ChangeListener<String> changeListener_key = null;
    private ChangeListener<Category> changeListener_category = null;
    private ListChangeListener<Tag> listChangeListener_tag = null;
    private ListChangeListener<Question> listChangeListener_question = null;

    @InjectScope
    private MainViewScope mainViewScope;

    private PopOver popOver_statMail;
    private PopOver popOver_createQuestion;


    /**
     * Default constructor
     *
     * @author Gaetan Brenckle
     */
    public DebateViewModel(Room debate) {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][constructor] Creation of the DebateViewModel() object.");
        }

        this.debate = debate;

        InscriptionStatViewModel inscriptionStatViewModel = new InscriptionStatViewModel(this.debate);
        popOver_statMail = new PopOver(FluentViewLoader.fxmlView(InscriptionStatView.class)
                .viewModel(inscriptionStatViewModel)
                .load().getView());
        popOver_statMail.setDetachable(false);
        popOver_statMail.setArrowLocation(PopOver.ArrowLocation.TOP_RIGHT);

        Launch.APPLICATION_STOP.addListener(new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (popOver_createQuestion != null) {
                    popOver_createQuestion.hide(Duration.millis(0));
                }
                if (popOver_statMail != null) {
                    popOver_statMail.hide(Duration.millis(0));
                }
                Launch.APPLICATION_STOP.removeListener(this);
            }
        });

        this.bindDebate();
    }

    private void bindDebate() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][method] Usage of the DebateViewModel.bindDebate()");
        }

        if (this.debate != null) {
            this.key_value.bind(this.debate.keyProperty());
            if (this.debate.getKey() == null || this.debate.getKey().isEmpty()) {
                CreateQuestionViewModel createQuestionViewModel = new CreateQuestionViewModel(this.debate);
                popOver_createQuestion = new PopOver(FluentViewLoader.fxmlView(CreateQuestionView.class)
                        .viewModel(createQuestionViewModel)
                        .load().getView());

            } else {
                KeyWindowViewModel keyWindowViewModel = new KeyWindowViewModel();
                keyWindowViewModel.keyProperty().bindBidirectional(this.debate.keyProperty());
                popOver_createQuestion = new PopOver(FluentViewLoader.fxmlView(KeyWindowView.class)
                        .viewModel(keyWindowViewModel)
                        .load().getView());
            }
            popOver_createQuestion.setDetachable(false);
            popOver_createQuestion.setArrowLocation(PopOver.ArrowLocation.TOP_RIGHT);

            this.changeListener_key = (observableValue, oldValue, newValue) -> {
                if (newValue == null || newValue.isEmpty()) {
                    CreateQuestionViewModel createQuestionViewModel = new CreateQuestionViewModel(this.debate);
                    popOver_createQuestion = new PopOver(FluentViewLoader.fxmlView(CreateQuestionView.class)
                            .viewModel(createQuestionViewModel)
                            .load().getView());

                } else {
                    KeyWindowViewModel keyWindowViewModel = new KeyWindowViewModel();
                    keyWindowViewModel.keyProperty().bindBidirectional(Debathon.getInstance().keyProperty());
                    popOver_createQuestion = new PopOver(FluentViewLoader.fxmlView(KeyWindowView.class)
                            .viewModel(keyWindowViewModel)
                            .load().getView());
                }
                popOver_createQuestion.setDetachable(false);
                popOver_createQuestion.setArrowLocation(PopOver.ArrowLocation.TOP_RIGHT);
            };
            this.debate.keyProperty().addListener(this.changeListener_key);

            this.lblTitle_label.bind(this.debate.labelProperty());
            this.description_htmlText.bind(this.debate.descriptionProperty());

            if (this.debate.getCategory() != null) {
                CategoryViewModel categoryViewModel = new CategoryViewModel(this.debate.getCategory());
                final ViewTuple<CategoryView, CategoryViewModel> categoryViewTuple = FluentViewLoader.fxmlView(CategoryView.class)
                        .viewModel(categoryViewModel)
                        .load();
                this.category_value.set(categoryViewTuple);
            }
            this.changeListener_category = (observableValue, oldValue, newValue) -> {
                if (newValue != null) {
                    CategoryViewModel categoryViewModel = new CategoryViewModel(newValue);
                    final ViewTuple<CategoryView, CategoryViewModel> categoryViewTuple = FluentViewLoader.fxmlView(CategoryView.class)
                            .viewModel(categoryViewModel)
                            .load();
                    this.category_value.set(categoryViewTuple);
                } else {
                    this.category_value.set(null);
                }
            };
            this.debate.categoryProperty().addListener(this.changeListener_category);

            this.debate.getListTag().forEach(tag ->
                    Platform.runLater(() -> {
                        if (tag != null) {
                            TagViewModel tagViewModel = new TagViewModel(tag);
                            final ViewTuple<TagView, TagViewModel> tagViewTuple = FluentViewLoader.fxmlView(TagView.class)
                                    .viewModel(tagViewModel)
                                    .load();

                            listTag_value.add(tagViewTuple);
                        }
                    }));
            this.listChangeListener_tag = change -> {
                while (change.next()) {
                    if (change.wasAdded()) {
                        change.getAddedSubList().forEach(item ->
                                Platform.runLater(() -> {
                                    if (item != null) {
                                        TagViewModel tagViewModel = new TagViewModel(item);
                                        final ViewTuple<TagView, TagViewModel> tagViewTuple = FluentViewLoader.fxmlView(TagView.class)
                                                .viewModel(tagViewModel)
                                                .load();

                                        listTag_value.add(tagViewTuple);
                                    }
                                })
                        );
                    } else if (change.wasRemoved()) {
                        change.getRemoved().forEach(item -> Platform.runLater(() -> listTag_value.removeIf(tag -> tag.getViewModel().getTag().equals(item))));
                    }
                }
            };
            this.debate.listTagProperty().addListener(this.listChangeListener_tag);

            try {
                Debathon.getInstance().getAppCommunication().requestRoom(this.debate.getId());

                this.debate.getListQuestion().forEach(question ->
                        Platform.runLater(() -> {
                            if (question != null) {
                                QuestionViewModel questionViewModel = new QuestionViewModel(this.debate, question);
                                final ViewTuple<QuestionView, QuestionViewModel> questionViewTuple = FluentViewLoader.fxmlView(QuestionView.class)
                                        .viewModel(questionViewModel)
                                        .load();

                                listQuestion_value.add(questionViewTuple);
                            }
                        }));
                this.listChangeListener_question = change -> {
                    while (change.next()) {
                        if (change.wasAdded()) {
                            change.getAddedSubList().forEach(item ->
                                    Platform.runLater(() -> {
                                        if (item != null) {
                                            QuestionViewModel questionViewModel = new QuestionViewModel(this.debate, item);
                                            final ViewTuple<QuestionView, QuestionViewModel> questionViewTuple = FluentViewLoader.fxmlView(QuestionView.class)
                                                    .viewModel(questionViewModel)
                                                    .load();

                                            listQuestion_value.add(questionViewTuple);
                                        }
                                    })
                            );
                        } else if (change.wasRemoved()) {
                            change.getRemoved().forEach(item -> listQuestion_value.removeIf(question -> question.getViewModel().getQuestion().equals(item)));
                        }
                    }
                };
                this.debate.listQuestionsProperty().addListener(this.listChangeListener_question);
            } catch (Exception e) {
                if (LOGGER.isWarnEnabled()) {
                    LOGGER.warn("Unable setup the update of the list of question");
                }
            }
        }
    }

    private void unbindDebate() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][method] Usage of the DebateViewModel.unbindDebate()");
        }

        if (this.debate != null) {
            this.key_value.unbind();
            this.lblTitle_label.unbind();
            this.description_htmlText.unbind();

            if (this.changeListener_key != null) {
                this.debate.keyProperty().removeListener(this.changeListener_key);
                this.changeListener_key = null;
            }

            if (this.changeListener_category != null) {
                this.debate.categoryProperty().removeListener(this.changeListener_category);
                this.changeListener_category = null;
            }

            if (this.listChangeListener_tag != null) {
                this.debate.listTagProperty().removeListener(this.listChangeListener_tag);
                this.listChangeListener_tag = null;
            }

            if (this.listChangeListener_question != null) {
                this.debate.listQuestionsProperty().removeListener(this.listChangeListener_question);
                this.listChangeListener_question = null;
            }
        }
    }

    /**
     * Call the communication to delete this debate
     *
     * @author Gaetan Brenckle
     */
    public void actvm_DeleteDebate() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][method] Usage of the HomePageViewModel.actvm_DeleteDebate()");
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(Launch.PRIMARYSTAGE);
        alert.initModality(Modality.APPLICATION_MODAL);

        alert.setHeaderText(this.resBundle_.get().getString("debate_delete_text"));

        alert.getDialogPane().getButtonTypes().setAll(ButtonType.NO, ButtonType.YES);
        Optional<ButtonType> optional = alert.showAndWait();

        if (optional.isPresent() && optional.get() == ButtonType.YES) {
            Debathon.getInstance().getAppCommunication().requestEndDebate(this.debate.getId());
        }
    }

    /**
     * Show a popover to create new questions.
     *
     * @author Gaetan Brenckle
     * @param node - {@link Node} - node used to show the popover
     */
    public void actvm_showCreateQuestion(Node node) {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][method] Usage of the HomePageViewModel.actvm_showCreateQuestion()");
        }

        if (popOver_createQuestion.isShowing()) {
            popOver_createQuestion.hide();
        } else {
            popOver_createQuestion.show(node);
        }
    }


    /**
     * Show a popover to select categories and tags.
     *
     * @author Gaetan Brenckle
     * @param node - {@link Node} - node used to show the popover
     */
    public void actvm_showStatMail(Node node) {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][method] Usage of the HomePageViewModel.actvm_createAddItem()");
        }

        if (popOver_statMail.isShowing()) {
            popOver_statMail.hide();
        } else {
            popOver_statMail.show(node);
        }
    }


    /**
     * Property of the variable borderPane.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link ObjectProperty} - return the property of the variable borderPane.
     */
    public ObjectProperty<BorderPane> borderPaneProperty() {
        return borderPane;
    }

    /**
     * Property of the variable key_value.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link StringProperty} - return the property of the variable key_value.
     */
    public StringProperty key_valueProperty() {
        return key_value;
    }

    /**
     * Property of the variable lblTitle_value.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link StringProperty} - return the property of the variable lblTitle_value.
     */
    public StringProperty lblTitle_labelProperty() {
        return lblTitle_label;
    }

    /**
     * Property of the variable description_htmlText.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link StringProperty} - return the property of the variable description_htmlText.
     */
    public StringProperty description_htmlTextProperty() {
        return description_htmlText;
    }


    /**
     * Property of the variable category_value.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link ObjectProperty} - return the property of the variable category_value.
     */
    public ObjectProperty<ViewTuple<CategoryView, CategoryViewModel>> category_valueProperty() {
        return category_value;
    }

    /**
     * Property of the variable listTag_value.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link ListProperty} - return the property of the variable listTag_value.
     */
    public ListProperty<ViewTuple<TagView, TagViewModel>> listTag_valueProperty() {
        return listTag_value;
    }

    /**
     * Property of the variable listQuestion_value.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link ListProperty} - return the property of the variable listQuestion_value.
     */
    public ListProperty<ViewTuple<QuestionView, QuestionViewModel>> listQuestion_valueProperty() {
        return listQuestion_value;
    }


    /**
     * Getter for the variable debate.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link Room} - return the variable debate.
     */
    public Room getDebate() {
        return this.debate;
    }


    @Override
    public void onViewAdded_Cycle() {

        this.mainViewScope.prevCommandProperty().set(this.mainViewScope.currentCommandProperty().get());
        this.mainViewScope.currentCommandProperty().set(new CompositeCommand());
        this.mainViewScope.currentCommandProperty().get().register(this.prevCommand);
    }

    @Override
    public void onViewRemoved_Cycle() {
        this.unbindDebate();
    }
}
