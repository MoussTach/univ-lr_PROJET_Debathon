package fr.univlr.debathon.application;

import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.ViewTuple;
import fr.univlr.debathon.application.view.PreloadWindowView;
import fr.univlr.debathon.application.viewmodel.PreloadWindowViewModel;
import fr.univlr.debathon.log.generate.CustomLogger;
import javafx.application.Preloader.StateChangeNotification.Type;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Preload class.
 *
 * Launch a preload until all information are loaded.
 *
 * @author Gaetan Brenckle
 */
public class PreloadLaunch extends javafx.application.Preloader {

    private static final CustomLogger LOGGER = CustomLogger.create(PreloadLaunch.class.getName());
    private Stage preloadStage;

    @Override
    public void start(Stage primaryStage) {
        this.preloadStage = primaryStage;

        final ViewTuple<PreloadWindowView, PreloadWindowViewModel> preloadViewTuple = FluentViewLoader.fxmlView(PreloadWindowView.class).load();
        final Scene scene = new Scene(preloadViewTuple.getView());

        final Image ico = new Image(getClass().getResourceAsStream("/img/logo/debathon_512.png"));
        primaryStage.getIcons().add(ico);

        primaryStage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Preload loaded");
        }

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification stateChangeNotification) {
        if (stateChangeNotification.getType() == Type.BEFORE_START) {
            preloadStage.hide();
        }
    }
}
