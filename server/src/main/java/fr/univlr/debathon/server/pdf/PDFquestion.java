package fr.univlr.debathon.server.pdf;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PDFquestion {

    private int idQuestion;
    private String labelQuestion;
    private boolean uniqueChoice;
    private Map<String, Float> mapResponse;

    private int most_nb_likes, most_nb_dislikes;
    private String most_like_comment, most_dislike_comment;


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

    public void setMost_nb_likes(int most_nb_likes) {
        this.most_nb_likes = most_nb_likes;
    }

    public void setMost_nb_dislikes(int most_nb_dislikes) {
        this.most_nb_dislikes = most_nb_dislikes;
    }

    public void setMost_like_comment(String most_like_comment) {
        this.most_like_comment = most_like_comment;
    }

    public void setMost_dislike_comment(String most_dislike_comment) {
        this.most_dislike_comment = most_dislike_comment;
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

    public int getMost_nb_likes() {
        return most_nb_likes;
    }

    public int getMost_nb_dislikes() {
        return most_nb_dislikes;
    }

    public String getMost_like_comment() {
        return most_like_comment;
    }

    public String getMost_dislike_comment() {
        return most_dislike_comment;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PDFquestion that = (PDFquestion) o;
        return idQuestion == that.idQuestion && uniqueChoice == that.uniqueChoice && most_nb_likes == that.most_nb_likes && most_nb_dislikes == that.most_nb_dislikes && Objects.equals(labelQuestion, that.labelQuestion) && Objects.equals(mapResponse, that.mapResponse) && Objects.equals(most_like_comment, that.most_like_comment) && Objects.equals(most_dislike_comment, that.most_dislike_comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idQuestion, labelQuestion, uniqueChoice, mapResponse, most_nb_likes, most_nb_dislikes, most_like_comment, most_dislike_comment);
    }


    @Override
    public String toString() {
        return "PDFquestion{" +
                "idQuestion=" + idQuestion +
                ", labelQuestion='" + labelQuestion + '\'' +
                ", uniqueChoice=" + uniqueChoice +
                ", mapResponse=" + mapResponse +
                ", most_nb_likes=" + most_nb_likes +
                ", most_nb_dislikes=" + most_nb_dislikes +
                ", most_like_comment='" + most_like_comment + '\'' +
                ", most_dislike_comment='" + most_dislike_comment + '\'' +
                '}';
    }
}
