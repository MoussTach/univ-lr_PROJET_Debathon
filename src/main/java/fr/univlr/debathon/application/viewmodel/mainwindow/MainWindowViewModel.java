package fr.univlr.debathon.application.viewmodel.mainwindow;

import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.InjectScope;
import de.saxsys.mvvmfx.ScopeProvider;
import de.saxsys.mvvmfx.ViewTuple;
import fr.univlr.debathon.application.Launch;
import fr.univlr.debathon.application.view.taskmanager.TaskManagerWindowView;
import fr.univlr.debathon.application.viewmodel.ViewModel_SceneCycle;
import fr.univlr.debathon.application.viewmodel.taskmanager.TaskManagerWindowViewModel;
import fr.univlr.debathon.language.LanguageBundle;
import fr.univlr.debathon.log.generate.CustomLogger;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.ResourceBundle;

/**
 * ViewModel of the view {@link fr.univlr.debathon.application.view.mainwindow.MainWindowView}.
 * This ViewModel process some fonction for the whole application.
 *
 * @author Gaetan Brenckle
 */
@ScopeProvider(scopes= {MainScope.class})
public class MainWindowViewModel extends ViewModel_SceneCycle {

    private final ObjectProperty<ResourceBundle> resBundleWindow_ = LanguageBundle.getInstance().bindResourceBundle("properties.language.lg_window");

    private static final CustomLogger LOGGER = CustomLogger.create(MainWindowViewModel.class.getName());

    //Text
    private final StringProperty taskWindow_title_ = new SimpleStringProperty(this.resBundleWindow_.get().getString("taskWindow_title"));

    //Value
    private final BooleanProperty taskWindow_isShowed_ = new SimpleBooleanProperty(false);

    private ChangeListener<ResourceBundle> listener_ChangedValue_bundleLanguage_Window_ = null;

    @InjectScope
    private MainScope mainScope;


    /**
     * Default constructor
     *
     * @author Gaetan Brenckle
     */
    public MainWindowViewModel() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][constructor] Creation of the MainWindowViewModel() object.");
        }

        //RessourceBundle Listener
        if (this.listener_ChangedValue_bundleLanguage_Window_ == null) {
            this.listener_ChangedValue_bundleLanguage_Window_ = this::listener_bundleLanguage_window;
            this.resBundleWindow_.addListener(this.listener_ChangedValue_bundleLanguage_Window_);
        }
    }

    /**
     * action to open the external view of status.
     *
     * @author Gaetan Brenckle
     */
    public void act_openTaskExternal() {

        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][method] Usage of the MainWindowViewModel.act_openTaskExternal()");
        }

        if (!taskWindow_isShowed_.get()) {
            try {
                final ViewTuple<TaskManagerWindowView, TaskManagerWindowViewModel> taskExternalViewTuple = FluentViewLoader.fxmlView(TaskManagerWindowView.class)
                        .load();
                final Scene scene = new Scene(taskExternalViewTuple.getView());
                final Stage stage = new Stage();

                stage.titleProperty().bind(this.taskWindow_title_);
                stage.initOwner(Launch.PRIMARYSTAGE);

                final Image ico = new Image(this.getClass().getResourceAsStream("/img/logo/Logo_univLR_64.png"));
                stage.getIcons().add(ico);
                stage.setScene(scene);

                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("JavaFX information correctly loaded");
                }

                stage.show();
                taskWindow_isShowed_.set(true);
                stage.setOnCloseRequest(event -> taskWindow_isShowed_.set(false));

            } catch (Exception e) {
                if (LOGGER.isFatalEnabled()) {
                    LOGGER.fatal("FATAL ERROR - external status windows can't be loaded", e);
                }
            }
        }
    }


    /**
     * Setter for the scope property mainScope.bPaneNodeProperty().
     *
     * @author Gaetan Brenckle
     * @param bPaneMain - borderPane to link
     */
    public void setbPaneMainProperty(BorderPane bPaneMain) {
        mainScope.bPane_main_Property().set(bPaneMain);
    }

    /**
     * Bind the progress property with the scope.
     *
     * @author Gaetan Brenckle
     * @param doubleProperty - progress property to link
     */
    public void bindProgressProperty(DoubleProperty doubleProperty) {
        doubleProperty.bind(mainScope.progressProperty());
    }

    /**
     * Bind the progress_label property with the scope.
     *
     * @author Gaetan Brenckle
     * @param labelProperty - progress_label to link
     */
    public void bindProgress_labelProperty(StringProperty labelProperty) {
        labelProperty.bind(mainScope.progress_labelProperty());
    }


    /**
     * Listener for the ressource bundle.
     *
     * @param observable - {@link ObservableValue} - the value observed
     * @param oldValue - {@link ResourceBundle} - the old value that are remplaced
     * @param newValue - {@link ResourceBundle} - the new value
     */
    private void listener_bundleLanguage_window(ObservableValue<? extends ResourceBundle> observable, ResourceBundle oldValue, ResourceBundle newValue) {
        this.taskWindow_title_.set(this.resBundleWindow_.get().getString("taskWindow_title"));
    }


    @Override
    public void onViewAdded_Cycle() {
        // default implementation
    }

    @Override
    public void onViewRemoved_Cycle() {
        if (this.listener_ChangedValue_bundleLanguage_Window_ != null) {
            this.resBundleWindow_.removeListener(this.listener_ChangedValue_bundleLanguage_Window_);
            this.listener_ChangedValue_bundleLanguage_Window_ = null;
        }
        LanguageBundle.getInstance().unbindRessourceBundle(this.resBundleWindow_);
    }
}