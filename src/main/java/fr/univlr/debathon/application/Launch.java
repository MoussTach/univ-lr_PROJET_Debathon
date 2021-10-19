package fr.univlr.debathon.application;

import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.ViewTuple;
import fr.univlr.debathon.application.view.mainwindow.MainWindowView;
import fr.univlr.debathon.application.viewmodel.mainwindow.MainWindowViewModel;
import fr.univlr.debathon.dataconnection.bdd.DbProperties_postgres;
import fr.univlr.debathon.log.generate.CustomLogger;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * Launch class.
 * Load the javaFX informations and the database information.
 *
 * @author Gaetan Brenckle
 */
public class Launch extends Application {

    private static final CustomLogger LOGGER = CustomLogger.create(Launch.class.getName());

    public static final DoubleProperty progressLaunching = new SimpleDoubleProperty(-1.0);
    public static final BooleanProperty APPLICATION_STOP = new SimpleBooleanProperty(false);
    public static Stage PRIMARYSTAGE;


    /**
     * Create a default file on the computer of the user to stock the log and the profile of the user.
     *
     * @author Gaetan Brenckle
     *
     * @throws IOException - {@link IOException} exception throwed when the file is not readeable
     */
    private void createDefaultFile() throws IOException {

        final Properties properties = new Properties();
        properties.load(getClass().getResourceAsStream("/properties/default.properties"));
        String name_destination_folder = properties.getProperty("name_data_folder");
        String path_destination_folder = String.format("%s/.%s/%s", System.getProperty("user.home"), properties.getProperty("name"), name_destination_folder);
        File destination_directory = new File(path_destination_folder);
        boolean destination_directory_exist = destination_directory.exists();

        if (!destination_directory_exist) {
            destination_directory_exist = destination_directory.mkdir();
        }

        if (destination_directory_exist) {

            File[] array_ressource_file = destination_directory.listFiles();
            for (File ressource_file : array_ressource_file) {
                if (ressource_file != null && ressource_file.exists()) {

                    String name_ressource_file = ressource_file.getName();
                    String path_destination_file = String.format("%s/%s", path_destination_folder, name_ressource_file);
                    File exist_destination_file = new File(path_destination_file);

                    if (!exist_destination_file.exists()) {

                        if (ressource_file.isDirectory()) {
                            FileUtils.copyDirectoryToDirectory(ressource_file, destination_directory);
                        } else {
                            FileUtils.copyFileToDirectory(ressource_file, destination_directory);
                        }
                    }
                }
            }
        }
    }

    /**
     * Create db connection, on a method to te called and processed after the preloader is showed.
     *
     * @author Gaetan Brenckle
     *
     * @throws IOException - {@link IOException} exception throwed when the file is not readeable
     */
    private void createDbConnection() throws IOException {

        final Properties properties = new Properties();
        properties.load(getClass().getResourceAsStream("/properties/default.properties"));
        String name_destination_folder = properties.getProperty("name_data_folder");
        String path_destination_folder = String.format("%s/.%s/%s", System.getProperty("user.home"), properties.getProperty("name"), name_destination_folder).replaceAll("\\\\","/");

        String path_DBProperties = String.format("%s/%s", path_destination_folder, "db.properties");
        Main.DB_CONNECTION = new DbProperties_postgres(path_DBProperties);
    }

    /**
     * Main method.
     * Load every useful information before launch the main windows of the program.
     * Use a preloader when the informations are collected.
     *
     * @author Gaetan Brenckle
     */
    @Override
    public void init() {
        Main.TASKMANAGER.setup();

        try {
            createDefaultFile();
            createDbConnection();

        } catch (Exception e) {
            if (LOGGER.isFatalEnabled()) {
                LOGGER.fatal(String.format("FATAL ERROR, %s", e.getMessage()), e);
            }
        }
    }

    /**
     * Method called when the application is stopped.
     *
     * @author Gaetan Brenckle
     */
    @Override
    public void stop(){
        APPLICATION_STOP.set(true);
    }

    /**
     * Override method to define the neccesary to launch the windows of the program.
     *
     * @author Gaetan Brenckle
     *
     * @param primaryStage {@link Stage} - the stage to launch.
     */
    @Override
    public void start(Stage primaryStage) {
        PRIMARYSTAGE = primaryStage;

        final ViewTuple<MainWindowView, MainWindowViewModel> MainWindowViewTuple = FluentViewLoader.fxmlView(MainWindowView.class).load();
        final Scene scene = new Scene(MainWindowViewTuple.getView(), 800.0D, 600.0D);

        primaryStage.setTitle("Project Database - application");
        final Image ico = new Image(getClass().getResourceAsStream("/img/logo/Logo_univLR_64.png"));
        primaryStage.getIcons().add(ico);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("JavaFX information correctly loaded");
        }

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
