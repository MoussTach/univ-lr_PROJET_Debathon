package fr.univlr.debathon.job.db_project.jobclass;

import javafx.beans.property.*;

public class Mcq {
    
    private IntegerProperty id = new SimpleIntegerProperty(-1);
    private StringProperty label = new SimpleStringProperty("/");
    private IntegerProperty nb_votes = new SimpleIntegerProperty(0);

    private ObjectProperty<Question> question = new SimpleObjectProperty<>();
    private ObjectProperty<Room> room = new SimpleObjectProperty<>();


    public Mcq(){
    }

    public Mcq(int id, String label, int nb_votes, Question question, Room room) {
        this.id.set(id);
        this.label.set(label);
        this.nb_votes.set(nb_votes);
        this.question.set(question);
        this.room.set(room);
    }

    public Mcq(int id, String label, Question question, Room room) {
        this(id, label, 0, question, room);
    }

    public Mcq(Mcq clone) {
        this.id.set(clone.getId());
        this.label.set(clone.getLabel());
        this.nb_votes.set(clone.getNb_votes());
        this.question.set(clone.getQuestion());
        this.room.set(clone.getRoom());
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

    public Question getQuestion() {
        return this.question.get();
    }

    public Room getRoom() {
        return this.room.get();
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

    public void setQuestion(Question question) {
        this.question.set(question);
    }

    public void setRoom(Room room) {
        this.room.set(room);
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

    public ObjectProperty<Question> questionProperty() {
        return question;
    }

    public ObjectProperty<Room> roomProperty() {
        return room;
    }
}
