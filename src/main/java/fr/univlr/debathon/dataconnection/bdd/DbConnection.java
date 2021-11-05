package fr.univlr.debathon.dataconnection.bdd;

import fr.univlr.debathon.dataconnection.DataConnection;
import fr.univlr.debathon.log.generate.CustomLogger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.Properties;

/**
 * Class created to handle the connection with a database.
 *
 * @author Gaetan Brenckle
 */
public abstract class DbConnection {

    private static final CustomLogger LOGGER = CustomLogger.create(DbConnection.class.getName());

    protected DataConnection dataConnection_ = null;
    protected String dbPropertiesFilePath_ = "";

    protected final BooleanProperty isConnected_ = new SimpleBooleanProperty(false);

    /**
     * Default constructor.
     */
    protected DbConnection() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][constructor] Creation of the DbConnection() object.");
        }
    }

    /**
     * Stock locally the information of the properties file to be able to stock them into the class if the connection is successful.
     *
     * @author Gaetan Brenckle
     *
     * @param properties {@link Properties} - Properties class to be able to stock easier some information.
     * @return boolean - return the state of the connection, created or not.
     */
    abstract boolean createPropertiesConnection(Properties properties);

    /**
     * Getter of the Database
     *
     * @return {@link DataConnection} - return the current database
     */
    public DataConnection getDataSource() {
        return dataConnection_;
    }

    /**
     * Getter of the properties path.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link String} - return the properties path
     */
    public String getDbPropertiesFilePath() {
        return dbPropertiesFilePath_;
    }

    /**
     * Property of the variable isConnected_.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link BooleanProperty} - return the property of the variable isConnected_.
     */
    public BooleanProperty isConnectedProperty() {
        return isConnected_;
    }


    //Getter
    public String getDriver() {
        if (this.dataConnection_ != null)
            return this.dataConnection_.getDriver();
        else
            return "";
    }

    public String getJdbcPath() {
        if (this.dataConnection_ != null)
            return this.dataConnection_.getJdbcPath();
        else
            return "";
    }

    public String getServer() {
        if (this.dataConnection_ != null)
            return this.dataConnection_.getServer();
        else
            return "";
    }

    public String getPort() {
        if (this.dataConnection_ != null)
            return this.dataConnection_.getPort();
        else
            return "";
    }

    public String getServerName() {
        if (this.dataConnection_ != null)
            return this.dataConnection_.getServerName();
        else
            return "";
    }

    public String getUser() {
        if (this.dataConnection_ != null)
            return this.dataConnection_.getUser();
        else
            return "";
    }

    public String getPassword() {
        if (this.dataConnection_ != null)
            return this.dataConnection_.getPassword();
        else
            return "";
    }
}
