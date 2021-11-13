package job.db_project.jobclass;

public class Category {
    private int id;
    private String label;


    public Category(int id, String label) {
        this.id = id;
        this.label = label;
    }

    public Category (Category clone) {
        this.id = clone.getId ();
        this.label = clone.getLabel ();
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
}
