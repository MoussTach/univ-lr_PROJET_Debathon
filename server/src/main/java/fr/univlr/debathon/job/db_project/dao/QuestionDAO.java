package fr.univlr.debathon.job.db_project.dao;

import fr.univlr.debathon.job.dao.DAO;
import fr.univlr.debathon.job.db_project.jobclass.Question;
import fr.univlr.debathon.job.db_project.jobclass.Room;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuestionDAO implements DAO<Question> {

	private Connection connection;
	
	public QuestionDAO(Connection conn) {
		this.connection = conn;
	}
	
	@Override
	public void setConnection(Connection conn) {
		this.connection = conn;
	}

	@Override
	public List<Question> selectAll() throws SQLException {
		
		List<Question> listQuestion = new ArrayList<>();
		
		String sql = "SELECT * FROM Question";
		
		try {
			PreparedStatement pstmt = this.connection.prepareStatement(sql);
			
			ResultSet rs = pstmt.executeQuery();
			
			RoomDAO roomDAO = new RoomDAO(this.connection);
			CommentDAO commentDAO = new CommentDAO(this.connection);
			UserDAO userDAO = new UserDAO(this.connection);
			
			while (rs.next()) {
				
				
				listQuestion.add(new Question(rs.getInt("idQuestion"), rs.getString("label"), rs.getString("context"), 
						rs.getString("type"), rs.getBoolean("is_active"), roomDAO.select(rs.getInt("id_room")), 
						commentDAO.selectCommentByIdQuestion(rs.getInt("idQuestion")), userDAO.select(rs.getInt("id_user"))));
				
			}
			
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return listQuestion;
	}

	@Override
	public boolean insert(Question question) throws SQLException {

		String sql = "INSERT INTO Question (label, context, type, id_room, id_user) values (?,?,?,?,?)";
		
		try {
			
			PreparedStatement pstmt = this.connection.prepareStatement(sql);
			
			pstmt.setString(1, question.getLabel());
			pstmt.setString(2, question.getContext());
			pstmt.setString(3, question.getType());
			pstmt.setInt(4, question.getRoom().getId());
			pstmt.setInt(5, question.getUser().getId());
			
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
		
		return true;
	}

	public int insertAndGetId(Question question) throws SQLException {

		String sql = "INSERT INTO Question (label, context, type, id_room, id_user) values (?,?,?,?,?)";

		try {

			PreparedStatement pstmt = this.connection.prepareStatement(sql);

			pstmt.setString(1, question.getLabel());
			pstmt.setString(2, question.getContext());
			pstmt.setString(3, question.getType());
			pstmt.setInt(4, question.getRoom().getId());
			pstmt.setInt(5, question.getUser().getId());

			pstmt.executeUpdate();

			return this.selectByContextAndLabel(question.getLabel(), question.getContext());

		} catch (Exception e) {
			System.out.println(e.getMessage());
			return -2;
		}
	}

	@Override
	public boolean update(Question question) throws SQLException {

		String sql = "UPDATE Question SET label = ?, context = ?, type = ?, id_room = ?, id_user = ? WHERE idQuestion = ?";
		
		try {
			
			PreparedStatement pstmt = this.connection.prepareStatement(sql);
			
			pstmt.setString(1, question.getLabel());
			pstmt.setString(2, question.getContext());
			pstmt.setString(3, question.getType());
			pstmt.setInt(4, question.getRoom().getId());
			pstmt.setInt(5, question.getUser().getId());
			pstmt.setInt(6, question.getId());
			
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
		
		return true;
	}

	@Override
	public boolean delete(Question question) throws SQLException {

		String sql = "DELETE Question WHERE idQuestion = ?";
		
		try {
			
			PreparedStatement pstmt = this.connection.prepareStatement(sql);
			
			pstmt.setInt(1, question.getId());
			
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
		
		return true;
	}

	@Override
	public List<Question> selectByMultiCondition(Map<String, String> map) throws SQLException {
		return null;
	}

	@Override
	public Question select(int id) throws SQLException {
		Question question = null;
		
		String sql = "SELECT * FROM Question WHERE idQuestion = ?";
		
		try {
			PreparedStatement pstmt = this.connection.prepareStatement(sql);
			
			pstmt.setInt(1, id);
			
			ResultSet rs = pstmt.executeQuery();
			
			RoomDAO roomDAO = new RoomDAO(this.connection);
			CommentDAO commentDAO = new CommentDAO(this.connection);
			UserDAO userDAO = new UserDAO(this.connection);
			
			if (rs.next()) {
				
				
				question = new Question(rs.getInt("idQuestion"), rs.getString("label"), rs.getString("context"), 
						rs.getString("type"), rs.getBoolean("is_active"), roomDAO.select(rs.getInt("id_room")), 
						commentDAO.selectCommentByIdQuestion(rs.getInt("idQuestion")), userDAO.select(rs.getInt("id_user")));
				
			}
			
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return question;
	}


	public int selectByContextAndLabel(String label, String context) throws SQLException {
		Question question = null;

		String sql = "SELECT * FROM Question WHERE label = ? AND context = ?";

		try {
			PreparedStatement pstmt = this.connection.prepareStatement(sql);

			pstmt.setString(1, label);
			pstmt.setString(2, context);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				return rs.getInt("idQuestion");
			}


		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}

		return -1;
	}

	public List<Question> selectByIdSalon(int id) throws SQLException {

		List<Question> listQuestion = new ArrayList<>();
		
		String sql = "SELECT * FROM Question WHERE id_room = ?";
		
		try {
			PreparedStatement pstmt = this.connection.prepareStatement(sql);
			
			pstmt.setInt(1, id);
			
			ResultSet rs = pstmt.executeQuery();
			
			RoomDAO roomDAO = new RoomDAO(this.connection);
			CommentDAO commentDAO = new CommentDAO(this.connection);
			UserDAO userDAO = new UserDAO(this.connection);
			
			while (rs.next()) {
				
				
				listQuestion.add(new Question(rs.getInt("idQuestion"), rs.getString("label"), rs.getString("context"), 
						rs.getString("type"), rs.getBoolean("is_active"), roomDAO.select(rs.getInt("id_room")),
						commentDAO.selectCommentByIdQuestion(rs.getInt("idQuestion")), userDAO.select(rs.getInt("id_user"))));
				
			}
			
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return listQuestion;
	}

	public List<Question> selectBySalon(Room room) throws SQLException {

		List<Question> listQuestion = new ArrayList<>();

		String sql = "SELECT idQuestion, label, context, type, is_active, id_room, id_user FROM Question WHERE id_room = ?";

		try {
			PreparedStatement pstmt = this.connection.prepareStatement(sql);

			pstmt.setInt(1, room.getId());

			ResultSet rs = pstmt.executeQuery();

			CommentDAO commentDAO = new CommentDAO(this.connection);
			UserDAO userDAO = new UserDAO(this.connection);

			while (rs.next()) {


				listQuestion.add(new Question(rs.getInt("idQuestion"), rs.getString("label"), rs.getString("context"),
						rs.getString("type"), rs.getBoolean("is_active"), null,
						commentDAO.selectCommentByIdQuestion(rs.getInt("idQuestion"), null), userDAO.select(rs.getInt("id_user"))));

			}


		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return listQuestion;
	}
	
}
