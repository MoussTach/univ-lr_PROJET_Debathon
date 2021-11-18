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

    public static Connection c = null;
    static String db_name = "server/db_debathon.db";


    public static void main(String[] args) throws Exception{

        ServerSocket serverSocket;
        List<UserInstance> userInstanceList = new ArrayList<>();

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:" + db_name); //Creation connection on db
            c.setAutoCommit(true);
            System.out.println("Opened database successfully");

            serverSocket = new ServerSocket(9878);

            while(true) //Loop to listen every client
            {
                Socket userSocket = serverSocket.accept();

                UserInstance userInstance = new UserInstance(userSocket);
                userInstanceList.add(userInstance);
                Thread t = new Thread(userInstance);
                t.start();
                System.out.println("======> Un nouveau thread de lancé : " + t.getName() + "  <=======");

            }


        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }

    }


}

