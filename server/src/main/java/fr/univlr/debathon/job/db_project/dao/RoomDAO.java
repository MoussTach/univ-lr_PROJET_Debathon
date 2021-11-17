package fr.univlr.debathon.job.db_project.dao;

import fr.univlr.debathon.job.dao.DAO;
import fr.univlr.debathon.job.db_project.jobclass.Question;
import fr.univlr.debathon.job.db_project.jobclass.Room;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RoomDAO implements DAO<Room> {

	private Connection connection;
	
	public RoomDAO(Connection conn) {
		this.connection = conn;
	}

	@Override
	public void setConnection(Connection conn) {
		this.connection = conn;
	}

	@Override
	public List<Room> selectAll() throws SQLException {
		
		List<Room> listRoom = new ArrayList<>();
		
		String sql = "SELECT * FROM Room";
		
		try {
			PreparedStatement pstmt = this.connection.prepareStatement(sql);
			
			ResultSet rs = pstmt.executeQuery();
			
			CategoryDAO categoryDAO = new CategoryDAO(this.connection);
			TagDAO tagDAO = new TagDAO(this.connection);
			QuestionDAO questionDAO = new QuestionDAO(this.connection);
			
			while (rs.next()) {				
				
				listRoom.add(new Room(rs.getInt("idRoom"), rs.getString("label"), rs.getString("description"), 
										rs.getString("key"), rs.getString("mail"), rs.getBoolean("is_open"), 
										null, null, categoryDAO.select(rs.getInt("id_category")),
										tagDAO.selectByIdRoom(rs.getInt("idRoom")), questionDAO.selectByIdSalon(rs.getInt("idRoom"))));
			}
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return listRoom;
	}



	@Override
	public boolean insert(Room room) throws SQLException {

		String sql = "INSERT INTO Room values (?,?,?,?,?,?,?)";
		
		try {
			PreparedStatement pstmt = this.connection.prepareStatement(sql);
			
			pstmt.setString(1, room.getLabel());
			pstmt.setString(2, room.getDescription());
			pstmt.setString(3, room.getKey());
			pstmt.setString(4, room.getMail());
			pstmt.setBoolean(5, room.getIs_open());
			pstmt.setDate(6, Date.valueOf(room.getDate_end()));
			pstmt.setInt(7, room.getCategory().getId());
			
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
		
		return true;
	}

	@Override
	public boolean update(Room room) throws SQLException {

		String sql = "UPDATE Room SET label=?, description=?, key=?, mail=?, is_open=?, date_start=?, date_end=?, id_category=? WHERE idRoom=?";
		
		try {
			PreparedStatement pstmt = this.connection.prepareStatement(sql);
			
			pstmt.setString(1, room.getLabel());
			pstmt.setString(2, room.getDescription());
			pstmt.setString(3, room.getKey());
			pstmt.setString(4, room.getMail());
			pstmt.setBoolean(5, room.getIs_open());
			pstmt.setDate(6, Date.valueOf(room.getDate_start()));
			pstmt.setDate(7, Date.valueOf(room.getDate_end()));
			pstmt.setInt(8, room.getCategory().getId());
			pstmt.setInt(9, room.getId());
			
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}

		
		return true;
	}

	@Override
	public boolean delete(Room room) throws SQLException {
		
		String sql = "DETELE Room where idRoom = ?";
		
		try {
			PreparedStatement pstmt = this.connection.prepareStatement(sql);
			
			pstmt.setInt(1, room.getId());
			
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
		
		return true;
	}

	@Override
	public List<Room> selectByMultiCondition(Map<String, String> map) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Room select(int id) throws SQLException {

		Room room = null;
		
		String sql = "SELECT * from Room where idRoom=?";
		
		try {
			PreparedStatement pstmt = this.connection.prepareStatement(sql);
			
			pstmt.setInt(1, id);
			
			ResultSet rs = pstmt.executeQuery();
			
			CategoryDAO categoryDAO = new CategoryDAO(this.connection);
			TagDAO tagDAO = new TagDAO(this.connection);
			QuestionDAO questionDAO = new QuestionDAO(this.connection);
			System.out.println("test");
			if (rs.next()) {
				room = new Room();
				room.setId(rs.getInt("idRoom"));
				room = new Room(rs.getInt("idRoom"), rs.getString("label"), rs.getString("description"), 
						rs.getString("key"), rs.getString("mail"), rs.getBoolean("is_open"), 
						null, null, categoryDAO.select(rs.getInt("id_category")),
						tagDAO.selectByIdRoom(rs.getInt("idRoom")), questionDAO.selectBySalon(room));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return room;
	}

}
