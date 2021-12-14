package fr.univlr.debathon.server.communication;

import fr.univlr.debathon.log.generate.CustomLogger;
import fr.univlr.debathon.taskmanager.Task_Custom;
import fr.univlr.debathon.tools.AlphaNumericStringGenerator;
import javafx.concurrent.Task;

import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private static final CustomLogger LOGGER = CustomLogger.create(Server.class.getName());
    private static Server server = null;

    public static final String CREATERIGHTS_KEY = AlphaNumericStringGenerator.getRandomString(10);
    protected static List<UserInstance> USERINSTANCELIST = new ArrayList<>();

    public static Connection CONNECTION = null;

    private Server() {
    }

    public static Server getInstance() {
        if (server == null)
            server = new Server();
        return server;
    }

    public Task<Void> connect(String dbName, int port) throws SQLException, ClassNotFoundException {

        Class.forName("org.sqlite.JDBC");
        CONNECTION = DriverManager.getConnection("jdbc:sqlite:" + dbName); //Creation connection on db
        CONNECTION.setAutoCommit(true);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Opened database successfully");
        }

        return new Task_Custom<Void>("test the validity of the email destination") {
            @Override
            protected Void call_Task() throws Exception {
                try (ServerSocket serverSocket = new ServerSocket(port)) {
                    while(true) //Loop to listen every client
                    {
                        Socket userSocket = serverSocket.accept();

                        UserInstance userInstance = new UserInstance(userSocket);

                        userInstance.start();
                        USERINSTANCELIST.add(userInstance);

                        if (LOGGER.isInfoEnabled()) {
                            LOGGER.info("======> Un nouveau thread de lancé : " + userInstance.getName() + "  <=======");
                            LOGGER.info("Nombre de Thread présent : " + USERINSTANCELIST.size());
                        }
                    }
                } catch (Exception e) {
                    if (LOGGER.isFatalEnabled()) {
                        LOGGER.fatal("Error on connection", e);
                    }
                    throw e;
                }
            }
        };
    }
}

