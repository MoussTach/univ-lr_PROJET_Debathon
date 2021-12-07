package fr.univlr.debathon.server;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

public class PDFGenerator {

    public PDFGenerator(){}

    public Document getPDF(){
        Document pdf = new Document(PageSize.A4);
        try {
            PdfWriter.getInstance(pdf,new FileOutputStream("test.pdf"));
            pdf.open();
            pdf.addTitle("Test Title");
            pdf.add(new Paragraph("Hello World"));

        }
        catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
        pdf.close();
        return pdf;
    }







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
