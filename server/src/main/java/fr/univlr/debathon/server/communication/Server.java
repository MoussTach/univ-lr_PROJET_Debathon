package fr.univlr.debathon.server.communication;

import fr.univlr.debathon.job.db_project.jobclass.Category;
import fr.univlr.debathon.job.db_project.jobclass.User;
import fr.univlr.debathon.log.generate.CustomLogger;
import fr.univlr.debathon.taskmanager.Task_Custom;
import fr.univlr.debathon.tools.AlphaNumericStringGenerator;
import javafx.concurrent.Task;

import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private static final CustomLogger LOGGER = CustomLogger.create(Server.class.getName());
    private static Server server = null;

    public static final String CREATERIGHTS_KEY = AlphaNumericStringGenerator.getRandomString(10);
    protected static List<UserInstance> USERINSTANCELIST = new ArrayList<>();

    public static Connection CONNECTION = null;
    public static List<User> listUser = new ArrayList<>();
    public static List<User> listUserUsed = new ArrayList<>();

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

        loadUser();

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



    public static void loadUser() {
        String sql = "SELECT * from User";

        try {
            PreparedStatement pstmt  = CONNECTION.prepareStatement(sql);


            ResultSet rs  = pstmt.executeQuery();

            while (rs.next()) {
                listUser.add(new User(rs.getInt("idUser"), rs.getString("label")));
            }
            pstmt.close();


        } catch (SQLException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(String.format("Error : %s", e.getMessage()), e);
            }
        }

    }


    public static User getUser () {
        for (User user : listUser) {
            if (!listUserUsed.contains(user)) {
                listUserUsed.add(user);
                return user;
            }
        }
        return new User(-1, "null");
    }


}

