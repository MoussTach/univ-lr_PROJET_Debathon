package job.db_project.jobclass;

public class Comment {

    private int id;
    private String comment;
        
    private int nb_likes;
    private int nb_dislikes;

    private Comment parent;
    private Question question;
    private Room room;
    private User user;

    public Comment(String comment, int nb_likes, int nb_dislikes, Comment parent, Question question, Room room, User user) {
        this.comment = comment;
        this.nb_likes = nb_likes;
        this.nb_dislikes = nb_dislikes;
        this.parent = parent;
        this.question = question;
        this.room = room;
        this.user = user;
    }


    public Comment(int id, String comment, int nb_likes, int nb_dislikes, Comment parent, Question question, Room room, User user) {
        this.id = id;
        this.comment = comment;
        this.nb_likes = nb_likes;
        this.nb_dislikes = nb_dislikes;
        this.parent = parent;
        this.question = question;
        this.room = room;
        this.user = user;
    }

    public Comment(int id, String comment, Question question, Room room, User user) {
        this.id = id;
        this.comment = comment;
        this.nb_likes = 0;
        this.nb_dislikes = 0;
        this.parent = null;
        this.question = question;
        this.room = room;
        this.user = user;
    }

    public Comment(Comment clone) {
        this.id = clone.getId();
        this.comment = clone.getComment();
        this.nb_likes = clone.getNb_likes();
        this.nb_dislikes = clone.getNb_dislikes();
        this.parent = clone.getParent();
        this.question = clone.getQuestion();
        this.room = clone.getRoom();
        this.user = clone.getUser();
    }


    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getNb_likes() {
        return this.nb_likes;
    }

    public void setNb_likes(int nb_likes) {
        this.nb_likes = nb_likes;
    }

    public int getNb_dislikes() {
        return this.nb_dislikes;
    }

    public void setNb_dislikes(int nb_dislikes) {
        this.nb_dislikes = nb_dislikes;
    }

    public Comment getParent() {
        return this.parent;
    }

    public void setParent(Comment parent) {
        this.parent = parent;
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

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
