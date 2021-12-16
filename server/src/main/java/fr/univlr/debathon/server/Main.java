package fr.univlr.debathon.server;

import com.sun.javafx.application.LauncherImpl;
import fr.univlr.debathon.log.generate.CustomLogger;
import fr.univlr.debathon.server.mailmanager.MailManager;
import fr.univlr.debathon.taskmanager.TaskManager;

import java.io.IOException;
import java.util.Properties;

/**
 * Main class.
 * Load the javaFX information and the database information with a preload before send the main to the controller of the main windows.
 *
 * @author Gaetan Brenckle
 */
public class Main {

    private static final CustomLogger LOGGER = CustomLogger.create(Main.class.getName());

    public static final TaskManager TASKMANAGER = new TaskManager();
    public static final MailManager MAILMANAGER = new MailManager();


    /**
     * Main method.
     *
     * @author Gaetan Brenckle
     *
     * @param args {@link String[]} - main arguments when you launch with the console.
     */
    public static void main(String[] args) {

        try {
            final Properties properties = new Properties();
            properties.load(Main.class.getResourceAsStream("/properties/default.properties"));

            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(String.format("Build version : %s", properties.getProperty("build_version")));
            }

        } catch (IOException e) {
            if (LOGGER.isFatalEnabled()) {
                LOGGER.fatal(String.format("FATAL ERROR, Build version can't be showed : %s", e.getMessage()), e);
            }
        }

        LauncherImpl.launchApplication(Launch.class, PreloadLaunch.class, args);
    }
}