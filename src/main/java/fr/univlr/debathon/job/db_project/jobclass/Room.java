package fr.univlr.debathon.job.db_project.jobclass;

import javafx.beans.property.*;
import javafx.collections.FXCollections;

import java.util.Date;
import java.util.List;

public class Room {
    
    private IntegerProperty id = new SimpleIntegerProperty(-1);

    private StringProperty label = new SimpleStringProperty("/");
    private StringProperty description = new SimpleStringProperty("/");
    private StringProperty key = new SimpleStringProperty("/");
    private StringProperty mail = new SimpleStringProperty("/");

    private BooleanProperty is_open = new SimpleBooleanProperty(false);

    private ObjectProperty<Date> date_start = new SimpleObjectProperty<>();
    private ObjectProperty<Date> date_end = new SimpleObjectProperty<>();

    private ObjectProperty<Category> category = new SimpleObjectProperty<>();

    private ListProperty<Tag> listTag = new SimpleListProperty<>(FXCollections.observableArrayList());
    private ListProperty<User> listUtilisateurs = new SimpleListProperty<>(FXCollections.observableArrayList());

    public Room(){

    }
    public Room(int id, String label, String description, String key, String mail, boolean is_open, Date date_start, Date date_end, Category category, List<Tag> listTag) {
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

    public Date getDate_start() {
        return this.date_start.get();
    }

    public Date getDate_end() {
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

    public void setDate_start(Date date_start) {
        this.date_start.set(date_start);
    }

    public void setDate_end(Date date_end) {
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

    public ObjectProperty<Date> date_startProperty() {
        return date_start;
    }

    public ObjectProperty<Date> date_endProperty() {
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
}
