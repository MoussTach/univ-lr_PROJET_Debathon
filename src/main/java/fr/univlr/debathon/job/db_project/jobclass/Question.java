package fr.univlr.debathon.job.db_project.jobclass;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class Question {
    
    private IntegerProperty id = new SimpleIntegerProperty(-1);

    private StringProperty label = new SimpleStringProperty("/");
    private StringProperty context = new SimpleStringProperty("/");
    private StringProperty type = new SimpleStringProperty("/");

    private BooleanProperty is_active = new SimpleBooleanProperty(false);

    private ObjectProperty<Room> room = new SimpleObjectProperty<>();

    private ListProperty<Comment> listComment = new SimpleListProperty<>(FXCollections.observableArrayList());
    private ObjectProperty<User> user = new SimpleObjectProperty<>();

    public Question(){

    }

    public Question(int id, String label, String context, String type, boolean is_active, Room room, List<Comment> listComment, User user) {
        this.id.set(id);
        this.label.set(label);
        this.context.set(context);
        this.type.set(type);
        this.is_active.set(is_active);
        this.room.set(room);
        this.listComment.addAll(listComment);
        this.user.set(user);
    }

    public Question(Question clone) {
        this.id.set(clone.getId());
        this.label.set(clone.getLabel());
        this.context.set(clone.getContext());
        this.type.set(clone.getType());
        this.is_active.set(clone.is_active());
        this.room.set(clone.getRoom());
        this.listComment.addAll(clone.getListComment());
        this.user.set(clone.getUser());
    }


    //Getter
    public int getId() {
        return this.id.get();
    }

    public String getContext() {
        return this.context.get();
    }

    public String getType () {
        return this.type.get();
    }

    public boolean is_active() {
        return this.is_active.get();
    }

    public Room getRoom() {
        return this.room.get();
    }

    public List<Comment> getListComment() {
        return this.listComment;
    }

    public User getUser() {
        return this.user.get();
    }

    public String getLabel() {
        return this.label.get();
    }


    //Setter
    public void setId(int id) {
        this.id.set(id);
    }

    public void setLabel(String label) {
        this.label.set(label);
    }

    public void setContext(String context) {
        this.context.set(context);
    }

    public void setType (String type) {
        this.type.set(type);
    }

    public void setIs_active(boolean is_active) {
        this.is_active.set(is_active);
    }

    public void setRoom(Room room) {
        this.room.set(room);
    }

    public void setListComment(ObservableList<Comment> listComment) {
        this.listComment.set(listComment);
    }

    public void setUser(User user) {
        this.user.set(user);
    }


    //Property
    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty labelProperty() {
        return label;
    }

    public StringProperty contextProperty() {
        return context;
    }

    public StringProperty typeProperty() {
        return type;
    }

    public boolean isIs_active() {
        return is_active.get();
    }

    public BooleanProperty is_activeProperty() {
        return is_active;
    }

    public ObjectProperty<Room> roomProperty() {
        return room;
    }

    public ListProperty<Comment> listCommentProperty() {
        return listComment;
    }

    public ObjectProperty<User> userProperty() {
        return user;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", label=" + label +
                ", context=" + context +
                ", type=" + type +
                ", is_active=" + is_active +
                ", room=" + room +
                ", listComment=" + listComment +
                ", user=" + user +
                '}';
    }
}
