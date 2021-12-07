package fr.univlr.debathon.server;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PDFquestion {

    private int idQuestion;
    private String labelQuestion;
    private boolean uniqueChoice;
    private Map<String, Float> mapResponse;

    public PDFquestion (int id) {
        this.idQuestion = id;
        this.mapResponse = new HashMap<>();
    }

    public void setIdQuestion(int idQuestion) {
        this.idQuestion = idQuestion;
    }

    public void setLabelQuestion(String labelQuestion) {
        this.labelQuestion = labelQuestion;
    }

    public void setUniqueChoice(boolean uniqueChoice) {
        this.uniqueChoice = uniqueChoice;
    }

    public void setMapResponse(Map<String, Float> mapResponse) {
        this.mapResponse = mapResponse;
    }


    public int getIdQuestion() {
        return idQuestion;
    }

    public String getLabelQuestion() {
        return labelQuestion;
    }

    public boolean isUniqueChoice() {
        return uniqueChoice;
    }

    public Map<String, Float> getMapResponse() {
        return mapResponse;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PDFquestion that = (PDFquestion) o;
        return idQuestion == that.idQuestion && uniqueChoice == that.uniqueChoice && Objects.equals(labelQuestion, that.labelQuestion) && Objects.equals(mapResponse, that.mapResponse);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idQuestion, labelQuestion, uniqueChoice, mapResponse);
    }

    @Override
    public String toString() {
        return "PDFquestion{" +
                "idQuestion=" + idQuestion +
                ", labelQuestion='" + labelQuestion + '\'' +
                ", uniqueChoice=" + uniqueChoice +
                ", mapResponse=" + mapResponse +
                '}';
    }
}
