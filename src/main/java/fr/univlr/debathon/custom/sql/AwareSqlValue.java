package fr.univlr.debathon.custom.sql;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Value of the {@link PreparedStatementAware}.
 * used to contain a irregular number of variable, with their designation.
 *
 * @author Gaetan Brenckle
 */
public class AwareSqlValue {
    private final List<Pair<String, String>> valueList;

    /**
     * Default constructor
     *
     * @author Gaetan Brenckle
     */
    public AwareSqlValue(){
        this.valueList  = new ArrayList<>();
    }

    /**
     * Add the pair on the list
     *
     * @param value - {@link Pair} - a pair of two element
     */
    public void addValue(Pair<String, String> value) {
        valueList.add(value);
    }

    /**
     * Getter for the list
     *
     * @author Gaetan Brenckle
     *
     * @return {@link List} - actual list of pair
     */
    public List<Pair<String, String>> getList() {
        return valueList;
    }
}
