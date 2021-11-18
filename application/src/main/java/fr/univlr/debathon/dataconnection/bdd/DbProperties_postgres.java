package fr.univlr.debathon.dataconnection.bdd;

import fr.univlr.debathon.dataconnection.DataConnection;
import fr.univlr.debathon.log.generate.CustomLogger;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Class created to handle the connection with a database.
 *
 * @author Gaetan Brenckle
 */
public class DbProperties_postgres extends DbConnection {
    private static final CustomLogger LOGGER = CustomLogger.create(DbProperties_postgres.class.getName());

    /**
     * Default Constructor.
     * It will parse the properties file and stock some useful information to create a connection
     *
     * @author Gaetan Brenckle
     *
     * @param propertiesFilePath {@link String} - path of the properties file. You can use a relative path.
     */
    public DbProperties_postgres(String propertiesFilePath) {
        super();

        File propertiesFile;
        Properties properties;

        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace(String.format("[public][constructor] Creation of the DbConnection_postgres(%s) object.", propertiesFilePath));
        }
        try {
            if ((propertiesFilePath == null) || (propertiesFilePath.isEmpty())) {
                if (LOGGER.isErrorEnabled()) {
                    LOGGER.error("Path defined for the properties file is empty or null.");
                }
                return;
            }

            propertiesFile = new File(propertiesFilePath);
            if (!propertiesFile.exists()) {
                if (LOGGER.isErrorEnabled()) {
                    LOGGER.error(String.format("%s -> [%s]", "Path defined for the properties file cannot be found.", propertiesFilePath));
                }

                if (LOGGER.isWarnEnabled()) {
                    LOGGER.warn(String.format("%s -> [%s]", "Creation of the file", propertiesFilePath));
                }

                if (propertiesFile.createNewFile()) {
                    if (LOGGER.isInfoEnabled()) {
                        LOGGER.info(String.format("%s -> [%s]", "New file created", propertiesFilePath));
                    }
                } else {
                    if (LOGGER.isErrorEnabled()) {
                        LOGGER.error(String.format("%s -> [%s]", "Unable to create the file", propertiesFilePath));
                    }
                }
            }

            properties = new Properties();
            try(InputStream propertiesFileStream = new FileInputStream(propertiesFile)) {
                properties.load(propertiesFileStream);
            }

            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(String.format("Properties file correctly loaded [Path=%s]", propertiesFilePath));
            }

            createPropertiesConnection(properties);
            dbPropertiesFilePath_ = propertiesFilePath;
        }
        catch (IOException e) {
            if (LOGGER.isFatalEnabled()) {
                LOGGER.fatal(String.format("FATAL ERROR : %s", e.getMessage()), e);
            }
        }
    }

    /**
     * Method used to change the information stocked into the properties file if they are true.
     * For that, this method will test the connection before replace the old information by the newer.
     *
     * @author Gaetan Brenckle
     *
     * @param server {@link String} - The server of the database than the program can use.
     * @param port {@link String} - The server port of the database than the program can use.
     * @param name {@link String} - The name of the database than the program can use.
     * @param user {@link String} - The user of the database than the program can use.
     * @param password [{@link String} - The password of the database.
     * @return boolean - return the state of the connection, if it is true, the information given in parameter have update the information of the properties file.
     */
    public boolean createConnection(String server, String port, String name, String user, String password) {

        try {
            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace(String.format("[public][method] usage of DbConnection_postgres.createConnection(%s, %s, %s, %s, %s).", server, port, name, user, password));
            }

            if (dbPropertiesFilePath_ == null) {
                if (LOGGER.isErrorEnabled()) {
                    LOGGER.error("Path defined for the properties file cannot be found.");
                }
                return false;
            }

            final Properties properties = new Properties();

            properties.setProperty("dbServer", server);
            properties.setProperty("dbPort", port);

            properties.setProperty("dbName", name);
            properties.setProperty("dbUser", user);
            properties.setProperty("dbPassword", password);

            if (createPropertiesConnection(properties)) {
                try(final FileOutputStream outputStream = new FileOutputStream(dbPropertiesFilePath_)) {
                    properties.store(outputStream, null);
                }

                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info(String.format("Update the properties file with newer information [Path=%s]", dbPropertiesFilePath_));
                }
            }
            return true;

        } catch (IOException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(String.format("Error catch : %s", e.getMessage()), e);
            }
        }
        return false;
    }

    /**
     * Stock locally the information of the properties file to be able to stock them into the class if the connection is successful.
     *
     * @author Gaetan Brenckle
     *
     * @param properties {@link Properties} - Properties class to be able to stock easier some information.
     * @return boolean - return the state of the connection, created or not.
     */
    @Override
    protected boolean createPropertiesConnection(Properties properties) {
        String server;
        String port;
        String name;

        String user;
        String password;

        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace(String.format("[private][method] usage of DbConnection_postgres.usePropertiesConnection(%s).", properties));
        }

        server = properties.getProperty("dbServer");
        port = properties.getProperty("dbPort");

        name = properties.getProperty("dbName");
        user = properties.getProperty("dbUser");
        password = properties.getProperty("dbPassword");

        if (((server == null) || (port == null) || (name == null) || (user == null) || (password == null)) &&
                (LOGGER.isErrorEnabled())) {
            LOGGER.error("A properties needed doesn't exist on this properties file.");
            return false;
        }

        try {
            if (this.dataConnection_ != null) {
                boolean isClosed = this.dataConnection_.disconnect();

                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info(String.format("The last database is closed [%b]", isClosed));
                }
            }
            this.dataConnection_ = new DataConnection("org.postgresql.Driver", "jdbc:postgresql", server, port, name, user, password);
            try (Connection conn = this.dataConnection_.getConnection()) {
                if (conn != null && conn.isValid(3)) {

                    isConnected_.set(true);

                    if (LOGGER.isInfoEnabled()) {
                        LOGGER.info(String.format("[PostGresSql] The connection is created - [%s/%s]", server, port));
                    }
                    return true;
                }
            }
        } catch (SQLException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(String.format("Error caught : exception when try connecting : %s", e.getMessage()), e);
            }
        }
        isConnected_.set(false);
        return false;
    }
}
