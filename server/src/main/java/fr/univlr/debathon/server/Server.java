package fr.univlr.debathon.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

public class Server {


    public static final int port = 8537;
    static final int taille = 8192;
    static final byte buffer[] = new byte[taille];

    public static final UserManager userManager = new UserManager();

    public static Connection c = null;
    static final String db_name = "server/db_debathon.db";


    public static void main(String[] args) throws Exception{

        ServerSocket serverSocket;
        List<UserInstance> userInstanceList = new ArrayList<>();

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:" + db_name); //Creation connection on db
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            serverSocket = new ServerSocket(9878);

            while(true) //Loop to listen every client
            {
                Socket userSocket = serverSocket.accept();

                UserInstance userInstance = new UserInstance(userSocket);
                userInstanceList.add(userInstance);
                Thread t = new Thread(userInstance);
                t.start();

                for (UserInstance ui : userInstanceList) {
                    System.out.println("----");
                    System.out.println("----");
                }
            }


        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }

    }


}

