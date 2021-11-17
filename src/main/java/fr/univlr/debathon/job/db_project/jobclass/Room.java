package fr.univlr.debathon.job.db_project.jobclass;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Room {
    
    private IntegerProperty id = new SimpleIntegerProperty(-1);

    private StringProperty label = new SimpleStringProperty("/");
    private StringProperty description = new SimpleStringProperty("/");
    private StringProperty key = new SimpleStringProperty("/");
    private StringProperty mail = new SimpleStringProperty("/");

    private BooleanProperty is_open = new SimpleBooleanProperty(false);

    private ObjectProperty<LocalDate> date_start = new SimpleObjectProperty<>();
    private ObjectProperty<LocalDate> date_end = new SimpleObjectProperty<>();

    private ObjectProperty<Category> category = new SimpleObjectProperty<>();

    private ListProperty<Tag> listTag = new SimpleListProperty<>(FXCollections.observableArrayList());
    private ListProperty<User> listUtilisateurs = new SimpleListProperty<>(FXCollections.observableArrayList());

    private ListProperty<Question> listQuestions = new SimpleListProperty<>(FXCollections.observableArrayList());

    public Room(){

    }
    public Room(int id, String label, String description, String key, String mail,
                boolean is_open, LocalDate date_start, LocalDate date_end,
                Category category, List<Tag> listTag, List<Question> questionList) {
        this.id.set(id);
        this.label.set(label);
        this.description.set(description);
        this.key.set(key);
        this.mail.set(mail);
        this.is_open.set(is_open);
        this.date_start.set(date_start);
        this.date_end.set(date_end);
        this.category.set(category);
        this.listTag.addAll(listTag);
        this.listQuestions.addAll(questionList);
    }

    public Room(int id, String label, String description, String key, String mail,
                boolean is_open, LocalDate date_start, LocalDate date_end,
                Category category, List<Tag> listTag) {
        this.id.set(id);
        this.label.set(label);
        this.description.set(description);
        this.key.set(key);
        this.mail.set(mail);
        this.is_open.set(is_open);
        this.date_start.set(date_start);
        this.date_end.set(date_end);
        this.category.set(category);
        this.listTag.addAll(listTag);
    }


    public Room(Room clone) {
        this.id.set(clone.getId());
        this.label.set(clone.getLabel());
        this.description.set(clone.getDescription());
        this.key.set(clone.getKey());
        this.mail.set(clone.getMail());
        this.is_open.set(clone.getIs_open());
        this.date_start.set(clone.getDate_start());
        this.date_end.set(clone.getDate_end());
        this.category.set(clone.getCategory());
        this.listTag.addAll(clone.getListTag());
    }


    //Getter
    public int getId() {
        return this.id.get();
    }

    public String getLabel() {
        return this.label.get();
    }

    public String getDescription() {
        return this.description.get();
    }

    public String getKey() {
        return this.key.get();
    }

    public String getMail() {
        return this.mail.get();
    }

    public boolean getIs_open() {
        return this.is_open.get();
    }

    public LocalDate getDate_start() {
        return this.date_start.get();
    }

    public LocalDate getDate_end() {
        return this.date_end.get();
    }

    public Category getCategory() {
        return this.category.get();
    }

    public List<Tag> getListTag() {
        return this.listTag.get();
    }

    public List<User> getListUtilisateurs() {
        return this.listUtilisateurs.get();
    }


    //Setter
    public void setId(int id) {
        this.id.set(id);
    }

    public void setLabel(String label) {
        this.label.set(label);
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public void setKey(String key) {
        this.key.set(key);
    }

    public void setMail(String mail) {
        this.mail.set(mail);
    }

    public void setIs_open(boolean is_open) {
        this.is_open.set(is_open);
    }

    public void setDate_start(LocalDate date_start) {
        this.date_start.set(date_start);
    }

    public void setDate_end(LocalDate date_end) {
        this.date_end.set(date_end);
    }

    public void setCategory(Category category) {
        this.category.set(category);
    }

    public void setListTag(List<Tag> listTag) {
        this.listTag.setAll(listTag);
    }

    public void addTag (Tag tag) {
        this.listTag.add(tag);
    }

    public void removeTagByTag (Tag tag) {
        this.listTag.remove(tag);
    }

    public void removeTagById (int id) {
        Tag tag = null;
        for (Tag t : this.listTag) {
            if (t.getId() == id)
                tag = t;
        }
        if (tag != null)
            this.listTag.remove(tag);
    }

    public void setListUtilisateurs(List<User> listUtilisateurs) {
        this.listUtilisateurs.setAll(listUtilisateurs);
    }

    public void addUtilisateur (User utilisateur) {
        this.listUtilisateurs.add(utilisateur);
    }

    public void removeUtilisateurByUtilisateur (User utilisateur) {
        this.listUtilisateurs.remove(utilisateur);
    }

    public void removeUtilisateurById (int id) {
        User utilisateur = null;
        for (User u : listUtilisateurs) {
            if (u.getId() == id)
                utilisateur = u;
        }
        if (utilisateur != null)
            listUtilisateurs.remove(utilisateur);
    }



    //Property
    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty labelProperty() {
        return label;
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public StringProperty keyProperty() {
        return key;
    }

    public StringProperty mailProperty() {
        return mail;
    }

    public boolean isIs_open() {
        return is_open.get();
    }

    public BooleanProperty is_openProperty() {
        return is_open;
    }

    public ObjectProperty<LocalDate> date_startProperty() {
        return date_start;
    }

    public ObjectProperty<LocalDate> date_endProperty() {
        return date_end;
    }

    public ObjectProperty<Category> categoryProperty() {
        return category;
    }

    public ListProperty<Tag> listTagProperty() {
        return listTag;
    }

    public ListProperty<User> listUtilisateursProperty() {
        return listUtilisateurs;
    }




    public ObservableList<Question> getListQuestions() {
        return listQuestions.get();
    }

    public ListProperty<Question> listQuestionsProperty() {
        return listQuestions;
    }

    public void setListQuestions(ObservableList<Question> listQuestions) {
        this.listQuestions.set(listQuestions);
    }

    public void setListQuestions(List<Question> listQuestions) {
        this.listQuestions.addAll(listQuestions);
    }

    public List<Question> getListQuestion () {
        List<Question> questionList = new ArrayList<>();
        for (Question q: this.listQuestions) {
            questionList.add(q);
        }
        return questionList;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", label=" + label +
                ", description=" + description +
                ", key=" + key +
                ", mail=" + mail +
                ", is_open=" + is_open +
                ", date_start=" + date_start +
                ", date_end=" + date_end +
                ", category=" + category +
                ", listTag=" + listTag +
                ", listUtilisateurs=" + listUtilisateurs +
                ", listQuestions=" + listQuestions +
                '}';
    }
}
