package fr.univlr.debathon.server.pdf;

import fr.univlr.debathon.server.pdf.PDFGenerator;
import fr.univlr.debathon.server.pdf.PDFdata;
import fr.univlr.debathon.server.pdf.PDFquestion;

import java.io.FileNotFoundException;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.util.List;
public class TestPdf {
    /*
    public static void main(String[] args){
        //PDFGenerator pdfG = new PDFGenerator();
        //pdfG.getPDF();
        ChartGenerator gen = new ChartGenerator();
        //gen.saveAsPng();
        Map data = new HashMap<String,Float>();
        data.put("Choix 1",60.5f);
        data.put("Choix 2",10.5f);
        data.put("Choix 3",20.3f);
        data.put("Choix 4",8.7f);

        gen.savePieAsPng("Question 1",data);

    }
    */


    public static void main(String[] args) throws FileNotFoundException {
        try {
            PDFGenerator.c = DriverManager.getConnection("jdbc:sqlite:server/db_debathon.db"); //Creation connection on db
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }

        List<PDFquestion> questions = PDFdata.getRequest1(1);
        PDFGenerator.getInstance().getPDF(questions,1,"Debat 1");
        /*
        for (PDFquestion question:questions) {
            ChartGenerator.getInstance().genPieQuestion(question);
        }*/
        //chartGen.genPieQuestion(questions.get(0));
    }
}
