package fr.univlr.debathon.job.db_project.dao;

import fr.univlr.debathon.job.dao.DAO;
import fr.univlr.debathon.job.db_project.jobclass.Comment;
import fr.univlr.debathon.job.db_project.jobclass.Question;
import fr.univlr.debathon.job.db_project.jobclass.Room;
import fr.univlr.debathon.log.generate.CustomLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommentDAO implements DAO<Comment> {

	private Connection connection;

	private static final CustomLogger LOGGER = CustomLogger.create(CategoryDAO.class.getName());

	public CommentDAO(Connection conn) {
		this.connection = conn;
	}

	@Override
	public void setConnection(Connection conn) {
		this.connection = conn;
	}

	@Override
	public List<Comment> selectAll() {

		List<Comment> commentList = new ArrayList<>();

		String sql = "SELECT * FROM Comment";

		try (PreparedStatement pstmt = this.connection.prepareStatement(sql)) {

			ResultSet rs = pstmt.executeQuery();

			QuestionDAO questionDAO = new QuestionDAO(this.connection);
			RoomDAO roomDAO = new RoomDAO(this.connection);
			UserDAO userDAO = new UserDAO(this.connection);

			Comment comment = null;
			Question question = null;


			while (rs.next()) {

				if (!("" + rs.getInt("id_parent")).equals("")) {
					comment = this.select(rs.getInt("id_parent"));
				}

				if (!("" + rs.getInt("id_question")).equals("")) {
					question = questionDAO.select(rs.getInt("id_parent"));
				}

				commentList.add(
						new Comment(
								rs.getInt("idComment"), rs.getString("comment"),
								rs.getInt("nb_likes"), rs.getInt("nb_dislikes"),
								comment, question, roomDAO.select(rs.getInt("id_room")),
								userDAO.select(rs.getInt("id_user"))
						)
				);
			}

		} catch (Exception e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(String.format("Error : %s", e.getMessage()), e);
			}
		}

		return commentList;

	}

	public List<Comment> selectCommentByIdQuestion(int id) {

		List<Comment> commentList = new ArrayList<>();

		String sql = "SELECT * FROM Comment WHERE id_question = ?";

		try (PreparedStatement pstmt = this.connection.prepareStatement(sql)) {

			pstmt.setInt(1, id);

			ResultSet rs = pstmt.executeQuery();

			QuestionDAO questionDAO = new QuestionDAO(this.connection);
			RoomDAO roomDAO = new RoomDAO(this.connection);
			UserDAO userDAO = new UserDAO(this.connection);

			Comment comment = null;
			Question question = null;


			while (rs.next()) {

				if (!("" + rs.getInt("id_parent")).equals("")) {
					comment = this.select(rs.getInt("id_parent"));
				}

				if (!("" + rs.getInt("id_question")).equals("")) {
					question = questionDAO.select(rs.getInt("id_parent"));
				}

				commentList.add(
						new Comment(
								rs.getInt("idComment"), rs.getString("comment"),
								rs.getInt("nb_likes"), rs.getInt("nb_dislikes"),
								comment, question, roomDAO.select(rs.getInt("id_room")),
								userDAO.select(rs.getInt("id_user"))
						)
				);
			}

		} catch (Exception e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(String.format("Error : %s", e.getMessage()), e);
			}
		}

		return commentList;

	}




	public List<Comment> selectCommentByIdQuestion(int id, Room room) {

		List<Comment> commentList = new ArrayList<>();

		String sql = "SELECT * FROM Comment WHERE id_question = ?";

		try (PreparedStatement pstmt = this.connection.prepareStatement(sql)) {

			pstmt.setInt(1, id);

			ResultSet rs = pstmt.executeQuery();

			QuestionDAO questionDAO = new QuestionDAO(this.connection);
			UserDAO userDAO = new UserDAO(this.connection);

			Comment comment = null;
			Question question = null;


			while (rs.next()) {

				if (!("" + rs.getInt("id_parent")).equals("")) {
					comment = this.select(rs.getInt("id_parent"));
				}

				if (!("" + rs.getInt("id_question")).equals("")) {
					question = questionDAO.select(rs.getInt("id_parent"));
				}

				commentList.add(
						new Comment(
								rs.getInt("idComment"), rs.getString("comment"),
								rs.getInt("nb_likes"), rs.getInt("nb_dislikes"),
								comment, question, room,
								userDAO.select(rs.getInt("id_user"))
						)
				);
			}

		} catch (Exception e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(String.format("Error : %s", e.getMessage()), e);
			}
		}

		return commentList;

	}





	@Override
	public boolean insert(Comment comment) throws SQLException {

		String sql = "INSERT INTO Comment (comment, id_parent, id_question, id_room, id_user) values (?,?,?,?,?)";

		int id_parent = comment.getParent() != null ? comment.getParent().getId() : null;
		int id_question = comment.getQuestion() != null ? comment.getQuestion().getId() : null;

		try (PreparedStatement pstmt = this.connection.prepareStatement(sql)) {

			pstmt.setString(1, comment.getComment());
			pstmt.setInt(2, id_parent);
			pstmt.setInt(3, id_question);
			pstmt.setInt(4, comment.getRoom().getId());
			pstmt.setInt(5, comment.getUser().getId());

			pstmt.executeUpdate();

		} catch (Exception e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(String.format("Error : %s", e.getMessage()), e);
			}
			return false;
		}

		return true;
	}

	public int insertAndGetId(Comment comment) {

		String sql = "INSERT INTO Comment (comment, id_parent, id_question, id_room, id_user) values (?,?,?,?,?) returning idComment";

		try (PreparedStatement pstmt = this.connection.prepareStatement(sql)) {

			pstmt.setString(1, comment.getComment());
			if (comment.getParent() != null) {
				pstmt.setInt(2, comment.getParent().getId());
			} else {
				pstmt.setObject(2, null);
			}

			if (comment.getQuestion() != null) {
				pstmt.setInt(3, comment.getQuestion().getId());
			} else {
				pstmt.setObject(3, null);
			}
			pstmt.setInt(4, comment.getRoom().getId());
			pstmt.setInt(5, comment.getUser().getId());

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				return rs.getInt("idComment");
			}

		} catch (Exception e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(String.format("Error : %s", e.getMessage()), e);
			}
			return -1;
		}
		return -1;
	}

	@Override
	public boolean update(Comment comment) throws SQLException {

		String sql = "UPDATE Comment SET comment=?, nb_likes=?, nb_dislikes=?, id_parent=?, id_question=?, id_room=?, id_user=? WHERE idComment = ?";

		int id_parent = comment.getParent() != null ? comment.getParent().getId() : null;
		int id_question = comment.getQuestion() != null ? comment.getQuestion().getId() : null;

		try (PreparedStatement pstmt = this.connection.prepareStatement(sql)) {

			pstmt.setString(1, comment.getComment());
			pstmt.setInt(2, comment.getNb_likes());
			pstmt.setInt(3, comment.getNb_dislikes());
			pstmt.setInt(4, id_parent);
			pstmt.setInt(5, id_question);
			pstmt.setInt(6, comment.getRoom().getId());
			pstmt.setInt(7, comment.getUser().getId());
			pstmt.setInt(8, comment.getId());

			pstmt.executeUpdate();
		} catch (Exception e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(String.format("Error : %s", e.getMessage()), e);
			}
			return false;
		}

		return true;
	}

	@Override
	public boolean delete(Comment comment) throws SQLException {

		String sql = "DELETE Comment WHERE idComment = ?";

		try (PreparedStatement pstmt = this.connection.prepareStatement(sql)) {

			pstmt.setInt(1, comment.getId());

			pstmt.executeUpdate();
		} catch (Exception e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(String.format("Error : %s", e.getMessage()), e);
			}
			return false;
		}

		return true;
	}

	@Override
	public List<Comment> selectByMultiCondition(Map<String, String> map) {
		return null;
	}

	@Override
	public Comment select(int id) throws SQLException {
		Comment comment = null;

		String sql = "SELECT * from Comment WHERE idComment = ?";

		try (PreparedStatement pstmt = this.connection.prepareStatement(sql)) {

			pstmt.setInt(1, id);

			ResultSet rs = pstmt.executeQuery();


			if (rs.next()) {

				QuestionDAO questionDAO = new QuestionDAO(this.connection);
				RoomDAO roomDAO = new RoomDAO(this.connection);
				UserDAO userDAO = new UserDAO(this.connection);


				Comment comParent = null;
				if (!("" + rs.getInt("id_parent")).equals("")) {
					comment = this.select(rs.getInt("id_parent"));
				}

				Question question = null;
				if (!("" + rs.getInt("id_question")).equals("")) {
					question = questionDAO.select(rs.getInt("id_question"));
				}

				comment = new Comment(
						rs.getInt("idComment"), rs.getString("comment"),
						rs.getInt("nb_likes"), rs.getInt("nb_dislikes"),
						comParent, question, roomDAO.select(rs.getInt("id_room")),
						userDAO.select(rs.getInt("id_user"))
				);
			}

		} catch (Exception e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(String.format("Error : %s", e.getMessage()), e);
			}
		}

		return comment;
	}




	public boolean updateLike(int id_comment) {

		String sql = "UPDATE Comment SET nb_likes=nb_likes+1 WHERE idComment = ?";


		try (PreparedStatement pstmt = this.connection.prepareStatement(sql)) {

			pstmt.setInt(1, id_comment);

			pstmt.executeUpdate();
		} catch (Exception e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(String.format("Error : %s", e.getMessage()), e);
			}
			return false;
		}

		return true;
	}

	public boolean updateDislike(int id_comment) {

		String sql = "UPDATE Comment SET nb_dislikes=nb_dislikes+1 WHERE idComment = ?";


		try (PreparedStatement pstmt = this.connection.prepareStatement(sql)) {

			pstmt.setInt(1, id_comment);

			pstmt.executeUpdate();
		} catch (Exception e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(String.format("Error : %s", e.getMessage()), e);
			}
			return false;
		}

		return true;
	}

}
