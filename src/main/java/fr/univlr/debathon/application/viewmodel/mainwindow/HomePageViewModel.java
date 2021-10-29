package fr.univlr.debathon.application.viewmodel.mainwindow;

import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.InjectScope;
import de.saxsys.mvvmfx.ViewTuple;
import de.saxsys.mvvmfx.utils.commands.Action;
import de.saxsys.mvvmfx.utils.commands.DelegateCommand;
import de.saxsys.mvvmfx.utils.validation.ObservableRuleBasedValidator;
import de.saxsys.mvvmfx.utils.validation.ValidationMessage;
import fr.univlr.debathon.application.taskmanager.TaskArray;
import fr.univlr.debathon.application.taskmanager.Task_Custom;
import fr.univlr.debathon.application.taskmanager.ThreadArray;
import fr.univlr.debathon.application.view.mainwindow.debate.DebateThumbnailView;
import fr.univlr.debathon.application.view.mainwindow.debate.items.TagView;
import fr.univlr.debathon.application.viewmodel.ViewModel_SceneCycle;
import fr.univlr.debathon.application.viewmodel.mainwindow.debate.DebateThumbnailViewModel;
import fr.univlr.debathon.application.viewmodel.mainwindow.debate.items.TagViewModel;
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
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.util.Pair;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;

import static fr.univlr.debathon.application.Main.TASKMANAGER;

public class HomePageViewModel extends ViewModel_SceneCycle {

    private final ObjectProperty<ResourceBundle> resBundle_ = LanguageBundle.getInstance().bindResourceBundle("properties.language.mainwindow.lg_homePage");
    private static final CustomLogger LOGGER = CustomLogger.create(HomePageViewModel.class.getName());

    //Text
    private final StringProperty tPaneParameters_label = new SimpleStringProperty(this.resBundle_.get().getString("tPaneParameters"));

    private final StringProperty lblOrganizer_label = new SimpleStringProperty(this.resBundle_.get().getString("lblOrganizer"));
    private final StringProperty chkShowCreatedDebate_label = new SimpleStringProperty(this.resBundle_.get().getString("chkShowCreatedDebate"));
    private final StringProperty btnCreateNewDebate_label = new SimpleStringProperty(this.resBundle_.get().getString("btnCreateNewDebate"));

    private final StringProperty btnAddTag_label = new SimpleStringProperty(this.resBundle_.get().getString("btnAddTag"));


    //Value
    private final BooleanProperty chkShowCreatedDebate_value = new SimpleBooleanProperty(false);

    private final ListProperty<ViewTuple<TagView, TagViewModel> > listTag_selected_value = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final StringProperty tfSearch_value = new SimpleStringProperty();

    private final ListProperty<ViewTuple<DebateThumbnailView, DebateThumbnailViewModel> > listDebate_value = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final ListProperty<Node> listDebate_node_value = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final FilteredList<Node> filteredData  = new FilteredList<>(listDebate_node_value, p -> true);

    private final ObservableRuleBasedValidator rule_changeFilter = new ObservableRuleBasedValidator();

    private ListChangeListener<ViewTuple<DebateThumbnailView, DebateThumbnailViewModel> > listChangeListener_Debate;
    private ChangeListener<ResourceBundle> listener_ChangedValue_bundleLanguage_ = null;

    @InjectScope
    private MainViewScope mainViewScope;


