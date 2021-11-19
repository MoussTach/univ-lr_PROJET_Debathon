package fr.univlr.debathon.application.viewmodel.mainwindow;

import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.InjectScope;
import de.saxsys.mvvmfx.ViewTuple;
import de.saxsys.mvvmfx.utils.commands.Action;
import de.saxsys.mvvmfx.utils.commands.Command;
import de.saxsys.mvvmfx.utils.commands.CompositeCommand;
import de.saxsys.mvvmfx.utils.commands.DelegateCommand;
import de.saxsys.mvvmfx.utils.validation.ObservableRuleBasedValidator;
import de.saxsys.mvvmfx.utils.validation.ValidationMessage;
import fr.univlr.debathon.application.communication.Debathon;
import fr.univlr.debathon.application.view.mainwindow.debate.DebateThumbnailView;
import fr.univlr.debathon.application.view.mainwindow.debate.items.TagView;
import fr.univlr.debathon.application.viewmodel.ViewModel_SceneCycle;
import fr.univlr.debathon.application.viewmodel.mainwindow.debate.DebateThumbnailViewModel;
import fr.univlr.debathon.application.viewmodel.mainwindow.debate.items.TagViewModel;
import fr.univlr.debathon.job.db_project.jobclass.Room;
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
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

import java.util.Optional;
import java.util.ResourceBundle;

public class HomePageViewModel extends ViewModel_SceneCycle {

    private final ObjectProperty<ResourceBundle> resBundle_ = LanguageBundle.getInstance().bindResourceBundle("properties.language.mainwindow.lg_homePage");
    private static final CustomLogger LOGGER = CustomLogger.create(HomePageViewModel.class.getName());

    private final ObjectProperty<BorderPane> borderPane = new SimpleObjectProperty<>(null);

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
    private final Command homeCommand = new DelegateCommand(() -> new Action() {
        @Override
        protected void action() {
            //TODO
            System.out.println("act home Home");
            Platform.runLater(() -> {
                BorderPane mainBorderPane = mainViewScope.basePaneProperty().get();
                mainBorderPane.setCenter(borderPane.get());
            });
        }
    }, true);

    private ListChangeListener<Room> listChangeListener_Debate;
    private ChangeListener<ResourceBundle> listener_ChangedValue_bundleLanguage_;

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

        //ResourceBundle Listener
        this.listener_ChangedValue_bundleLanguage_ = this::listener_bundleLanguage;
        this.resBundle_.addListener(this.listener_ChangedValue_bundleLanguage_);

        bindDebate();
    }

    private void bindDebate() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][method] Usage of the HomePageViewModel.bindDebate()");
        }

        Debathon.getInstance().getDebates().forEach(room ->
                Platform.runLater(() -> {
                    if (room != null) {
                        DebateThumbnailViewModel debateThumbnailViewModel = new DebateThumbnailViewModel(room);
                        final ViewTuple<DebateThumbnailView, DebateThumbnailViewModel> debateThumbViewTuple = FluentViewLoader.fxmlView(DebateThumbnailView.class)
                                .providedScopes(mainViewScope)
                                .viewModel(debateThumbnailViewModel)
                                .load();

                        listDebate_value.add(debateThumbViewTuple);
                        listDebate_node_value.add(debateThumbViewTuple.getView());
                    }
                }));

        this.listChangeListener_Debate = change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().forEach(item ->
                            Platform.runLater(() -> {
                                if (item != null) {
                                    DebateThumbnailViewModel debateThumbnailViewModel = new DebateThumbnailViewModel(item);
                                    final ViewTuple<DebateThumbnailView, DebateThumbnailViewModel> debateThumbViewTuple = FluentViewLoader.fxmlView(DebateThumbnailView.class)
                                            .providedScopes(mainViewScope)
                                            .viewModel(debateThumbnailViewModel)
                                            .load();

                                    listDebate_value.add(debateThumbViewTuple);
                                    listDebate_node_value.add(debateThumbViewTuple.getView());
                                }
                            })
                    );
                } else if (change.wasRemoved()) {
                    change.getRemoved().forEach(item ->
                            Platform.runLater(() -> {
                                Optional<ViewTuple<DebateThumbnailView, DebateThumbnailViewModel>> optional = listDebate_value.stream().filter(debate -> debate.getViewModel().getDebate().equals(item)).findAny();
                                if (optional.isPresent()) {
                                    listDebate_node_value.remove(optional.get().getView());
                                    listDebate_value.remove(optional.get());
                                }
                            })
                    );
                }
            }
        };
        Debathon.getInstance().debatesProperty().addListener(this.listChangeListener_Debate);
    }


    private void unbindDebate() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][method] Usage of the HomePageViewModel.unbindDebate()");
        }

        if (this.listChangeListener_Debate != null) {
            Debathon.getInstance().debatesProperty().removeListener(this.listChangeListener_Debate);
            this.listChangeListener_Debate = null;
        }
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
                                filterKeep = false;

                                for (ViewTuple<TagView, TagViewModel> itemView : optionalDebate.get().getViewModel().listTag_selected_valueProperty()) {
                                    for (ViewTuple<TagView, TagViewModel> itemActual : this.listTag_selected_value) {
                                        filterKeep = itemView.getViewModel().lblTag_labelProperty().get().equals(itemActual.getViewModel().lblTag_labelProperty().get());
                                        if (filterKeep)
                                            break;
                                    }

                                    if (!filterKeep)
                                        break;
                                }
                            }

                            //Select search
                            if (this.tfSearch_value.get() != null && !this.tfSearch_value.get().isEmpty() && filterKeep) {
                                String lowerCaseFilter = this.tfSearch_value.get().toLowerCase();

                                filterKeep = optionalDebate.get().getViewModel().lblTitle_labelProperty().toString().toLowerCase().contains(lowerCaseFilter);
                            }
                        }
                        return filterKeep;
                    });
            return null;
        }, this.tfSearch_value, this.listTag_selected_value, this.listDebate_value);
    }


    /**
     * Getter for the list filtered of debate available
     *
     * @return - {@link SortedList} - Filtered list of debate available.
     */
    public FilteredList<Node> getFilteredData() {
        return filteredData;
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
     * Listener for the resource bundle.
     *
     * @param observable - {@link ObservableValue} - the value observed
     * @param oldValue - {@link ResourceBundle} - the old value that are replaced
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
        //TODO
        System.out.println("OnView home");
        this.mainViewScope.prevCommandProperty().set(this.mainViewScope.currentCommandProperty().get());
        this.mainViewScope.currentCommandProperty().set(new CompositeCommand());
        this.mainViewScope.currentCommandProperty().get().register(this.prevCommand);
        this.mainViewScope.homeCommandProperty().get().register(this.homeCommand);
    }

    @Override
    public void onViewRemoved_Cycle() {
        if (this.listener_ChangedValue_bundleLanguage_ != null) {
            this.resBundle_.removeListener(this.listener_ChangedValue_bundleLanguage_);
            this.listener_ChangedValue_bundleLanguage_ = null;
        }

        unbindDebate();

        LanguageBundle.getInstance().unbindResourceBundle(this.resBundle_);
    }
}
