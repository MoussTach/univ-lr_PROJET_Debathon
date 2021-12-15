package fr.univlr.debathon.job.db_project.jobclass;

import javafx.beans.property.*;

public class Mcq {
    
    private final IntegerProperty id = new SimpleIntegerProperty(-1);
    private final StringProperty label = new SimpleStringProperty("/");
    private final IntegerProperty nb_votes = new SimpleIntegerProperty(0);

    private int id_question = 0;
    private int id_room;


    public Mcq(){
    }

    public Mcq(int id, String label, int nb_votes, int id_question, int id_room) {
        this.id.set(id);
        this.label.set(label);
        this.nb_votes.set(nb_votes);
        this.id_question = id_question;
        this.id_room = id_room;
    }

    public Mcq(String label, int id_question, Room room) {
        this.id.set(-1);
        this.label.set(label);
        this.nb_votes.set(0);
        this.id_question = id_question;
        this.id_room = room.getId();
    }

    public Mcq(int id, String label, int id_question, Room room) {
        this(id, label, 0,id_question, room.getId());
    }

    public Mcq(Mcq clone) {
        this.id.set(clone.getId());
        this.label.set(clone.getLabel());
        this.nb_votes.set(clone.getNb_votes());
        this.id_question = clone.getId_question();
        this.id_room = clone.getId_room();
    }


    //Getter
    public int getId() {
        return this.id.get();
    }

    public String getLabel() {
        return this.label.get();
    }

    public int getNb_votes() {
        return this.nb_votes.get();
    }


    //Setter
    public void setId(int id) {
        this.id.set(id);
    }

    public void setLabel(String label) {
        this.label.set(label);
    }

    public void setNb_votes(int nb_votes) {
        this.nb_votes.set(nb_votes);
    }



    //Property
    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty labelProperty() {
        return label;
    }

    public IntegerProperty nb_votesProperty() {
        return nb_votes;
    }


    public int getId_question() {
        return id_question;
    }

    public void setId_question(int id_question) {
        this.id_question = id_question;
    }

    public int getId_room() {
        return id_room;
    }

    public void setId_room(int id_room) {
        this.id_room = id_room;
    }

    @Override
    public String toString() {
        return "Mcq{" +
                "id=" + id +
                ", label=" + label +
                ", nb_votes=" + nb_votes +
                ", id_question=" + id_question +
                ", id_room=" + id_room +
                '}';
    }
}
