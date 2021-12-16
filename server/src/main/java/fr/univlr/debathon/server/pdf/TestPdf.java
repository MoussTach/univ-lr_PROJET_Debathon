package fr.univlr.debathon.server.pdf;

import fr.univlr.debathon.server.pdf.PDFGenerator;
import fr.univlr.debathon.server.pdf.PDFdata;
import fr.univlr.debathon.server.pdf.PDFquestion;

import java.io.FileNotFoundException;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.util.List;
public class TestPdf {

    public static void main(String[] args) throws FileNotFoundException {
        try {
            PDFGenerator.c = DriverManager.getConnection("jdbc:sqlite:server/db_debathon.db"); //Creation connection on db
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }

        List<PDFquestion> questions = PDFdata.getRequest1(1);
        PDFGenerator.getInstance().getPDF(questions,1);
        /*
        for (PDFquestion question:questions) {
            ChartGenerator.getInstance().genPieQuestion(question);
        }*/
        //chartGen.genPieQuestion(questions.get(0));
    }
}
