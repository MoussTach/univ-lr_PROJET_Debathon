package fr.univlr.debathon.job.db_project.jobclass;

import javafx.beans.property.*;

public class Comment {

    private final IntegerProperty id = new SimpleIntegerProperty(-1);
    private final StringProperty comment = new SimpleStringProperty("");
        
    private final IntegerProperty nb_likes = new SimpleIntegerProperty(0);
    private final IntegerProperty nb_dislikes = new SimpleIntegerProperty(0);

    private final ObjectProperty<Comment> parent = new SimpleObjectProperty<>();
    private final ObjectProperty<Question> question = new SimpleObjectProperty<>();
    private final ObjectProperty<Room> room = new SimpleObjectProperty<>();
    private final ObjectProperty<User> user = new SimpleObjectProperty<>();

    public Comment(){

    }

    public Comment(int id, String comment, int nb_likes, int nb_dislikes, Comment parent, Question question, Room room, User user) {
        this.id.set(id);
        this.comment.set(comment);
        this.nb_likes.set(nb_likes);
        this.nb_dislikes.set(nb_dislikes);
        this.parent.set(parent);
        this.question.set(question);
        this.room.set(room);
        this.user.set(user);
    }

    public Comment(int id, String comment, Question question, Room room, User user) {
        this(id, comment, 0, 0, null, question, room, user);
    }

    public Comment(String comment, int nb_likes, int nb_dislikes, Comment parent, Question question, Room room, User user) {
        this(-1, comment, nb_likes, nb_dislikes, parent, question, room, user);
    }

    public Comment(Comment clone) {
        this.id.set(clone.getId());
        this.comment.set(clone.getComment());
        this.nb_likes.set(clone.getNb_likes());
        this.nb_dislikes.set(clone.getNb_dislikes());
        this.parent.set(clone.getParent());
        this.question.set(clone.getQuestion());
        this.room.set(clone.getRoom());
        this.user.set(clone.getUser());
    }


    //Getter
    public int getId() {
        return this.id.get();
    }

    public String getComment() {
        return this.comment.get();
    }

    public int getNb_likes() {
        return this.nb_likes.get();
    }

    public int getNb_dislikes() {
        return this.nb_dislikes.get();
    }

    public Comment getParent() {
        return this.parent.get();
    }

    public Question getQuestion() {
        return this.question.get();
    }

    public Room getRoom() {
        return this.room.get();
    }

    public User getUser() {
        return this.user.get();
    }


    //Setter
    public void setId(int id) {
        this.id.set(id);
    }

    public void setComment(String comment) {
        this.comment.set(comment);
    }

    public void setNb_likes(int nb_likes) {
        this.nb_likes.set(nb_likes);
    }

    public void setNb_dislikes(int nb_dislikes) {
        this.nb_dislikes.set(nb_dislikes);
    }

    public void setParent(Comment parent) {
        this.parent.set(parent);
    }

    public void setQuestion(Question question) {
        this.question.set(question);
    }

    public void setRoom(Room room) {
        this.room.set(room);
    }

    public void setUser(User user) {
        this.user.set(user);
    }


    //Property
    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty commentProperty() {
        return comment;
    }

    public IntegerProperty nb_likesProperty() {
        return nb_likes;
    }

    public IntegerProperty nb_dislikesProperty() {
        return nb_dislikes;
    }

    public ObjectProperty<Comment> parentProperty() {
        return parent;
    }

    public ObjectProperty<Question> questionProperty() {
        return question;
    }

    public ObjectProperty<Room> roomProperty() {
        return room;
    }

    public ObjectProperty<User> userProperty() {
        return user;
    }
}
