package fr.univlr.debathon.server.pdf;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import javax.swing.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChartGenerator extends JFrame {
    public static ChartGenerator instance;

    public ChartGenerator() {
    }

    public static ChartGenerator getInstance(){
        if(instance == null){
            instance = new ChartGenerator();
        }
        return instance;
    }

    //Creation PieDatasat avec map
    public PieDataset getPieDataset(Map<String, Float> data) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        //Boucle sur chaque element du Map
        for (Map.Entry entry : data.entrySet()) {
            dataset.setValue(entry.getKey().toString(), Float.parseFloat(entry.getValue().toString()));
        }
        return dataset;
    }

    //Generation pie chart avec une question
    public JFreeChart genPieChart(PDFquestion question) {
        //Recuperation Map
        Map<String,Float> data = question.getMapResponse();
        //Recuperation dataset avec map
        PieDataset dataset = getPieDataset(data);
        //Cration piechart avec Factory
        JFreeChart chartPie = ChartFactory.createPieChart(
                question.getLabelQuestion(),   // chart title
                dataset,          // data
                true,             // include legend
                true,
                false);
        //Format Label
        PieSectionLabelGenerator labelGenerator = new StandardPieSectionLabelGenerator(
                "{0} : ({1})",
                new DecimalFormat("0"),
                new DecimalFormat("0%"));

        ((PiePlot) chartPie.getPlot()).setLabelGenerator(labelGenerator);
        // Create Panel
        ChartPanel panel = new ChartPanel(chartPie);
        setContentPane(panel);
        return chartPie;
    }

    //generation et recuperation list de chart
    public List<JFreeChart> genAllPieCharts(List<PDFquestion> questions){
        List<JFreeChart> charts = new ArrayList<>();
        //boucle sur chaque question de la liste
        for (PDFquestion q : questions) {
            //Ajout d'un chart a la list avec generation
            charts.add(genPieChart(q));
        }
        return charts;
    }

}
