package fr.univlr.debathon.application.communication;

import fr.univlr.debathon.application.viewmodel.mainwindow.HomePageViewModel;
import fr.univlr.debathon.job.db_project.jobclass.Category;
import fr.univlr.debathon.job.db_project.jobclass.Mcq;
import fr.univlr.debathon.job.db_project.jobclass.Room;
import fr.univlr.debathon.job.db_project.jobclass.Tag;
import fr.univlr.debathon.log.generate.CustomLogger;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;

public class Debathon {

    private static final CustomLogger LOGGER = CustomLogger.create(Debathon.class.getName());

    private static Debathon debathon = null;

    private final ListProperty<Category> categories = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final ListProperty<Tag> tags = new SimpleListProperty<>(FXCollections.observableArrayList());


    private final ListProperty<Mcq> mcq = new SimpleListProperty<>(FXCollections.observableArrayList());

    private final ListProperty<Room> debates = new SimpleListProperty<>(FXCollections.observableArrayList());

    private Room current_debate = null;



    private AppCommunication appCommunication = null;

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


    public Room getCurrent_debate() {
        return current_debate;
    }

    public void setCurrent_debate (Room room) {
        this.current_debate = room;
    }


    public ObservableList<Mcq> getMcq() {
        return mcq.get();
    }

    public ListProperty<Mcq> mcqProperty() {
        return mcq;
    }

    public void setMcq(ObservableList<Mcq> mcq) {
        this.mcq.set(mcq);
    }

    public AppCommunication getAppCommunication () {
        return this.appCommunication;
    }
}
