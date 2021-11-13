package job.db_project.jobclass;

public class Mcq {
    
    private int id;
    private String label;
    private int nb_votes;

    private Question question;
    private Room room;



    public Mcq(int id, String label, int nb_votes, Question question, Room room) {
        this.id = id;
        this.label = label;
        this.nb_votes = nb_votes;
        this.question = question;
        this.room = room;
    }

    public Mcq(Mcq clone) {
        this.id = clone.getId();
        this.label = clone.getLabel();
        this.nb_votes = clone.getNb_votes();
        this.question = clone.getQuestion();
        this.room = clone.getRoom();
    }

    public Mcq(int id, String label, Question question, Room room) {
        this.id = id;
        this.label = label;
        this.nb_votes = 0;
        this.question = question;
        this.room = room;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getNb_votes() {
        return this.nb_votes;
    }

    public void setNb_votes(int nb_votes) {
        this.nb_votes = nb_votes;
    }

    public Question getQuestion() {
        return this.question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Room getRoom() {
        return this.room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

}
