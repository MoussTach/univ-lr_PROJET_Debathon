package job.db_project.jobclass;

import java.util.Date;
import java.util.List;

public class Room {
    
    private int id;

    private String label;
    private String description;
    private String key;
    private String mail;

    private boolean is_open;

    private Date date_start;
    private Date date_end;

    private Category category;

    private List<Tag> listTag;
    private List<User> listUtilisateurs;


    public Room(int id, String label, String description, String key, String mail, boolean is_open, Date date_start, Date date_end, Category category, List<Tag> listTag) {
        this.id = id;
        this.label = label;
        this.description = description;
        this.key = key;
        this.mail = mail;
        this.is_open = is_open;
        this.date_start = date_start;
        this.date_end = date_end;
        this.category = category;
        this.listTag = listTag;
    }


    public Room(Room clone) {
        this.id = clone.getId ();
        this.label = clone.getLabel ();
        this.description = clone.getDescription ();
        this.key = clone.getKey ();
        this.mail = clone.getMail ();
        this.is_open = clone.getIs_open ();
        this.date_start = clone.getDate_start ();
        this.date_end = clone.getDate_end ();
        this.category = clone.getCategory ();
        this.listTag = clone.getListTag ();
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

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMail() {
        return this.mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }


    public boolean getIs_open() {
        return this.is_open;
    }

    public void setIs_open(boolean is_open) {
        this.is_open = is_open;
    }

    public Date getDate_start() {
        return this.date_start;
    }

    public void setDate_start(Date date_start) {
        this.date_start = date_start;
    }

    public Date getDate_end() {
        return this.date_end;
    }

    public void setDate_end(Date date_end) {
        this.date_end = date_end;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Tag> getListTag() {
        return this.listTag;
    }

    public void setListTag(List<Tag> listTag) {
        this.listTag = listTag;
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

    public List<User> getListUtilisateurs() {
        return this.listUtilisateurs;
    }

    public void setListUtilisateurs(List<User> listUtilisateurs) {
        this.listUtilisateurs = listUtilisateurs;
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

}
