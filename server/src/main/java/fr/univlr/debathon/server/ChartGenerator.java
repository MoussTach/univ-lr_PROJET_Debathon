package fr.univlr.debathon.server;

import javafx.scene.chart.PieChart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import java.io.*;
import java.util.Map;

public class ChartGenerator {

    public ChartGenerator() {
    }

    public PieDataset getPieDataset(Map<String, Float> data) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (Map.Entry entry : data.entrySet()) {
            dataset.setValue(entry.getKey().toString(), Float.parseFloat(entry.getValue().toString()));
        }
        return dataset;
    }

    public JFreeChart genPieChart(String question_label, Map<String, Float> data) {
        PieDataset dataset = getPieDataset(data);
        JFreeChart chartPie = ChartFactory.createPieChart(
                question_label,   // chart title
                dataset,          // data
                true,             // include legend
                true,
                false);
        return chartPie;
    }

    public void savePieAsPng(String label, Map<String, Float> data){
        JFreeChart pie = genPieChart(label,data);
        File out = null;
        try {
            out = new File("testPng.png");
            ChartUtilities.saveChartAsPNG(out,
                    pie,
                    1000,
                    600);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
/*
    public JFreeChart genChartBar(Map<String,Float> data){
        JFreeChart barChart = ChartFactory.createBarChart(
                "Oui / Non",
                "",
                "pourcentage votant",
                getBarDataset(data),
                PlotOrientation.VERTICAL,
                false,true,false
        );
        return barChart;
    }

 */
/*
    public void saveAsPng(){
        JFreeChart bar = genChartBar("test");
        File out = null;
        try {
            out = new File("testPng.png");
            ChartUtilities.saveChartAsPNG(out,
                    bar,
                    1000,
                    600);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }*/
}
