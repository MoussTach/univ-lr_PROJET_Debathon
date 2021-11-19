package fr.univlr.debathon.dataconnection;

import fr.univlr.debathon.log.generate.CustomLogger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataConnection {
    private static final CustomLogger LOGGER = CustomLogger.create(DataConnection.class.getName());

    private final String driver;
    private final String jdbcPath;

    private final String server;
    private final String port;

    private final String serverName;
    private final String user;
    private final String password;
    private Connection connection = null;

    public DataConnection(String driver, String jdbcPath, String server, String port, String serverName, String user, String password) {
        this.driver = driver;
        this.jdbcPath = jdbcPath;

        this.server = server;
        this.port = port;

        this.serverName = serverName;
        this.user = user;
        this.password = password;
    }

    public DataConnection(String server, String port, String serverName, String user, String password) {
        this("org.postgresql.Driver", "jdbc:postgresql", server, port, serverName, user, password);
    }


    /**
     * Use value specified to create a connection
     *
     * @author Gaetan Brenckle
     *
     * @return - {@link Connection} - return a connection for the database
     */
    public Connection getConnection() {
        try {
            if (this.driver != null && !this.driver.isEmpty()) {
                Class.forName(this.driver);
            }

            return connection = DriverManager.getConnection(String.format("%s://%s:%s/%s", this.jdbcPath, this.server, this.port, this.serverName), this.user, this.password);

        } catch (ClassNotFoundException | SQLException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(String.format("Error when trying to create a connection : %s", e.getMessage()), e);
            }
        }
        return null;
    }

    /**
     * Disconnect the current connection.
     * Since most of driver implement now a connectionPool, the disconnect should only be used
     * when the database need to be changed
     *
     * @author Gaetan Brenckle
     *
     * @return - boolean - return if the database is close
     */
    public boolean disconnect() {
        try {
            if (connection != null) {
                connection.close();
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(String.format("Error when trying to close a connection : %s", e.getMessage()), e);
            }
            return false;
        }
    }


    //Getter
    public String getDriver() {
        return driver;
    }

    public String getJdbcPath() {
        return jdbcPath;
    }

    public String getServer() {
        return server;
    }

    public String getPort() {
        return port;
    }

    public String getServerName() {
        return serverName;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}