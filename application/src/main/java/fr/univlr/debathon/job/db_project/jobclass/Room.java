package fr.univlr.debathon.job.db_project.jobclass;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Room {
    
    private final IntegerProperty id = new SimpleIntegerProperty(-1);

    private final StringProperty label = new SimpleStringProperty("/");
    private final StringProperty description = new SimpleStringProperty("/");
    private final StringProperty key = new SimpleStringProperty("/");

    private final BooleanProperty is_open = new SimpleBooleanProperty(false);

    private final ObjectProperty<LocalDate> date_start = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDate> date_end = new SimpleObjectProperty<>();

    private final ObjectProperty<Category> category = new SimpleObjectProperty<>();

    private final ListProperty<Tag> listTag = new SimpleListProperty<>(FXCollections.synchronizedObservableList(FXCollections.observableArrayList()));
    private final ListProperty<User> listUtilisateurs = new SimpleListProperty<>(FXCollections.synchronizedObservableList(FXCollections.observableArrayList()));

    private final ListProperty<Question> listQuestions = new SimpleListProperty<>(FXCollections.synchronizedObservableList(FXCollections.observableArrayList()));

    public Room(){

    }
    public Room(int id, String label, String description, String key,
                boolean is_open, LocalDate date_start, LocalDate date_end,
                Category category, List<Tag> listTag, List<Question> questionList) {
        this.id.set(id);
        this.label.set(label);
        this.description.set(description);
        this.key.set(key);
        this.is_open.set(is_open);
        this.date_start.set(date_start);
        this.date_end.set(date_end);
        this.category.set(category);
        this.listTag.addAll(listTag);
        this.listQuestions.addAll(questionList);
    }

    public Room(String label, String description, String key,
                Category category, List<Tag> listTag) {
        this.id.set(1);
        this.label.set(label);
        this.description.set(description);
        this.key.set(key);
        this.is_open.set(true);
        this.date_start.set(null);
        this.date_end.set(null);
        this.category.set(category);
        this.listTag.addAll(listTag);
    }


    public Room(Room clone) {
        this.id.set(clone.getId());
        this.label.set(clone.getLabel());
        this.description.set(clone.getDescription());
        this.key.set(clone.getKey());
        this.is_open.set(clone.getIs_open());
        this.date_start.set(clone.getDate_start());
        this.date_end.set(clone.getDate_end());
        this.category.set(clone.getCategory());
        this.listTag.addAll(clone.getListTag());
        this.listQuestions.addAll(clone.getListQuestion());
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

    public List<Question> getListQuestion () {
        return new ArrayList<>(this.listQuestions);
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

    public void setListQuestions(ObservableList<Question> listQuestions) {
        this.listQuestions.set(listQuestions);
    }

    public void setListQuestions(List<Question> listQuestions) {
        this.listQuestions.addAll(listQuestions);
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

    public ListProperty<Question> listQuestionsProperty() {
        return listQuestions;
    }


    public void addQuestion (Question question) {
        this.listQuestions.add(question);
    }


}
