package fr.univlr.debathon.application.viewmodel.mainwindow;

import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.InjectScope;
import de.saxsys.mvvmfx.ViewTuple;
import fr.univlr.debathon.application.Launch;
import fr.univlr.debathon.application.view.sidewindow.options.OptionsWindowView;
import fr.univlr.debathon.application.viewmodel.ViewModel_SceneCycle;
import fr.univlr.debathon.application.viewmodel.sidewindow.options.OptionsWindowViewModel;
import fr.univlr.debathon.language.LanguageBundle;
import fr.univlr.debathon.log.generate.CustomLogger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ResourceBundle;

/**
 * ViewModel of the view {@link fr.univlr.debathon.application.view.mainwindow.MenuBarView}.
 * This ViewModel process some fonction for the menubar
 *
 * @author Gaetan Brenckle
 */
public class MenuBarViewModel extends ViewModel_SceneCycle {

    private static final CustomLogger LOGGER = CustomLogger.create(MenuBarViewModel.class.getName());

    private final ObjectProperty<ResourceBundle> resBundle_ = LanguageBundle.getInstance().bindResourceBundle("properties.language.mainwindow.lg_menubar");
    private final ObjectProperty<ResourceBundle> resBundleWindow_ = LanguageBundle.getInstance().bindResourceBundle("properties.language.lg_window");

    private final StringProperty menuBarFile_label_ = new SimpleStringProperty(this.resBundle_.get().getString("menuBarFile"));

    private final StringProperty userConnectionWindow_title_ = new SimpleStringProperty(this.resBundleWindow_.get().getString("userConnectionWindows_title"));
    private final StringProperty menuBarFile_menuItemOption_label_ = new SimpleStringProperty(this.resBundle_.get().getString("menuBarFile_menuItemOption"));
    private final StringProperty optionsWindow_title_ = new SimpleStringProperty(this.resBundleWindow_.get().getString("optionsWindows_title"));

    private final StringProperty menuBarHelp_label_ = new SimpleStringProperty(this.resBundle_.get().getString("menuBarHelp"));
    private final StringProperty menuBarHelp_menuItemAbout_label_ = new SimpleStringProperty(this.resBundle_.get().getString("menuBarHelp_menuItemAbout"));

    private ChangeListener<ResourceBundle> listener_ChangedValue_bundleLanguage_ = null;
    private ChangeListener<ResourceBundle> listener_ChangedValue_bundleLanguage_title_ = null;

    @InjectScope
    private MainScope mainScope;


    /**
     * Default Constructor.
     * Create a RessourceBundle listener.
     *
     * @author Gaetan Brenckle
     */
    public MenuBarViewModel() {

        //RessourceBundle Listener
        if (this.listener_ChangedValue_bundleLanguage_ == null) {
            this.listener_ChangedValue_bundleLanguage_ = this::listener_bundleLanguage;
            this.resBundle_.addListener(this.listener_ChangedValue_bundleLanguage_);
        }

        if (this.listener_ChangedValue_bundleLanguage_title_ == null) {
            this.listener_ChangedValue_bundleLanguage_title_ = this::listener_bundleLanguage_title;
            this.resBundleWindow_.addListener(this.listener_ChangedValue_bundleLanguage_title_);
        }
    }

