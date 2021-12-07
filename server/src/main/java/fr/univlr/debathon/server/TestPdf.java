package fr.univlr.debathon.server;

import javafx.scene.chart.PieChart;
import org.jfree.data.general.PieDataset;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class TestPdf {
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
}
