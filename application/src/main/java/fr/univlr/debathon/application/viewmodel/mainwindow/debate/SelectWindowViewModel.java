package fr.univlr.debathon.application.viewmodel.mainwindow.debate;

import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.InjectScope;
import de.saxsys.mvvmfx.ViewTuple;
import fr.univlr.debathon.application.communication.Debathon;
import fr.univlr.debathon.application.view.mainwindow.debate.DebateThumbnailView;
import fr.univlr.debathon.application.view.mainwindow.debate.items.CategorySelectView;
import fr.univlr.debathon.application.view.mainwindow.debate.items.TagSelectView;
import fr.univlr.debathon.application.viewmodel.ViewModel_SceneCycle;
import fr.univlr.debathon.application.viewmodel.mainwindow.debate.items.CategorySelectViewModel;
import fr.univlr.debathon.application.viewmodel.mainwindow.debate.items.SelectCategoryScope;
import fr.univlr.debathon.application.viewmodel.mainwindow.debate.items.SelectTagScope;
import fr.univlr.debathon.application.viewmodel.mainwindow.debate.items.TagSelectViewModel;
import fr.univlr.debathon.job.db_project.jobclass.Category;
import fr.univlr.debathon.job.db_project.jobclass.Tag;
import fr.univlr.debathon.language.LanguageBundle;
import fr.univlr.debathon.log.generate.CustomLogger;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.Optional;
import java.util.ResourceBundle;

public class SelectWindowViewModel extends ViewModel_SceneCycle {

    private static final CustomLogger LOGGER = CustomLogger.create(SelectWindowViewModel.class.getName());
    private final ObjectProperty<ResourceBundle> resBundle_ = LanguageBundle.getInstance().bindResourceBundle("properties.language.mainwindow.lg_homePage");

    //Text
    private final StringProperty lblCategory_label = new SimpleStringProperty(this.resBundle_.get().getString("lblCategory"));
    private final StringProperty lblTags_label = new SimpleStringProperty(this.resBundle_.get().getString("lblTags"));

    //Value
    private final ListProperty<ViewTuple<CategorySelectView, CategorySelectViewModel>> listCategories_value = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final ListProperty<ViewTuple<TagSelectView, TagSelectViewModel>> listTags_value = new SimpleListProperty<>(FXCollections.observableArrayList());

    private ChangeListener<ResourceBundle> listener_ChangedValue_bundleLanguage_;

    private ListChangeListener<Tag> listChangeListener_Tag;
    private ListChangeListener<Category> listChangeListener_Category;

    @InjectScope
    private SelectCategoryScope selectCategoryScope;
    @InjectScope
    private SelectTagScope selectTagScope;


