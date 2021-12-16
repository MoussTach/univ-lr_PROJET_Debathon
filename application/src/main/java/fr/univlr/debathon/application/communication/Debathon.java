package fr.univlr.debathon.application.communication;

import fr.univlr.debathon.job.db_project.jobclass.*;
import fr.univlr.debathon.log.generate.CustomLogger;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Debathon {

    private static final CustomLogger LOGGER = CustomLogger.create(Debathon.class.getName());

    private static Debathon debathon = null;
    private final StringProperty key = new SimpleStringProperty();
    private final ObjectProperty<User> user = new SimpleObjectProperty(new User(6, "cheval gris"));

    private AppCommunication appCommunication = null;

    private final ListProperty<Category> categories = new SimpleListProperty<>(FXCollections.synchronizedObservableList(FXCollections.synchronizedObservableList(FXCollections.observableArrayList())));
    private final ListProperty<Tag> tags = new SimpleListProperty<>(FXCollections.synchronizedObservableList(FXCollections.synchronizedObservableList(FXCollections.observableArrayList())));

    private final ListProperty<Mcq> mcq = new SimpleListProperty<>(FXCollections.synchronizedObservableList(FXCollections.observableArrayList()));
    private final ListProperty<Room> debates = new SimpleListProperty<>(FXCollections.synchronizedObservableList(FXCollections.observableArrayList()));

    private Room current_debate = null;


    private Debathon() {
        try {
            this.appCommunication = new AppCommunication();
            this.appCommunication.start();
            this.appCommunication.requestHome();
        } catch (Exception e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error(String.format("Error when trying to run Debathon: %s", e.getMessage()), e);
            }
        }
    }


    /**
     * Pattern singleton
     *
     * @author Gaetan Brenckle
     *
     * @return {@link Debathon} - return class
     */
    public static Debathon getInstance() {
        if (debathon == null)
            debathon = new Debathon();
        return debathon;
    }

    /**
     * Getter for the variable key.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link String} - return the variable key.
     */
    public String getKey() {
        return key.get();
    }

    /**
     * Getter for the variable user.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link User} - return the variable user.
     */
    public User getUser() {
        return user.get();
    }

    /**
     * Getter for the variable appCommunication.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link AppCommunication} - return the variable appCommunication.
     */
    public AppCommunication getAppCommunication () {
        return this.appCommunication;
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
     * Getter for the variable mcq.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link ObservableList} - return the variable mcq.
     */
    public ObservableList<Mcq> getMcq() {
        return mcq.get();
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
     * Getter for the variable current_debate.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link Room} - return the variable current_debate.
     */
    public Room getCurrent_debate() {
        return current_debate;
    }


    /**
     * Setter for the variable key.
     *
     * @param key - {@link String} - key of the communication.
     */
    public void setKey(String key) {
        this.key.set(key);
    }

    /**
     * Setter for the variable mcq.
     *
     * @param mcq - {@link ObservableList} - mcq of the communication.
     */
    public void setMcq(ObservableList<Mcq> mcq) {
        this.mcq.set(mcq);
    }

    /**
     * Setter for the variable current_debate.
     *
     * @param room - {@link Room} - current_debate of the communication.
     */
    public void setCurrent_debate (Room room) {
        this.current_debate = room;
    }


    /**
     * Property of the variable key.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link StringProperty} - return the property of the variable key.
     */
    public StringProperty keyProperty() {
        return key;
    }

    /**
     * Property of the variable user.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link ObjectProperty} - return the property of the variable user.
     */
    public ObjectProperty<User> userProperty() {
        return user;
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
     * Property of the variable mcq.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link ListProperty} - return the property of the variable mcq.
     */
    public ListProperty<Mcq> mcqProperty() {
        return mcq;
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
