package fr.univlr.debathon.application.viewmodel.mainwindow.debate;

import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.InjectScope;
import de.saxsys.mvvmfx.ViewTuple;
import fr.univlr.debathon.application.view.mainwindow.debate.DebateView;
import fr.univlr.debathon.application.view.mainwindow.debate.items.CategoryView;
import fr.univlr.debathon.application.view.mainwindow.debate.items.TagView;
import fr.univlr.debathon.application.viewmodel.ViewModel_SceneCycle;
import fr.univlr.debathon.application.viewmodel.mainwindow.MainViewScope;
import fr.univlr.debathon.application.viewmodel.mainwindow.debate.items.CategoryViewModel;
import fr.univlr.debathon.application.viewmodel.mainwindow.debate.items.TagViewModel;
import fr.univlr.debathon.job.db_project.jobclass.Category;
import fr.univlr.debathon.job.db_project.jobclass.Room;
import fr.univlr.debathon.job.db_project.jobclass.Tag;
import fr.univlr.debathon.log.generate.CustomLogger;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.scene.layout.BorderPane;

public class DebateThumbnailViewModel extends ViewModel_SceneCycle {

    private static final CustomLogger LOGGER = CustomLogger.create(DebateThumbnailViewModel.class.getName());

    private final Room debate;

    //Value
    private final StringProperty lblTitle_label = new SimpleStringProperty("/");
    private final ObjectProperty<ViewTuple<CategoryView, CategoryViewModel>> category_value = new SimpleObjectProperty<>();
    private final ListProperty<ViewTuple<TagView, TagViewModel>> listTag_selected_value = new SimpleListProperty<>(FXCollections.synchronizedObservableList(FXCollections.observableArrayList()));

    private final StringProperty lblNbPeople_value = new SimpleStringProperty("/");

    private ChangeListener<Category> changeListener_category = null;
    private ListChangeListener<Tag> listChangeListener_tag = null;

    @InjectScope
    private MainViewScope mainViewScope;


    /**
     * Default constructor
     *
     * @param debate {@link Room} - the debate associated to this view.
     *
     * @author Gaetan Brenckle
     */
    public DebateThumbnailViewModel(Room debate) {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][constructor] Creation of the DebateThumbnailViewModel() object.");
        }

        this.debate = debate;

        bindDebate();
    }


    private void bindDebate() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][method] Usage of the DebateThumbnailViewModel.bindDebate()");
        }

        if (this.debate != null) {
            this.lblTitle_label.bind(this.debate.labelProperty());

            if (this.debate.getCategory() != null) {
                CategoryViewModel categoryViewModel = new CategoryViewModel(this.debate.getCategory());
                final ViewTuple<CategoryView, CategoryViewModel> categoryViewTuple = FluentViewLoader.fxmlView(CategoryView.class)
                        .viewModel(categoryViewModel)
                        .load();

                this.category_value.set(categoryViewTuple);
            }

            if (this.changeListener_category == null) {
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
            }

            //-------------------------------

            this.debate.getListTag().forEach(tag ->
                    Platform.runLater(() -> {
                        if (tag != null) {
                            TagViewModel tagViewModel = new TagViewModel(tag);
                            final ViewTuple<TagView, TagViewModel> tagViewTuple = FluentViewLoader.fxmlView(TagView.class)
                                    .viewModel(tagViewModel)
                                    .load();

                            listTag_selected_value.add(tagViewTuple);
                        }
                    }));

            if (this.listChangeListener_tag == null) {
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

                                            listTag_selected_value.add(tagViewTuple);
                                        }
                                    })
                            );
                        } else if (change.wasRemoved()) {
                            change.getRemoved().forEach(item -> Platform.runLater(() -> listTag_selected_value.removeIf(tag -> tag.getViewModel().getTag().equals(item))));
                        }
                    }
                };

                this.debate.listTagProperty().addListener(this.listChangeListener_tag);
            }
        }
    }

    private void unbindDebate() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][method] Usage of the DebateThumbnailViewModel.unbindDebate()");
        }

        if (this.debate != null) {
            this.lblTitle_label.unbind();

            if (this.changeListener_category != null) {
                this.debate.categoryProperty().removeListener(this.changeListener_category);
                this.changeListener_category = null;
            }

            if (this.listChangeListener_tag != null) {
                this.debate.listTagProperty().removeListener(this.listChangeListener_tag);
                this.listChangeListener_tag = null;
            }
        }
    }


    public void actvm_btnOpenDebate() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][method] Usage of the DebateThumbnailViewModel.actvm_btnOpenDebate()");
        }

        DebateViewModel debateViewModel = new DebateViewModel(this.debate);
        final ViewTuple<DebateView, DebateViewModel> debateViewTuple = FluentViewLoader.fxmlView(DebateView.class)
                .viewModel(debateViewModel)
                .load();

        BorderPane mainBorder = this.mainViewScope.basePaneProperty().get();
        mainBorder.setCenter(debateViewTuple.getView());
    }


    //Value
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
     * Property of the variable lblNbPeople_value.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link StringProperty} - return the property of the variable lblNbPeople_value.
     */
    public StringProperty lblNbPeople_valueProperty() {
        return lblNbPeople_value;
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
    }

    @Override
    public void onViewRemoved_Cycle() {
    }
}
