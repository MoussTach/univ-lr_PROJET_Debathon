package fr.univlr.debathon.job.db_project.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fr.univlr.debathon.job.dao.DAO;
import fr.univlr.debathon.job.db_project.jobclass.Category;


public class CategoryDAO implements DAO<Category> {

    private Connection connection = null;


    public CategoryDAO(Connection connection) {
        this.connection = connection;
    }


    /**
     * Setter of the connection.
     * 
     * @param conn - {@link Connection} - Connection used.
     */
    public void setConnection(Connection conn) {
        this.connection = conn;
    }

    /**
     * SELECT of all occurance of the class.
     *
     * @return - {@link List} - a list that contain all occurance of {@link T}, the class associate.
     * @throws SQLException - throw the exception to force a try catch when used.
     */
    public List<Category> selectAll() throws SQLException {
        List<Category> categoryList = new ArrayList<>();

        String sql = "SELECT idCategory, label FROM Category";

        try {
            PreparedStatement pstmt  = connection.prepareStatement(sql);
            
            ResultSet rs  = pstmt.executeQuery();
            
            while (rs.next()) {
                categoryList.add(new Category(rs.getInt("idCategory"), rs.getString("label")));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return categoryList;    
    }

    /**
     * INSERT the class.
     *
     * @param obj - {@link T} - insert the class.
     * @return - boolean - the state of the sql insert.
     * @throws SQLException - throw the exception to force a try catch when used.
     */
    public boolean insert(Category category) throws SQLException {
        String sql = "INSERT INTO Category (idCategory, label) VALUES(?,?)";

        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, category.getId());
            pstmt.setString(2, category.getLabel());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }    
        
        return true;
        
    }

    /**
     * UPDATE the class.
     *
     * @param obj - {@link T} - update the class.
     * @return - boolean - the state of the sql update.
     * @throws SQLException - throw the exception to force a try catch when used.
     */
    public boolean update(Category category) throws SQLException {
        String sql = "UPDATE Category SET label = ? WHERE idCategory = ?";
        
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(2, category.getId());
            pstmt.setString(1, category.getLabel());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }    
        
        return true;
    }

    /**
     * DELETE the class.
     *
     * @param obj - {@link T} - delete the class.
     * @return - boolean - the state of the sql delete.
     * @throws SQLException - throw the exception to force a try catch when used.
     */
    public boolean delete(Category category) throws SQLException {
        String sql = "DELETE Category WHERE idCategory = ?";
        
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, category.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }    
        
        return true;
    }

    /**
     * SELECT with the index of the associate class.
     *
     * @param map - {@link Map} - index of the associate class. Can handle null.
     * @return - {@link List} - the class that can be found with the index
     * @throws SQLException - throw the exception to force a try catch when used.
     */
    public List<Category> selectByMultiCondition(Map<String, String> map) throws SQLException {
        return null;
    }



        /**
     * SELECT with the index of the associate class.
     *
     * @param id - {@link int} - index of the associate class. Can handle null.
     * @return - {@link T} - the class that can be found with the index
     * @throws SQLException - throw the exception to force a try catch when used.
     */
    public Category select (int id) throws SQLException {
        Category category = null;
        
        String sql = "SELECT idCategory, label FROM Category WHERE idCategory = ?";

        try {
             PreparedStatement pstmt  = connection.prepareStatement(sql);
            
            pstmt.setInt(1, id);

            ResultSet rs  = pstmt.executeQuery();
            
            if (rs.next()) {
                category = new Category (rs.getInt("idCategory"), rs.getString("label"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return category;
    }

}
