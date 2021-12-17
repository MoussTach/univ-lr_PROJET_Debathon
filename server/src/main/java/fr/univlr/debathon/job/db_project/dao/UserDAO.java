package fr.univlr.debathon.job.db_project.dao;

import fr.univlr.debathon.job.dao.DAO;
import fr.univlr.debathon.job.db_project.jobclass.User;
import fr.univlr.debathon.log.generate.CustomLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserDAO implements DAO<User> {

	private Connection connection;

	private static final CustomLogger LOGGER = CustomLogger.create(UserDAO.class.getName());

	public UserDAO(Connection conn) {
		this.connection = conn;
	}
	
	@Override
	public void setConnection(Connection conn) {
		this.connection = conn;
	}

	@Override
	public List<User> selectAll() {
		List<User> userList = new ArrayList<>();
		
		String sql = "SELECT idUser, label FROM User";
		
		try {
            PreparedStatement pstmt  = connection.prepareStatement(sql);
                
            ResultSet rs  = pstmt.executeQuery();
            
            while (rs.next()) {
                userList.add(new User(rs.getInt("idUser"), rs.getString("label")));
            }
		pstmt.close();
		} catch (Exception e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(String.format("Error : %s", e.getMessage()), e);
			}
		}
		
		
		return userList;
	}

	@Override
	public boolean insert(User user) throws SQLException {
		
		String sql = "INSERT INTO User (idUser, label) values (?,?)";
		
		try {
			PreparedStatement pstmt = connection.prepareStatement(sql);
			
			pstmt.setInt(1, user.getId());
			pstmt.setString(2, user.getLabel());
			
			pstmt.executeUpdate();
			pstmt.close();
		} catch (Exception e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(String.format("Error : %s", e.getMessage()), e);
			}
            return false;
		}
		
		return true;
	}

	@Override
	public boolean update(User user) throws SQLException {

		String sql = "UPDATE User"
					+ " SET label = ?"
					+ " WHERE idUser = ?";
		
		try {
			PreparedStatement pstmt = connection.prepareStatement(sql);
			
			pstmt.setString(1, user.getLabel());
			pstmt.setInt(2, user.getId());
			
			pstmt.executeUpdate();
			pstmt.close();
		} catch (Exception e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(String.format("Error : %s", e.getMessage()), e);
			}
			return false;
		}
		
		return true;
	}

	@Override
	public boolean delete(User user) throws SQLException {

		String sql = "DELETE User "
					+ " WHERE idUser = ?";
		
		try {
			PreparedStatement pstmt = connection.prepareStatement(sql);
			
			pstmt.setInt(1, user.getId());
			
			pstmt.executeUpdate();
			pstmt.close();
		} catch (Exception e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(String.format("Error : %s", e.getMessage()), e);
			}
			return false;
		}
		
		return true;
	}

	@Override
	public List<User> selectByMultiCondition(Map<String, String> map) {
		return null;
	}

	@Override
	public User select(int id) throws SQLException {

		User user = null;
		
		String sql = "SELECT idUser, label "
					+ "FROM User "
					+ "WHERE idUser = ?";
		
		try {
			PreparedStatement pstmt = connection.prepareStatement(sql);
			
			pstmt.setInt(1, id);
			
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next()) {
				user = new User(rs.getInt("idUser"), rs.getString("label"));
			}
			
			pstmt.close();
		} catch (Exception e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(String.format("Error : %s", e.getMessage()), e);
			}
		}
		
		return user;
	}

}
