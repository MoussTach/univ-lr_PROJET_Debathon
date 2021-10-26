package fr.univlr.debathon.application.viewmodel.mainwindow;

import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.ViewTuple;
import fr.univlr.debathon.application.taskmanager.TaskArray;
import fr.univlr.debathon.application.taskmanager.Task_Custom;
import fr.univlr.debathon.application.taskmanager.ThreadArray;
import fr.univlr.debathon.application.view.mainwindow.debate.items.TagView;
import fr.univlr.debathon.application.viewmodel.ViewModel_SceneCycle;
import fr.univlr.debathon.application.viewmodel.mainwindow.debate.items.TagViewModel;
import fr.univlr.debathon.language.LanguageBundle;
import fr.univlr.debathon.log.generate.CustomLogger;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.image.Image;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static fr.univlr.debathon.application.Main.TASKMANAGER;

public class HomePageViewModel extends ViewModel_SceneCycle {

    private final ObjectProperty<ResourceBundle> resBundle_ = LanguageBundle.getInstance().bindResourceBundle("properties.language.mainwindow.lg_homePage");
    private static final CustomLogger LOGGER = CustomLogger.create(HomePageViewModel.class.getName());

    //Text
    private final StringProperty lblOrganizer_label = new SimpleStringProperty(this.resBundle_.get().getString("lblOrganizer"));
    private final StringProperty chkShowCreatedDebate_label = new SimpleStringProperty(this.resBundle_.get().getString("chkShowCreatedDebate"));
    private final StringProperty btnCreateNewDebate_label = new SimpleStringProperty(this.resBundle_.get().getString("btnCreateNewDebate"));

    private final StringProperty btnAddTag_label = new SimpleStringProperty(this.resBundle_.get().getString("btnAddTag"));


    //Value
    private final BooleanProperty chkShowCreatedDebate_value = new SimpleBooleanProperty(false);

    private final ListProperty<ViewTuple<TagView, TagViewModel> > listTag_selected_value = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final StringProperty tfSearch_value = new SimpleStringProperty();


    private ChangeListener<ResourceBundle> listener_ChangedValue_bundleLanguage_ = null;


    /**
     * Default constructor
     *
     * @author Gaetan Brenckle
     */
    public HomePageViewModel() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][constructor] Creation of the HomePageViewModel() object.");
        }

        //RessourceBundle Listener
        this.listener_ChangedValue_bundleLanguage_ = this::listener_bundleLanguage;
        this.resBundle_.addListener(this.listener_ChangedValue_bundleLanguage_);

        loadTag();
    }

    //TODO public -> private
    public void loadTag() {

        Task_Custom<Void> task_loadTag = new Task_Custom<>(new Image(getClass().getResourceAsStream("/img/add_64.png")), "Load tags") {

            @Override
            protected Void call_Task() {
                for (int i = 0; i < 10; i++) {
                    TagViewModel tagViewModel = new TagViewModel();
                    tagViewModel.lblTag_labelProperty().set("test");


                    final ViewTuple<TagView, TagViewModel> tagViewTuple = FluentViewLoader.fxmlView(TagView.class)
                            .viewModel(tagViewModel)
                            .load();
                    listTag_selected_value.add(tagViewTuple);
                }
                return null;
            }
        };

        task_loadTag.setOnFailed(workerStateEvent -> {
            task_loadTag.getException().printStackTrace();
        });

        TASKMANAGER.addArray(new TaskArray<>(ThreadArray.ExecutionType.PARALLEL).setLevel(0)
                .addTask(new Pair<Task_Custom<Void>, ThreadArray<?>>(task_loadTag, new TaskArray<>(ThreadArray.ExecutionType.END).setLevel(1)))
        );
    }


    //Text
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
     * Listener for the ressource bundle.
     *
     * @param observable - {@link ObservableValue} - the value observed
     * @param oldValue - {@link ResourceBundle} - the old value that are remplaced
     * @param newValue - {@link ResourceBundle} - the new value
     */
    private void listener_bundleLanguage(ObservableValue<? extends ResourceBundle> observable, ResourceBundle oldValue, ResourceBundle newValue) {
        lblOrganizer_label.set(this.resBundle_.get().getString("lblOrganizer"));
        chkShowCreatedDebate_label.set(this.resBundle_.get().getString("chkShowCreatedDebate"));
        btnCreateNewDebate_label.set(this.resBundle_.get().getString("btnCreateNewDebate"));

        btnAddTag_label.set(this.resBundle_.get().getString("btnAddTag"));    }


    @Override
    public void onViewAdded_Cycle() {
    }

    @Override
    public void onViewRemoved_Cycle() {
        if (this.listener_ChangedValue_bundleLanguage_ != null) {
            this.resBundle_.removeListener(this.listener_ChangedValue_bundleLanguage_);
            this.listener_ChangedValue_bundleLanguage_ = null;
        }
        LanguageBundle.getInstance().unbindRessourceBundle(this.resBundle_);

    }
}