    /**
     * action that will be called on view.
     * Open the options window.
     *
     * @author Gaetan Brenckle
     */
    public void act_Open_OptionWindow() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][method] usage of MenuBarViewModel.act_Open_OptionWindow().");
        }

        try {
            final ViewTuple<OptionsWindowView, OptionsWindowViewModel> OptionsViewTuple = FluentViewLoader.fxmlView(OptionsWindowView.class).load();
            final Scene scene = new Scene(OptionsViewTuple.getView());
            final Stage stage = new Stage();

            stage.titleProperty().bind(this.optionsWindow_title_);
            stage.setMinWidth(1000.0);
            stage.setMinHeight(600.0);
            stage.setMaxWidth(1200.0);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(Launch.PRIMARYSTAGE);

            final Image ico = new Image(this.getClass().getResourceAsStream("/img/logo/Logo_univLR_64.png"));
            stage.getIcons().add(ico);
            stage.setScene(scene);

            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("JavaFX information correctly loaded");
            }

            stage.show();

        } catch (Exception e) {
            if (LOGGER.isFatalEnabled()) {
                LOGGER.fatal("FATAL ERROR - Options windows can't be loaded", e);
            }
        }
    }


    /**
     * Property of the variable menuBarFile_label_.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link StringProperty} - return the property of the variable menuBarFile_label_.
     */
    public StringProperty menuBarFile_label_Property() {
        return this.menuBarFile_label_;
    }

    /**
     * Property of the variable menuBarFile_menuItemOption_label_.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link StringProperty} - return the property of the variable menuBarFile_menuItemOption_label_.
     */
    public StringProperty menuBarFile_menuItemOption_labelProperty() {
        return this.menuBarFile_menuItemOption_label_;
    }

    /**
     * Property of the variable menuBarHelp_label_.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link StringProperty} - return the property of the variable menuBarHelp_label_.
     */
    public StringProperty menuBarHelp_label_Property() {
        return this.menuBarHelp_label_;
    }

    /**
     * Property of the variable menuBarHelp_menuItemAbout_label_.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link StringProperty} - return the property of the variable menuBarHelp_menuItemAbout_label_.
     */
    public StringProperty menuBarHelp_menuItemAbout_label_Property() {
        return this.menuBarHelp_menuItemAbout_label_;
    }


    /**
     * Listener for the ressource bundle.
     *
     * @param observable - {@link ObservableValue} - the value observed
     * @param oldValue - {@link ResourceBundle} - the old value that are remplaced
     * @param newValue - {@link ResourceBundle} - the new value
     */
    private void listener_bundleLanguage(ObservableValue<? extends ResourceBundle> observable, ResourceBundle oldValue, ResourceBundle newValue) {
        this.menuBarFile_label_.set(this.resBundle_.get().getString("menuBarFile"));
        this.menuBarFile_menuItemOption_label_.set(this.resBundle_.get().getString("menuBarFile_menuItemOption"));

        this.menuBarHelp_label_.set(this.resBundle_.get().getString("menuBarHelp"));
        this.menuBarHelp_menuItemAbout_label_.set(this.resBundle_.get().getString("menuBarHelp_menuItemAbout"));
    }

    /**
     * Listener for the ressource bundle of title for window.
     *
     * @param observable - {@link ObservableValue} - the value observed
     * @param oldValue - {@link ResourceBundle} - the old value that are remplaced
     * @param newValue - {@link ResourceBundle} - the new value
     */
    private void listener_bundleLanguage_title(ObservableValue<? extends ResourceBundle> observable, ResourceBundle oldValue, ResourceBundle newValue) {
        this.userConnectionWindow_title_.set(this.resBundleWindow_.get().getString("userConnectionWindows_title"));
        this.optionsWindow_title_.set(this.resBundleWindow_.get().getString("optionsWindows_title"));
    }


    @Override
    public void onViewAdded_Cycle() {
        // Default implementation
    }

    @Override
    public void onViewRemoved_Cycle() {
        if (this.listener_ChangedValue_bundleLanguage_ != null) {
            this.resBundle_.removeListener(this.listener_ChangedValue_bundleLanguage_);
            this.listener_ChangedValue_bundleLanguage_ = null;
        }
        LanguageBundle.getInstance().unbindRessourceBundle(this.resBundle_);

        if (this.listener_ChangedValue_bundleLanguage_title_ != null) {
            this.resBundleWindow_.removeListener(this.listener_ChangedValue_bundleLanguage_title_);
            this.listener_ChangedValue_bundleLanguage_title_ = null;
        }
        LanguageBundle.getInstance().unbindRessourceBundle(this.resBundleWindow_);

        mainScope.progress_labelProperty().unbind();
        mainScope.progressProperty().unbind();
    }
}