    /**
     * Default constructor
     *
     * @author Gaetan Brenckle
     */
    public HomePageViewModel() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][constructor] Creation of the HomePageViewModel() object.");
        }

        rule_changeFilter.addRule(createRule_changeFilter());

        this.listChangeListener_Debate = change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().forEach(item -> Platform.runLater(() -> listDebate_node_value.add(item.getView())));
                } else if (change.wasRemoved()) {
                    change.getRemoved().forEach(item -> Platform.runLater(() -> listDebate_node_value.remove(item.getView())));
                }
            }
        };
        this.listDebate_value.addListener(this.listChangeListener_Debate);

        //RessourceBundle Listener
        this.listener_ChangedValue_bundleLanguage_ = this::listener_bundleLanguage;
        this.resBundle_.addListener(this.listener_ChangedValue_bundleLanguage_);

        loadDebate();
        //TODO loadTag();
    }

    //TODO define a call to the data base
    private void loadDebate() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[private][method] usage of HomePageViewModel.loadDebate().");
        }

        Task_Custom<Void> task_loadDebate = new Task_Custom<>(new Image(getClass().getResourceAsStream("/img/add_64.png")), "Load dabates") {

            @Override
            protected Void call_Task() {
                for (int i = 0; i < 10; i++) {
                    DebateThumbnailViewModel debateThumbnailViewModel = new DebateThumbnailViewModel();

                    //Random name
                    byte[] array = new byte[7]; // length is bounded by 7
                    new Random().nextBytes(array);
                    String generatedString = new String(array, StandardCharsets.UTF_8);

                    debateThumbnailViewModel.lblTitle_valueProperty().set(generatedString);
                    debateThumbnailViewModel.lblNbPeople_valueProperty().set("3");

                    if (i % 2 == 0) {
                        TagViewModel tagViewModel = new TagViewModel();
                        tagViewModel.lblTag_labelProperty().set("test");


                        final ViewTuple<TagView, TagViewModel> tagViewTuple = FluentViewLoader.fxmlView(TagView.class)
                                .providedScopes(mainViewScope)
                                .viewModel(tagViewModel)
                                .load();

                        debateThumbnailViewModel.listTag_selected_valueProperty().add(tagViewTuple);
                    }

                    final ViewTuple<DebateThumbnailView, DebateThumbnailViewModel> debateViewTuple = FluentViewLoader.fxmlView(DebateThumbnailView.class)
                            .viewModel(debateThumbnailViewModel)
                            .load();
                    listDebate_value.add(debateViewTuple);
                }
                return null;
            }
        };

        TASKMANAGER.addArray(new TaskArray<>(ThreadArray.ExecutionType.PARALLEL).setLevel(0)
                .addTask(new Pair<Task_Custom<Void>, ThreadArray<?>>(task_loadDebate, new TaskArray<>(ThreadArray.ExecutionType.END).setLevel(1)))
        );
    }

    //TODO define a call to the data base
    public void loadTag() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[private][method] usage of HomePageViewModel.loadTag().");
        }

        Task_Custom<Void> task_loadTag = new Task_Custom<>(new Image(getClass().getResourceAsStream("/img/add_64.png")), "Load tags") {

            @Override
            protected Void call_Task() {
                for (int i = 0; i < 1; i++) {
                    TagViewModel tagViewModel = new TagViewModel();
                    tagViewModel.lblTag_labelProperty().set("test");

                    final ViewTuple<TagView, TagViewModel> tagViewTuple = FluentViewLoader.fxmlView(TagView.class)
                            .viewModel(tagViewModel)
                            .load();
                    Platform.runLater(() -> listTag_selected_value.add(tagViewTuple));
                }
                return null;
            }
        };

        TASKMANAGER.addArray(new TaskArray<>(ThreadArray.ExecutionType.PARALLEL).setLevel(0)
                .addTask(new Pair<Task_Custom<Void>, ThreadArray<?>>(task_loadTag, new TaskArray<>(ThreadArray.ExecutionType.END).setLevel(1)))
        );
    }


    /**
     * Rule to change the current debate showed.
     *
     * @return {@link ObjectBinding} - the rule
     */
    private ObjectBinding<ValidationMessage> createRule_changeFilter() {
        return Bindings.createObjectBinding(() -> {
            this.filteredData.setPredicate(
                    debate -> {
                        //Select sort
                        boolean filterKeep = true;

                        Optional<ViewTuple<DebateThumbnailView, DebateThumbnailViewModel> > optionalDebate = listDebate_value.stream().filter(debateModel -> debateModel.getView().equals(debate)).findFirst();

                        if (optionalDebate.isPresent()) {
                            if (!listTag_selected_value.isEmpty()) {
                                filterKeep = optionalDebate.get().getViewModel().listTag_selected_valueProperty().containsAll(listTag_selected_value);
                            }

                            //Select search
                            if (this.tfSearch_value.get() != null && !this.tfSearch_value.get().isEmpty() && filterKeep) {
                                String lowerCaseFilter = this.tfSearch_value.get().toLowerCase();

                                filterKeep = optionalDebate.get().getViewModel().lblTitle_valueProperty().toString().toLowerCase().contains(lowerCaseFilter);
                            }
                        }
                        return filterKeep;
                    });
            return null;
        }, this.tfSearch_value, this.listTag_selected_value);
    }


    /**
     * Getter for the list filtered of debate available
     *
     * @return - {@link SortedList} - Filtered list of debate available.
     */
    public FilteredList<Node> getFilteredData() {
        return filteredData;
    }


    //Text
    /**
     * Property of the variable tPaneParameters_label.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link StringProperty} - return the property of the variable tPaneParameters_label.
     */
    public StringProperty tPaneParameters_labelProperty() {
        return tPaneParameters_label;
    }

    /**
     * Property of the variable lblOrganizer_label.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link StringProperty} - return the property of the variable lblOrganizer_label.
     */
    public StringProperty lblOrganizer_labelProperty() {
        return lblOrganizer_label;
    }

    /**
     * Property of the variable chkShowCreatedDebate_label.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link StringProperty} - return the property of the variable chkShowCreatedDebate_label.
     */
    public StringProperty chkShowCreatedDebate_labelProperty() {
        return chkShowCreatedDebate_label;
    }

    /**
     * Property of the variable btnCreateNewDebate_label.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link StringProperty} - return the property of the variable btnCreateNewDebate_label.
     */
    public StringProperty btnCreateNewDebate_labelProperty() {
        return btnCreateNewDebate_label;
    }

    /**
     * Property of the variable btnAddTag_label.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link StringProperty} - return the property of the variable btnAddTag_label.
     */
    public StringProperty btnAddTag_labelProperty() {
        return btnAddTag_label;
    }


    //Value
    /**
     * Property of the variable chkShowCreatedDebate_value.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link BooleanProperty} - return the property of the variable chkShowCreatedDebate_value.
     */
    public BooleanProperty chkShowCreatedDebate_valueProperty() {
        return chkShowCreatedDebate_value;
    }

    /**
     * Property of the variable listTag_selected_value.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link ListProperty} - return the property of the variable listTag_selected_value.
     */
    public ListProperty<ViewTuple<TagView, TagViewModel> > listTag_selected_valueProperty() {
        return listTag_selected_value;
    }

    /**
     * Property of the variable tfSearch_value.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link StringProperty} - return the property of the variable tfSearch_value.
     */
    public StringProperty tfSearch_valueProperty() {
        return tfSearch_value;
    }

    /**
     * Property of the variable listDebate_value.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link ListProperty} - return the property of the variable listDebate_value.
     */
    public ListProperty<ViewTuple<DebateThumbnailView, DebateThumbnailViewModel>> listDebate_valueProperty() {
        return listDebate_value;
    }

    /**
     * Property of the variable listDebate_node_value.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link ListProperty} - return the property of the variable listDebate_node_value.
     */
    public ListProperty<Node> listDebate_node_valueProperty() {
        return listDebate_node_value;
    }


    /**
     * Listener for the ressource bundle.
     *
     * @param observable - {@link ObservableValue} - the value observed
     * @param oldValue - {@link ResourceBundle} - the old value that are remplaced
     * @param newValue - {@link ResourceBundle} - the new value
     */
    private void listener_bundleLanguage(ObservableValue<? extends ResourceBundle> observable, ResourceBundle oldValue, ResourceBundle newValue) {
        tPaneParameters_label.set(this.resBundle_.get().getString("tPaneParameters"));

        lblOrganizer_label.set(this.resBundle_.get().getString("lblOrganizer"));
        chkShowCreatedDebate_label.set(this.resBundle_.get().getString("chkShowCreatedDebate"));
        btnCreateNewDebate_label.set(this.resBundle_.get().getString("btnCreateNewDebate"));

        btnAddTag_label.set(this.resBundle_.get().getString("btnAddTag"));
    }


    @Override
    public void onViewAdded_Cycle() {
    }

    @Override
    public void onViewRemoved_Cycle() {
        mainViewScope.prevCommandProperty().set(null);
        mainViewScope.homeCommandProperty().set(null);

        if (this.listener_ChangedValue_bundleLanguage_ != null) {
            this.resBundle_.removeListener(this.listener_ChangedValue_bundleLanguage_);
            this.listener_ChangedValue_bundleLanguage_ = null;
        }

        if (this.listChangeListener_Debate != null) {
            this.listDebate_value.removeListener(this.listChangeListener_Debate);
            this.listChangeListener_Debate = null;
        }

        LanguageBundle.getInstance().unbindRessourceBundle(this.resBundle_);
    }
}