    /**
     * Default constructor
     *
     * @author Gaetan Brenckle
     */
    public SelectWindowViewModel() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][constructor] Creation of the TagSelectWindowViewModel() object.");
        }

        //ResourceBundle Listener
        this.listener_ChangedValue_bundleLanguage_ = this::listener_bundleLanguage;
        this.resBundle_.addListener(this.listener_ChangedValue_bundleLanguage_);
    }

    public void initialize() {
        bindSelectedItems();
    }

    private void bindSelectedItems() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[private][method] Usage of the TagSelectWindowViewModel.bindSelectedItems()");
        }

        Debathon.getInstance().getCategories().forEach(category ->
                Platform.runLater(() -> {
                    if (category != null) {
                        CategorySelectViewModel categorySelectViewModel = new CategorySelectViewModel(category);
                        final ViewTuple<CategorySelectView, CategorySelectViewModel> categoryViewTuple = FluentViewLoader.fxmlView(CategorySelectView.class)
                                .providedScopes(selectCategoryScope)
                                .viewModel(categorySelectViewModel)
                                .load();

                        listCategories_value.add(categoryViewTuple);
                    }
                }));

        this.listChangeListener_Category = change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().forEach(item ->
                            Platform.runLater(() -> {
                                if (item != null) {
                                    CategorySelectViewModel categorySelectViewModel = new CategorySelectViewModel(item);
                                    final ViewTuple<CategorySelectView, CategorySelectViewModel> categoryViewTuple = FluentViewLoader.fxmlView(CategorySelectView.class)
                                            .providedScopes(selectCategoryScope)
                                            .viewModel(categorySelectViewModel)
                                            .load();

                                    listCategories_value.add(categoryViewTuple);
                                }
                            })
                    );
                } else if (change.wasRemoved()) {
                    change.getRemoved().forEach(item ->
                            Platform.runLater(() -> {
                                Optional<ViewTuple<CategorySelectView, CategorySelectViewModel>> optional = listCategories_value.stream().filter(category -> category.getViewModel().getCategory().equals(item)).findAny();
                                optional.ifPresent(listCategories_value::remove);
                            })
                    );
                }
            }
        };
        Debathon.getInstance().getCategories().addListener(this.listChangeListener_Category);

        //--------------

        Debathon.getInstance().getTags().forEach(tag ->
                Platform.runLater(() -> {
                    if (tag != null) {
                        TagSelectViewModel tagSelectViewModel = new TagSelectViewModel(tag);
                        final ViewTuple<TagSelectView, TagSelectViewModel> tagViewTuple = FluentViewLoader.fxmlView(TagSelectView.class)
                                .providedScopes(selectTagScope)
                                .viewModel(tagSelectViewModel)
                                .load();

                        listTags_value.add(tagViewTuple);
                    }
                }));

        this.listChangeListener_Tag = change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().forEach(item ->
                            Platform.runLater(() -> {
                                if (item != null) {
                                    TagSelectViewModel tagSelectViewModel = new TagSelectViewModel(item);
                                    final ViewTuple<TagSelectView, TagSelectViewModel> tagViewTuple = FluentViewLoader.fxmlView(TagSelectView.class)
                                            .providedScopes(selectTagScope)
                                            .viewModel(tagSelectViewModel)
                                            .load();

                                    listTags_value.add(tagViewTuple);
                                }
                            })
                    );
                } else if (change.wasRemoved()) {
                    change.getRemoved().forEach(item ->
                            Platform.runLater(() -> {
                                Optional<ViewTuple<TagSelectView, TagSelectViewModel>> optional = listTags_value.stream().filter(tag -> tag.getViewModel().getTag().equals(item)).findAny();
                                optional.ifPresent(listTags_value::remove);
                            })
                    );
                }
            }
        };
        Debathon.getInstance().getTags().addListener(this.listChangeListener_Tag);
    }


    private void unbindSelectedItems() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[private][method] Usage of the HomePageViewModel.unbindSelectedItems()");
        }

        if (this.listChangeListener_Category != null) {
            Debathon.getInstance().getCategories().removeListener(this.listChangeListener_Category);
            this.listChangeListener_Category = null;
        }

        if (this.listChangeListener_Tag != null) {
            Debathon.getInstance().getTags().removeListener(this.listChangeListener_Tag);
            this.listChangeListener_Tag = null;
        }
    }


    /**
     * Property of the variable lblCategory_label.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link StringProperty} - return the property of the variable lblCategory_label.
     */
    public StringProperty lblCategory_labelProperty() {
        return lblCategory_label;
    }

    /**
     * Property of the variable lblTags_label.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link StringProperty} - return the property of the variable lblTags_label.
     */
    public StringProperty lblTags_labelProperty() {
        return lblTags_label;
    }

    /**
     * Property of the variable listCategories_value.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link ListProperty} - return the property of the variable listCategories_value.
     */
    public ListProperty<ViewTuple<CategorySelectView, CategorySelectViewModel>> listCategories_valueProperty() {
        return listCategories_value;
    }

    /**
     * Property of the variable listTags_value.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link ListProperty} - return the property of the variable listTags_value.
     */
    public ListProperty<ViewTuple<TagSelectView, TagSelectViewModel>> listTags_valueProperty() {
        return listTags_value;
    }


    /**
     * Listener for the resource bundle.
     *
     * @param observable - {@link ObservableValue} - the value observed
     * @param oldValue - {@link ResourceBundle} - the old value that are replaced
     * @param newValue - {@link ResourceBundle} - the new value
     */
    private void listener_bundleLanguage(ObservableValue<? extends ResourceBundle> observable, ResourceBundle oldValue, ResourceBundle newValue) {
        this.lblCategory_label.set(this.resBundle_.get().getString("lblCategory"));
        this.lblTags_label.set(this.resBundle_.get().getString("lblTags"));
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

        unbindSelectedItems();
    }
}
