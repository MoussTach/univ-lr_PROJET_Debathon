package fr.univlr.debathon.server.viewmodel.mainwindow;

import fr.univlr.debathon.log.generate.CustomLogger;
import fr.univlr.debathon.server.Main;
import fr.univlr.debathon.server.communication.Server;
import fr.univlr.debathon.server.mailmanager.MailData;
import fr.univlr.debathon.server.mailmanager.MailManager;
import fr.univlr.debathon.server.viewmodel.ViewModel_SceneCycle;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.BodyPart;
import jakarta.mail.internet.MimeBodyPart;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;

/**
 * ViewModel of the view {@link fr.univlr.debathon.server.view.mainwindow.MainWindowView}.
 * This ViewModel process some function for the whole application.
 *
 * @author Gaetan Brenckle
 */
public class MainWindowViewModel extends ViewModel_SceneCycle {

    private static final CustomLogger LOGGER = CustomLogger.create(MainWindowViewModel.class.getName());

    private final StringProperty tfKey_value = new SimpleStringProperty();

    /**
     * Default constructor
     *
     * @author Gaetan Brenckle
     */
    public MainWindowViewModel() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][constructor] Creation of the MainWindowViewModel() object.");
        }

        tfKey_value.set(Server.CREATERIGHTS_KEY);
    }


    /**
     * Property of the variable tfKey_value.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link StringProperty} - return the property of the variable tfKey_value.
     */
    public StringProperty tfKey_valueProperty() {
        return tfKey_value;
    }


    @Override
    public void onViewAdded_Cycle() {
        // default implementation
    }

    @Override
    public void onViewRemoved_Cycle() {
        // default implementation
    }
}