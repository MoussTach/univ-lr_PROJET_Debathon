package fr.univlr.debathon.server.communication;

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
    private static Server server = null; // instance du serveur

    public static final String CREATERIGHTS_KEY = AlphaNumericStringGenerator.getRandomString(10); // clé de sécurité
    protected static List<UserInstance> USERINSTANCELIST = new ArrayList<>(); // liste des applications connéctées

    public static Connection CONNECTION = null; // connection sql
    public static List<User> listUser = new ArrayList<>(); // liste des noms utilisateurs dispos
    public static List<User> listUserUsed = new ArrayList<>(); // liste des noms d'utilisateur utilisés

    private Server() {
    }

    public static Server getInstance() { // Singleton
        if (server == null)
            server = new Server();
        return server;
    }


    /**
     * connxion de la base de données et lancement du serveur
     * @param dbName nom de la base de données
     * @param port port
     * @return Task
     * @throws SQLException exception
     * @throws ClassNotFoundException exception
     */
    public Task<Void> connect(String dbName, int port) throws SQLException, ClassNotFoundException {

        // connexion
        Class.forName("org.sqlite.JDBC");
        CONNECTION = DriverManager.getConnection("jdbc:sqlite:" + dbName); //Creation connection on db
        CONNECTION.setAutoCommit(true);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Opened database successfully");
        }

        loadUser(); // chargement des psuedos

        return new Task_Custom<Void>("test the validity of the email destination") {
            @Override
            protected Void call_Task() throws Exception {
                try (ServerSocket serverSocket = new ServerSocket(port)) { // test de la conxion
                    while(true) //boucle infini
                    {
                        Socket userSocket = serverSocket.accept(); // recption d'une connexion ou mise en attente tant qu'il n'y en a pas une

                        UserInstance userInstance = new UserInstance(userSocket); //initialisation d'un nouveau UserInstance

                        userInstance.start(); // lancement du thread
                        USERINSTANCELIST.add(userInstance); // ajout à la liste des apps connectés

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


    /**
     * Load les psuedos
     */
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

    /**
     * Revoie un psuedo disponible
     * @return un objet User
     */
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

