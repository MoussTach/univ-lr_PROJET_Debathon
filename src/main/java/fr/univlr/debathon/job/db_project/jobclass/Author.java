package fr.univlr.debathon.job.db_project.jobclass;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Job class.
 * Client on the database (getted with the Sage database throught a ODBC).
 *
 * @author Gaetan Brenckle
 */
public class Author {

    private final IntegerProperty author_ID = new SimpleIntegerProperty(0);
    private final StringProperty name = new SimpleStringProperty("n/a");

    /**
     * Default Constructor
     *
     * @author Gaetan Brenckle
     */
    public Author() {}

    /**
     * Clone constructor
     *
     * @param clone - {@link Author}
     */
    public Author(Author clone) {

        if (clone != null) {

            this.author_ID.set(clone.author_ID.get());

            this.name.set(clone.name.get());
        }
    }

    /**
     * Getter for the variable author_ID.
     *
     * @return {@link Integer} - return the variable author_ID.
     */
    public Integer getauthor_ID() {
        return author_ID.get();
    }

    /**
     * Getter for the variable name.
     *
     * @return {@link String} - return the variable name.
     */
    public String getname() {
        return name.get();
    }

    /**
     * Setter for the variable author_ID.
     *
     * @param author_ID - {@link Integer} - index of the Author class.
     * @return - {@link Author} - builder pattern return
     */
    public Author setauthor_ID(Integer author_ID) {
        this.author_ID.set(author_ID);
        return this;
    }

    /**
     * Setter for the variable name.
     *
     * @param name - {@link String} - name of this class.
     * @return - {@link Author} - builder pattern return
     */
    public Author setname(String name) {
        this.name.set(name);
        return this;
    }

    /**
     * Property of the variable author_ID.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link IntegerProperty} - return the property of the variable author_ID.
     */
    public IntegerProperty author_IDProperty() {
        return author_ID;
    }

    /**
     * Property of the variable name.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link StringProperty} - return the property of the variable name.
     */
    public StringProperty nameProperty() {
        return name;
    }

    /**
     * ToString methods, created when this class is used on listing
     *
     * @return - {@link String} - the builded list
     */
    @Override
    public String toString() {
        return String.format("%s", author_ID.get());
    }
}
