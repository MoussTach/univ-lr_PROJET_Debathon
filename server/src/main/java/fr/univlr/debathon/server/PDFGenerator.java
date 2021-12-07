package fr.univlr.debathon.server;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import org.jfree.chart.JFreeChart;

public class PDFGenerator {
    public static Connection c;
    public PDFGenerator(){}

    public Document getPDF(PDFquestion question){
        Document pdf = new Document(PageSize.A4);
        JFreeChart chart = ChartGenerator.getInstance().genPieChart(question);

        try {
            //
            PdfWriter writer = PdfWriter.getInstance(pdf,new FileOutputStream("recapitulatif.pdf"));
            pdf.open();
            pdf.addTitle("Récapitulatif");
            Paragraph p = new Paragraph("Récapitulatif");
            p.setAlignment("Center");
            pdf.add(p);
            BufferedImage buffer = chart.createBufferedImage((int) (pdf.getPageSize().getWidth()/2),(int) (pdf.getPageSize().getHeight()/5));
            Image image = Image.getInstance(writer,buffer,1.0f);
            image.scaleToFit(PageSize.A4.getWidth()/2, image.getScaledWidth()/2);
            float x = (PageSize.A4.getWidth() - image.getScaledWidth()) / 2;
            image.setAbsolutePosition(x, image.getAbsoluteY());
            pdf.add(image);


        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }

        pdf.close();
        return pdf;
    }


}
