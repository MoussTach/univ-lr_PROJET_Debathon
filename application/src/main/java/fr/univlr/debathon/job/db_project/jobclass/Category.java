package fr.univlr.debathon.job.db_project.jobclass;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Objects;

public class Category {
    private final IntegerProperty id = new SimpleIntegerProperty(-1);
    private final StringProperty label = new SimpleStringProperty("/");
    private final StringProperty color = new SimpleStringProperty("#ffffff");


    public Category(){

    }

    public Category(int id, String label, String color) {
        this.id.set(id);
        this.label.set(label);
        this.color.set(color);
    }

    public Category (Category clone) {
        this.id.set(clone.getId ());
        this.label.set(clone.getLabel ());
        this.color.set(clone.getColor());
    }


    //Getter
    public String getLabel() {
        return this.label.get();
    }

    public int getId() {
        return this.id.get();
    }

    public String getColor () { return this.color.get(); }


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


    public StringProperty colorProperty() {
        return color;
    }

    public void setColor(String color) {
        this.color.set(color);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(id, category.id) && Objects.equals(label, category.label) && Objects.equals(color, category.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, label, color);
    }
}
