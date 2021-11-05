package fr.univlr.debathon.job.db_project.dao;

import fr.univlr.debathon.custom.sql.PreparedStatementAware;
import fr.univlr.debathon.job.dao.Dao;
import fr.univlr.debathon.job.db_project.jobclass.Author;
import fr.univlr.debathon.log.generate.CustomLogger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * DAO pattern class.
 * Used for the job class {@link Author}
 *
 * @author Gaetan Brenckle
 */
public class DAO_Author implements Dao<Author> {

    private static final CustomLogger LOGGER = CustomLogger.create(DAO_Author.class.getName());
    private Connection connectionHandle_ = null;

    /**
     * Default constructor.
     * Need to call {@link DAO_Author#setConnection(Connection)} before any other function.
     *
     * @author Gaetan Brenckle
     */
    public DAO_Author() {
    }

    /**
     * Constructor with the Connection argument.
     *
     * @author Gaetan Brenckle
     *
     * @param conn - {@link Connection} - connection used.
     */
    public DAO_Author(Connection conn)  {
        this.connectionHandle_ = conn;
    }

    /**
     * Setter of the connection.
     *
     * @author Gaetan Brenckle
     *
     * @param conn - {@link Connection} - Connection used.
     */
    @Override
    public void setConnection(Connection conn)  {
        this.connectionHandle_ = conn;
    }



