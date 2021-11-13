package fr.univlr.debathon.job.db_project.dao;

import fr.univlr.debathon.job.dao.DAO;
import fr.univlr.debathon.job.db_project.jobclass.Tag;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TagDAO implements DAO<Tag> {

    private Connection connection = null;


    public TagDAO(Connection connection) {
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
     * @return - {@link List} - a list that contain all occurance of {@link Tag}, the class associate.
     * @throws SQLException - throw the exception to force a try catch when used.
     */
    public List<Tag> selectAll() throws SQLException {
        List<Tag> tagList = new ArrayList<>();

        String sql = "SELECT idTag, label, color FROM Tag";

        try {
            PreparedStatement pstmt  = connection.prepareStatement(sql);
            
            ResultSet rs  = pstmt.executeQuery();
            
            while (rs.next()) {
                tagList.add(new Tag(rs.getInt("idTag"), rs.getString("label"), rs.getString("color")));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return tagList;    
    }

    /**
     * INSERT the class.
     *
     * @param tag - {@link Tag} - insert the class.
     * @return - boolean - the state of the sql insert.
     * @throws SQLException - throw the exception to force a try catch when used.
     */
    public boolean insert(Tag tag) throws SQLException {
        String sql = "INSERT INTO Tag (idTag, label, color) VALUES(?,?,?)";

        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, tag.getId());
            pstmt.setString(2, tag.getLabel());
            pstmt.setString(3, tag.getColor());
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
     * @param tag - {@link Tag} - update the class.
     * @return - boolean - the state of the sql update.
     * @throws SQLException - throw the exception to force a try catch when used.
     */
    public boolean update(Tag tag) throws SQLException {
    	
        String sql = "UPDATE Tag SET label = ?, color = ? WHERE id = ?";
        
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(3, tag.getId());
            pstmt.setString(1, tag.getLabel());
            pstmt.setString(2, tag.getColor());
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
     * @param tag - {@link Tag} - delete the class.
     * @return - boolean - the state of the sql delete.
     * @throws SQLException - throw the exception to force a try catch when used.
     */
    public boolean delete(Tag tag) throws SQLException {
        String sql = "DELETE Tag WHERE id = ?";
        
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, tag.getId());
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
    public List<Tag> selectByMultiCondition(Map<String, String> map) throws SQLException {
        return null;
    }

    /**
     * SELECT with the index of the associate class.
     *
     * @param id - {@link int} - index of the associate class. Can handle null.
     * @return - {@link List} - the class that can be found with the index
     * @throws SQLException - throw the exception to force a try catch when used.
     */
    public List<Tag> selectByIdRoom(int id) throws SQLException {
        
        String sqlIdTag = "SELECT id_tag FROM Relation_room_tag WHERE id_room = ?";

        List<Integer> listIdTag = new ArrayList<>();
        try {
            PreparedStatement pstmt  = connection.prepareStatement(sqlIdTag);
            pstmt.setInt(1, id);

            ResultSet rs  = pstmt.executeQuery();

            while (rs.next()) {
                listIdTag.add(rs.getInt("id_tag"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        
        List<Tag> tagList = new ArrayList<>();

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < listIdTag.size(); i++) {
            builder.append(listIdTag.get(i) + ",");
        }

        String sql = "SELECT idTag, label, color FROM Tag WHERE idTag in ("  +builder.deleteCharAt( builder.length() -1).toString()+ ")";

        try {
            PreparedStatement pstmt  = connection.prepareStatement(sql);

            ResultSet rs  = pstmt.executeQuery();

            while (rs.next()) {
                tagList.add(new Tag(rs.getInt("idTag"), rs.getString("label"), rs.getString("color")));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return tagList;    
    }


        /**
     * SELECT with the index of the associate class.
     *
     * @param id - {@link int} - index of the associate class. Can handle null.
     * @return - {@link Tag} - the class that can be found with the index
     * @throws SQLException - throw the exception to force a try catch when used.
     */
    public Tag select (int id) throws SQLException {
        Tag tag = null;
        String sql = "SELECT idTag, label, color FROM Tag WHERE idTag = ?";

        try {
             PreparedStatement pstmt  = connection.prepareStatement(sql);
            
            pstmt.setInt(1, id);

            ResultSet rs  = pstmt.executeQuery();
            
            if (rs.next()) {
                tag = new Tag (rs.getInt("idTag"), rs.getString("label"), rs.getString("color"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return tag;
    }

}
