package fr.univlr.debathon.job.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Interface for the pattern DAO.
 *
 * @author Gaetan Brenckle
 *
 * @param <T> - the associate job class of the class that implement this interface.
 */
public interface Dao<T> {

    /**
     * Setter of the connection.
     *
     * @author Gaetan Brenckle
     *
     * @param conn - {@link Connection} - Connection used.
     */
    void setConnection(Connection conn);

    /**
     * SELECT of all occurance of the job class.
     *
     * @author Gaetan Brenckle
     *
     * @return - {@link List} - a list that contain all occurance of {@link T}, the job class associate.
     * @throws SQLException - throw the exception to force a try catch when used.
     */
    List<T> selectAll() throws SQLException;

    /**
     * SELECT of all occurance of the job class.
     * Use a list of key to exclude them from the select
     *
     * @author Gaetan Brenckle
     *
     * @param excludeList - {@link List} - list of key
     * @return - {@link List} - a list that contain all occurance of {@link T}, the job class associate.
     * @throws SQLException - throw the exception to force a try catch when used.
     */
    List<T> selectAll(List<T> excludeList) throws SQLException;

    /**
     * INSERT the job class.
     *
     * @author Gaetan Brenckle
     *
     * @param obj - {@link T} - insert the job class.
     * @return - boolean - the state of the sql insert.
     * @throws SQLException - throw the exception to force a try catch when used.
     */
    boolean insert(T obj) throws SQLException;

    /**
     * UPDATE the job class.
     *
     * @author Gaetan Brenckle
     *
     * @param obj - {@link T} - update the job class.
     * @return - boolean - the state of the sql update.
     * @throws SQLException - throw the exception to force a try catch when used.
     */
    boolean update(T obj) throws SQLException;

    /**
     * DELETE the job class.
     *
     * @author Gaetan Brenckle
     *
     * @param obj - {@link T} - delete the job class.
     * @return - boolean - the state of the sql delete.
     * @throws SQLException - throw the exception to force a try catch when used.
     */
    boolean delete(T obj) throws SQLException;
}
