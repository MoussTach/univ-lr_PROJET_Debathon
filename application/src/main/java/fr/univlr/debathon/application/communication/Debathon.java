package fr.univlr.debathon.application.communication;

import fr.univlr.debathon.job.db_project.jobclass.Category;
import fr.univlr.debathon.job.db_project.jobclass.Room;
import fr.univlr.debathon.job.db_project.jobclass.Tag;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Debathon {

    private static Debathon debathon = null;

    private final ListProperty<Category> categories = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final ListProperty<Tag> tags = new SimpleListProperty<>(FXCollections.observableArrayList());

    private final ListProperty<Room> debates = new SimpleListProperty<>(FXCollections.observableArrayList());

    private Debathon() {

        //TODO suppr
        Room debate = new Room();
        debate.setLabel("Name");

        Tag tag = new Tag();
        tag.setLabel("tag");
        debate.addTag(tag);

        tags.add(tag);
        debates.add(debate);
    }


    /**
     * Pattern singleton
     *
     * @Author Gaetan Brenckle
     *
     * @return {@link Debathon} - return class
     */
    public static Debathon getInstance() {
        if (debathon == null)
            debathon = new Debathon();
        return debathon;
    }


    /**
     * Getter for the variable categories.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link ObservableList} - return the variable categories.
     */
    public ObservableList<Category> getCategories() {
        return categories.get();
    }

    /**
     * Getter for the variable tags.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link ObservableList} - return the variable tags.
     */
    public ObservableList<Tag> getTags() {
        return tags.get();
    }

    /**
     * Getter for the variable debates.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link ObservableList} - return the variable debates.
     */
    public ObservableList<Room> getDebates() {
        return debates.get();
    }


    /**
     * Property of the variable categories.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link ListProperty} - return the property of the variable categories.
     */
    public ListProperty<Category> categoriesProperty() {
        return categories;
    }

    /**
     * Property of the variable tags.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link ListProperty} - return the property of the variable tags.
     */
    public ListProperty<Tag> tagsProperty() {
        return tags;
    }

    /**
     * Property of the variable debates.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link ListProperty} - return the property of the variable debates.
     */
    public ListProperty<Room> debatesProperty() {
        return debates;
    }
}
