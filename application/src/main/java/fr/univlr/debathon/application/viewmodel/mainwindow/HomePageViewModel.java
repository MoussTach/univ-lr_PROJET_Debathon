package fr.univlr.debathon.application.viewmodel.mainwindow;

import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.InjectScope;
import de.saxsys.mvvmfx.ScopeProvider;
import de.saxsys.mvvmfx.ViewTuple;
import de.saxsys.mvvmfx.utils.commands.Action;
import de.saxsys.mvvmfx.utils.commands.Command;
import de.saxsys.mvvmfx.utils.commands.CompositeCommand;
import de.saxsys.mvvmfx.utils.commands.DelegateCommand;
import de.saxsys.mvvmfx.utils.validation.ObservableRuleBasedValidator;
import de.saxsys.mvvmfx.utils.validation.ValidationMessage;
import fr.univlr.debathon.application.Launch;
import fr.univlr.debathon.application.communication.Debathon;
import fr.univlr.debathon.application.view.mainwindow.SelectWindowView;
import fr.univlr.debathon.application.view.mainwindow.debate.DebateThumbnailView;
import fr.univlr.debathon.application.view.mainwindow.debate.items.CategoryView;
import fr.univlr.debathon.application.view.mainwindow.debate.items.TagView;
import fr.univlr.debathon.application.viewmodel.ViewModel_SceneCycle;
import fr.univlr.debathon.application.viewmodel.mainwindow.debate.DebateThumbnailViewModel;
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
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import org.controlsfx.control.PopOver;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

@ScopeProvider(scopes= {SelectCategoryScope.class, SelectTagScope.class})
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

    private final ObjectProperty<ViewTuple<CategoryView, CategoryViewModel> > category_selected_value = new SimpleObjectProperty<>();
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


    private ChangeListener<Category> changeListener_Category;
    private ListChangeListener<Tag> listChangeListener_Tag;

    private ListChangeListener<Room> listChangeListener_Debate;
    private ChangeListener<ResourceBundle> listener_ChangedValue_bundleLanguage_;

    @InjectScope
    private MainViewScope mainViewScope;

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
    public HomePageViewModel() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][constructor] Creation of the HomePageViewModel() object.");
        }

        rule_changeFilter.addRule(createRule_changeFilter());

        //ResourceBundle Listener
        this.listener_ChangedValue_bundleLanguage_ = this::listener_bundleLanguage;
        this.resBundle_.addListener(this.listener_ChangedValue_bundleLanguage_);

        bindDebate();
        Platform.runLater(this::bindSelectedItems);
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

    private void bindSelectedItems() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[private][method] Usage of the HomePageViewModel.bindSelectedItems()");
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

    private void unbindSelectedItems() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[private][method] Usage of the HomePageViewModel.unbindSelectedItems()");
        }

        if (this.changeListener_Category != null) {
            this.selectCategoryScope.selectedCategoryProperty().removeListener(this.changeListener_Category);
            this.changeListener_Category = null;
        }

        if (this.listChangeListener_Tag != null) {
            this.selectTagScope.selectedTagsProperty().removeListener(this.listChangeListener_Tag);
            this.listChangeListener_Tag = null;
        }
    }

    private void unbindDebate() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[private][method] Usage of the HomePageViewModel.unbindDebate()");
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

                            //Categories
                            if (this.category_selected_value.get() != null) {
                                filterKeep = this.category_selected_value.get().getViewModel().getCategory().equals(optionalDebate.get().getViewModel().getDebate().getCategory());
                            }

                            //Tags
                            if (!listTag_selected_value.isEmpty() && filterKeep) {
                                List<Tag> currentTags = new ArrayList<>();
                                this.listTag_selected_value.forEach(tag -> currentTags.add(tag.getViewModel().getTag()));
                                filterKeep = optionalDebate.get().getViewModel().getDebate().getListTag().containsAll(currentTags);
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
        }, this.tfSearch_value, this.category_selected_value, this.listTag_selected_value, this.listDebate_value);
    }


    /**
     * Show a popover to select categories and tags.
     *
     * @author Gaetan Brenckle
     * @param node - {@link Node} - node used to show the popover
     */
    public void actvm_createAddItem(Node node) {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][method] Usage of the HomePageViewModel.actvm_createAddItem()");
        }

        if (popOver_selectItems.isShowing()) {
            popOver_selectItems.hide();
        } else {
            popOver_selectItems.show(node);
        }
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
        unbindSelectedItems();

        LanguageBundle.getInstance().unbindResourceBundle(this.resBundle_);
    }
}
