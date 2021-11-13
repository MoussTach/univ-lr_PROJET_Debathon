package fr.univlr.debathon.job.db_project.jobclass;

public class Tag {
    
    private int id;
    private String label;
    private String color;

    public Tag(){

    }
    public Tag(int id, String label, String color) {
        this.id = id;
        this.label = label;
        this.color = color;
    }


    public Tag (Tag clone) {
        this.id = clone.getId ();
        this.label = clone.getLabel ();
        this.color = clone.getColor ();
    }

    public String getLabel() {
        return this.label;
    }

    public int getId() {
        return this.id;
    }

    public void setId (int id) {
        this.id = id;
    }

    public void setLabel (String label) {
        this.label = label;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }

}
