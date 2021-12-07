package fr.univlr.debathon.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PDFGenerator {

    public static Connection c;

    public static void main(String[] args) {
        try {
            c = DriverManager.getConnection("jdbc:sqlite:server/db_debathon.db"); //Creation connection on db
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }

        PDFdata.getRequest1(1);
    }

}