    /**
     * SELECT with the index of the associate job class.
     *
     * @author Gaetan Brenckle
     *
     * @param id - {@link Integer} - index of the associate job class. Can handle null.
     * @return - {@link Author} - the job class that can be found with the index
     * @throws SQLException - throw the exception to force a try catch when used.
     */
    public final Author select(Integer id) throws SQLException {
        Author retAuthor = null;

        if (id == null) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn("Author select when the given id is null");
            }
            return null;
        }

        final String select_sql = String.format("%s %s %s",
                String.format("SELECT %s, %s", "author_ID", "name"),
                String.format("FROM %s ", "Author"),
                "WHERE author_ID = ?"
        );

        try (PreparedStatementAware prepSelect = new PreparedStatementAware(connectionHandle_.prepareStatement(select_sql))) {
            prepSelect.setInt(id);

            try (final ResultSet resultSelect = prepSelect.executeQuery()) {
                if (resultSelect.next()) {
                    retAuthor =
                            new Author()
                                    .setauthor_ID(resultSelect.getInt("author_ID"))
                                    .setname(resultSelect.getString("name"));

                }
            }
        }
        return retAuthor;
    }

    /**
     * SELECT with the index of the associate job class.
     *
     * @author Gaetan Brenckle
     *
     * @param map - {@link Map} - index of the associate job class. Can handle null.
     * @return - {@link List} - the job class that can be found with the index
     * @throws SQLException - throw the exception to force a try catch when used.
     */
    public final List<Author> selectByMultiCondition(Map<String, String> map) throws SQLException {
        final List<Author> retAuthors = new ArrayList<>();

        if (map == null) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn("Author select when the given id is null");
            }
            return retAuthors;
        }


        StringBuilder select_sql = new StringBuilder(String.format("%s %s %s",
                String.format("SELECT %s, %s", "author_ID", "name"),
                String.format("FROM %s ", "Author"),
                "WHERE 1=1"
        ));

        for (Map.Entry<String, String> entry : map.entrySet()) {
            select_sql.append(String.format(" %s ", entry.getKey()));
        }


        try (PreparedStatementAware prepSelect = new PreparedStatementAware(connectionHandle_.prepareStatement(select_sql.toString()))) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                prepSelect.setString(entry.getValue());
            }

            try (final ResultSet resultSelect = prepSelect.executeQuery()) {
                while (resultSelect.next()) {
                    Author Author =
                            new Author()
                                    .setauthor_ID(resultSelect.getInt("author_ID"))
                                    .setname(resultSelect.getString("name"));
                    retAuthors.add(Author);
                }
            }
        }
        return retAuthors;
    }

    /**
     * SELECT of all occurance of the Author class.
     *
     * @author Gaetan Brenckle
     *
     * @return - {@link List} - a list that contain all occurance of {@link Author}, the job class associate.
     * @throws SQLException - throw the exception to force a try catch when used.
     */
    @Override
    public List<Author> selectAll() throws SQLException {
        final List<Author> retAuthors = new ArrayList<>();

        String format = String.format("%s %s",
                String.format("SELECT %s, %s", "author_ID", "name"),
                String.format("FROM %s ", "Author"));
        final String selectAll_sql = format;

        final PreparedStatementAware prepSelectAll = new PreparedStatementAware(connectionHandle_.prepareStatement(selectAll_sql));

        try(final ResultSet resultSelectAll = prepSelectAll.executeQuery()) {
            while (resultSelectAll.next()) {
                Author Author =
                        new Author()
                                .setauthor_ID(resultSelectAll.getInt("author_ID"))
                                .setname(resultSelectAll.getString("name"));
                retAuthors.add(Author);
            }
        }

        return retAuthors;
    }

    @Override
    public List<Author> selectAll(List<Author> excludeList) throws SQLException {
        return Collections.emptyList();
    }

    /**
     * INSERT the job class.
     * Cannot use (ODBC table)
     *
     * @author Gaetan Brenckle
     *
     * @param obj - {@link Author} - insert the job class.
     * @return - boolean - the state of the sql insert.
     * @throws SQLException - throw the exception to force a try catch when used.
     */
    @Override
    public boolean insert(Author obj) throws SQLException {
        boolean retBool = true;

        if (obj == null) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn(String.format("%s on parameter to insert is null.", "Author"));
            }
            return false;
        }

        if (obj.getauthor_ID() == null) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn(String.format("The parameter %s on the class is null", "author_ID"));
            }
            retBool = false;
        }

        if (obj.getname() == null || obj.getname().isEmpty()) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn(String.format("The parameter %s on the class is null", "name"));
            }
            retBool = false;
        }

        if (!retBool) {
            return false;
        }

        String insert_sql = String.format("%s %s ",
                String.format("INSERT INTO %s (%s, %s)", "Author", "author_ID", "name"),
                "VALUES (?, ?)");

        try (PreparedStatementAware prepInsert = new PreparedStatementAware(connectionHandle_.prepareStatement(insert_sql))) {
            prepInsert.setInt(obj.getauthor_ID());
            prepInsert.setString(obj.getname());

            retBool = prepInsert.executeUpdate() > 0;

            if (LOGGER.isInfoEnabled() && retBool) {
                String printedSql = String.format("Insert a new %s : [%s]", "Author", prepInsert.printSqlStatement(insert_sql));

                LOGGER.info(printedSql);
            }
        }
        return retBool;
    }

    /**
     * UPDATE the job class.
     * Cannot use (ODBC table)
     *
     * @author Gaetan Brenckle
     *
     * @param obj - {@link Author} - insert the job class.
     * @return - boolean - the state of the sql update.
     * @throws SQLException - throw the exception to force a try catch when used.
     */
    @Override
    public boolean update(Author obj) throws SQLException {
        boolean retBool = true;

        if (obj == null) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn(String.format("%s on parameter to insert is null.", "Author"));
            }
            return false;
        }

        if (obj.getauthor_ID() == null) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn(String.format("The parameter %s on the class is null", "author_ID"));
            }
            retBool = false;
        }

        if (obj.getname() == null || obj.getname().isEmpty()) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn(String.format("The parameter %s on the class is null", "name"));
            }
            retBool = false;
        }

        if (!retBool) {
            return false;
        }

        String update_sql = String.format("%s %s %s %s ",
                String.format("UPDATE %s", "Author"),
                String.format("SET %s = ?", "author_ID"),
                String.format("SET %s = ?", "name"),
                String.format("WHERE %s = ?", "author_ID"));

        try (PreparedStatementAware prepUpdate = new PreparedStatementAware(connectionHandle_.prepareStatement(update_sql))) {
            prepUpdate.setInt(obj.getauthor_ID());
            prepUpdate.setString(obj.getname());

            retBool = prepUpdate.executeUpdate() > 0;

            if (LOGGER.isInfoEnabled() && retBool) {
                String printedSql = String.format("Update a new %s : [%s]", "Author", prepUpdate.printSqlStatement(update_sql));

                LOGGER.info(printedSql);
            }
        }
        return retBool;
    }


    /**
     * DELETE the job class.
     * Cannot use (ODBC table)
     *
     * @author Gaetan Brenckle
     *
     * @param obj - {@link Author} - insert the job class.
     * @return - boolean - the state of the sql delete.
     * @throws SQLException - throw the exception to force a try catch when used.
     */
    @Override
    public boolean delete(Author obj) throws SQLException {
        boolean retBool = true;

        if (obj == null) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn(String.format("%s on parameter to insert is null.", "Author"));
            }
            return false;
        }

        if (obj.getauthor_ID() == null) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn(String.format("The parameter %s on the class is null", "author_ID"));
            }
            retBool = false;
        }

        if (!retBool) {
            return false;
        }

        String delete_sql = String.format("%s %s ",
                String.format("DELETE FROM %s", "Author"),
                String.format("WHERE %s = ?", "author_ID"));

        try (PreparedStatementAware prepDelete = new PreparedStatementAware(connectionHandle_.prepareStatement(delete_sql))) {
            prepDelete.setInt(obj.getauthor_ID());

            retBool = prepDelete.executeUpdate() > 0;

            if (LOGGER.isInfoEnabled() && retBool) {
                String printedSql = String.format("Delete a new %s : [%s]", "Author", prepDelete.printSqlStatement(delete_sql));

                LOGGER.info(printedSql);
            }
        }

        return retBool;
    }
}
