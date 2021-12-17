package fr.univlr.debathon.server;

import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.ViewTuple;
import fr.univlr.debathon.log.generate.CustomLogger;
import fr.univlr.debathon.server.communication.Server;
import fr.univlr.debathon.server.view.mainwindow.MainWindowView;
import fr.univlr.debathon.server.viewmodel.mainwindow.MainWindowViewModel;
import fr.univlr.debathon.taskmanager.TaskArray;
import fr.univlr.debathon.taskmanager.ThreadArray;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Launch class.
 * Load the javaFX information and the database information.
 *
 * @author Gaetan Brenckle
 */
public class Launch extends Application {

    private static final CustomLogger LOGGER = CustomLogger.create(Launch.class.getName());
    public static final DoubleProperty progressLaunching = new SimpleDoubleProperty(-1.0);
    public static final BooleanProperty APPLICATION_STOP = new SimpleBooleanProperty(false);

    public static Stage PRIMARYSTAGE;


    /**
     * Create a default file on the computer of the user to stock the log and statistics of the user.
     *
     * @author Gaetan Brenckle
     *
     * @throws IOException - {@link IOException} exception throw when the file is not readable
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

            File[] array_resource_file = destination_directory.listFiles();
            if (array_resource_file != null) {
                for (File resource_file : array_resource_file) {
                    if (resource_file != null && resource_file.exists()) {

                        String name_resource_file = resource_file.getName();
                        String path_destination_file = String.format("%s/%s", path_destination_folder, name_resource_file);
                        File exist_destination_file = new File(path_destination_file);

                        if (!exist_destination_file.exists()) {

                            if (resource_file.isDirectory()) {
                                FileUtils.copyDirectoryToDirectory(resource_file, destination_directory);
                            } else {
                                FileUtils.copyFileToDirectory(resource_file, destination_directory);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Create db connection, on a method to te called and processed after the preload is showed.
     *
     * @author Gaetan Brenckle
     *
     * @throws SQLException - {@link SQLException} exception throw when the connection fails
     * @throws ClassNotFoundException - {@link ClassNotFoundException} exception throw when the class is not found
     */
    private void createDbConnection() throws SQLException, ClassNotFoundException {
        try {
            //Since it's a server application, the program should exit if the connection are not reached
            Task<Void> taskConnect = Server.getInstance().connect("server/db_debathon.db",9878);

            Main.TASKMANAGER.addArray(new TaskArray(ThreadArray.ExecutionType.SEQUENTIAL).addTask(new Pair(taskConnect, new TaskArray(ThreadArray.ExecutionType.END))));

        } catch (Exception e) {
            if (LOGGER.isFatalEnabled()) {
                LOGGER.fatal("[ABORT] The server couldn't open :", e);
            }
            throw e;
        }
    }

    /**
     * Main method.
     * Load every useful information before launch the main windows of the program.
     * Use a preload when the information are collected.
     *
     * @author Gaetan Brenckle
     */
    @Override
    public void init() {
        Main.TASKMANAGER.setup();
        Main.MAILMANAGER.setup();

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
     * Override method to define the necessary to launch the windows of the program.
     *
     * @author Gaetan Brenckle
     *
     * @param primaryStage {@link Stage} - the stage to launch.
     */
    @Override
    public void start(Stage primaryStage) {
        PRIMARYSTAGE = primaryStage;

        final ViewTuple<MainWindowView, MainWindowViewModel> MainWindowViewTuple = FluentViewLoader.fxmlView(MainWindowView.class).load();
        final Scene scene = new Scene(MainWindowViewTuple.getView(), 300, 100);

        primaryStage.setTitle("Debathon - server");
        final Image ico = new Image(getClass().getResourceAsStream("/img/logo/debathon_512.png"));
        primaryStage.getIcons().add(ico);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("JavaFX information correctly loaded");
        }

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
