package fr.univlr.debathon.application;

import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.ViewTuple;
import fr.univlr.debathon.application.view.PreloaderWindowView;
import fr.univlr.debathon.application.viewmodel.PreloaderWindowViewModel;
import fr.univlr.debathon.log.generate.CustomLogger;
import javafx.application.Preloader.StateChangeNotification.Type;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Preloader class.
 *
 * Launch a preloader until all information are loaded.
 *
 * @author Gaetan Brenckle
 */
public class Preloader extends javafx.application.Preloader {

    private static final CustomLogger LOGGER = CustomLogger.create(Preloader.class.getName());
    private Stage preloaderStage;

    @Override
    public void start(Stage primaryStage) {
        this.preloaderStage = primaryStage;

        final ViewTuple<PreloaderWindowView, PreloaderWindowViewModel> preloaderViewTuple = FluentViewLoader.fxmlView(PreloaderWindowView.class).load();
        final Scene scene = new Scene(preloaderViewTuple.getView());

        final Image ico = new Image(getClass().getResourceAsStream("/img/logo/Logo_univLR_64.png"));
        primaryStage.getIcons().add(ico);

        primaryStage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Preloader loaded");
        }

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification stateChangeNotification) {
        if (stateChangeNotification.getType() == Type.BEFORE_START) {
            preloaderStage.hide();
        }
    }
}
