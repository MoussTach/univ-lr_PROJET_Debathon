package job.dao;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface DAO<T> {
    
    /**
     * Setter of the connection.
     * 
     * @param conn - {@link Connection} - Connection used.
     */
    void setConnection(Connection conn);

    /**
     * SELECT of all occurance of the class.
     *
     * @return - {@link List} - a list that contain all occurance of {@link T}, the class associate.
     * @throws SQLException - throw the exception to force a try catch when used.
     */
    List<T> selectAll() throws SQLException;

    /**
     * INSERT the class.
     *
     * @param obj - {@link T} - insert the class.
     * @return - boolean - the state of the sql insert.
     * @throws SQLException - throw the exception to force a try catch when used.
     */
    boolean insert(T obj) throws SQLException;

    /**
     * UPDATE the class.
     *
     * @param obj - {@link T} - update the class.
     * @return - boolean - the state of the sql update.
     * @throws SQLException - throw the exception to force a try catch when used.
     */
    boolean update(T obj) throws SQLException;

    /**
     * DELETE the class.
     *
     * @param obj - {@link T} - delete the class.
     * @return - boolean - the state of the sql delete.
     * @throws SQLException - throw the exception to force a try catch when used.
     */
    boolean delete(T obj) throws SQLException;

    /**
     * SELECT with the index of the associate class.
     *
     * @param map - {@link Map} - index of the associate class. Can handle null.
     * @return - {@link List} - the class that can be found with the index
     * @throws SQLException - throw the exception to force a try catch when used.
     */
    List<T> selectByMultiCondition(Map<String, String> map) throws SQLException;


        /**
     * SELECT with the index of the associate class.
     *
     * @param id - {@link int} - index of the associate class. Can handle null.
     * @return - {@link T} - the class that can be found with the index
     * @throws SQLException - throw the exception to force a try catch when used.
     */
    T select (int id) throws SQLException;


}
