package fr.univlr.debathon.job.db_project.jobclass;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User {
    
    private IntegerProperty id = new SimpleIntegerProperty(-1);
    private StringProperty label = new SimpleStringProperty("/");

    public User(){

    }

    public User(int id, String label) {
        this.id.set(id);
        this.label.set(label);
    }

    public User (User clone) {
        this.id.set(clone.getId());
        this.label.set(clone.getLabel());
    }


    //Getter
    public String getLabel() {
        return this.label.get();
    }

    public int getId() {
        return this.id.get();
    }


    //Setter
    public void setId (int id) {
        this.id.set(id);
    }

    public void setLabel (String label) {
        this.label.set(label);
    }


    //Property
    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty labelProperty() {
        return label;
    }
}
