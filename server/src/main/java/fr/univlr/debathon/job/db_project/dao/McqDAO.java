package fr.univlr.debathon.job.db_project.dao;

import fr.univlr.debathon.job.dao.DAO;
import fr.univlr.debathon.job.db_project.jobclass.Mcq;
import fr.univlr.debathon.log.generate.CustomLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class McqDAO implements DAO<Mcq> {

	private Connection connection;

	private static final CustomLogger LOGGER = CustomLogger.create(McqDAO.class.getName());

	public McqDAO(Connection conn) {
			this.connection = conn;
	}
	
	@Override
	public void setConnection(Connection conn) {
		this.connection = conn;
	}

	@Override
	public List<Mcq> selectAll() throws SQLException {
        List<Mcq> qcmList = new ArrayList<>();

        String sql = "SELECT idMcq, label, nb_votes, id_question, id_room FROM Mcq";

        try {
            PreparedStatement pstmt  = connection.prepareStatement(sql);
            
            ResultSet rs  = pstmt.executeQuery();
            
            QuestionDAO questionDAO = new QuestionDAO(this.connection);
            RoomDAO roomDAO = new RoomDAO(this.connection);
            
            while (rs.next()) {
            	
               qcmList.add(new Mcq(rs.getInt("idMcq"), rs.getString("label"), rs.getInt("nb_votes"), rs.getInt("id_question"), rs.getInt("id_room")));
            }

			pstmt.close();
        } catch (SQLException e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(String.format("Error : %s", e.getMessage()), e);
			}
        }

        return qcmList;  	
    }
	
	

	@Override
	public boolean insert(Mcq mcq) throws SQLException {

		String sql = "INSERT INTO MCQ (label, nb_votes, id_question, id_room) values (?,?,?,?)";
		
		try {
			
			PreparedStatement pstmt = this.connection.prepareStatement(sql);
			
			pstmt.setString(1, mcq.getLabel());
			pstmt.setInt(2, mcq.getNb_votes());
			pstmt.setInt(3, mcq.getId_question());
			pstmt.setInt(4, mcq.getId_room());
			
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

	public Mcq insertAndGetId(Mcq mcq) throws SQLException {

		String sql = "INSERT INTO MCQ (label, id_question, id_room) values (?,?,?) returning idMcq";

		try {

			PreparedStatement pstmt = this.connection.prepareStatement(sql);

			pstmt.setString(1, mcq.getLabel());
			pstmt.setInt(2, mcq.getId_question());
			pstmt.setInt(3, mcq.getId_room());

			ResultSet rs = pstmt.executeQuery();
			int id = -1;
			if (rs.next()) {
				id = rs.getInt("idMcq");
			}
			pstmt.close();
			return this.select(id);


		} catch (Exception e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(String.format("Error : %s", e.getMessage()), e);
			}
			return null;
		}

	}


	@Override
	public boolean update(Mcq mcq) throws SQLException {

		String sql = "UPDATE MCQ SET label = ?, nb_votes = ?, id_question = ?, id_room = ? WHERE idMcq = ?";
		
		try {
			
			PreparedStatement pstmt = this.connection.prepareStatement(sql);
			
			pstmt.setString(1, mcq.getLabel());
			pstmt.setInt(2, mcq.getNb_votes());
			pstmt.setInt(3, mcq.getId_question());
			pstmt.setInt(4, mcq.getId_room());
			pstmt.setInt(5, mcq.getId());
			
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

	public boolean updateNewLike(int id) throws SQLException {

		String sql = "UPDATE MCQ SET nb_votes = nb_votes + 1 WHERE idMcq = ?";

		try {

			PreparedStatement pstmt = this.connection.prepareStatement(sql);

			pstmt.setInt(1, id);

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
	public boolean delete(Mcq mcq) throws SQLException {

		String sql = "DELETE MCQ WHERE idMcq = ?";
		
		try {
			
			PreparedStatement pstmt = this.connection.prepareStatement(sql);
			
			pstmt.setInt(1, mcq.getId());
			
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
	public List<Mcq> selectByMultiCondition(Map<String, String> map) throws SQLException {
		return null;
	}

	@Override
	public Mcq select(int id) throws SQLException {
        Mcq mcq = null;

        String sql = "SELECT idMcq, label, nb_votes, id_question, id_room FROM Mcq WHERE idMcq = ?";

        try {
            PreparedStatement pstmt  = connection.prepareStatement(sql);
            
            pstmt.setInt(1, id);
            
            ResultSet rs  = pstmt.executeQuery();
            
            QuestionDAO questionDAO = new QuestionDAO(this.connection);
            RoomDAO roomDAO = new RoomDAO(this.connection);
            
            while (rs.next()) {
            	
               mcq = new Mcq(rs.getInt("idMcq"), rs.getString("label"), rs.getInt("nb_votes"), rs.getInt("id_question"), rs.getInt("id_room"));
            }
			pstmt.close();
        } catch (SQLException e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(String.format("Error : %s", e.getMessage()), e);
			}
        }

        return mcq;  	
	}

	public int select(String label, int id_question) throws SQLException {
		Mcq mcq = null;

		String sql = "SELECT idMcq FROM Mcq WHERE label = ? and id_question = ?";

		try {
			PreparedStatement pstmt  = connection.prepareStatement(sql);

			pstmt.setString(1, label);
			pstmt.setInt(2, id_question);

			ResultSet rs  = pstmt.executeQuery();

			if (rs.next()) {
				return rs.getInt("idMcq");
			}
		pstmt.close();
		} catch (SQLException e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(String.format("Error : %s", e.getMessage()), e);
			}
		}
		return -1;
	}

	
	public List<Mcq> selectMcqByIdQuestion(int id) throws SQLException {
        List<Mcq> qcmList = new ArrayList<>();

        String sql = "SELECT idMcq, label, nb_votes, id_question, id_room FROM Mcq WHERE id_question = ?";

        try {
            PreparedStatement pstmt  = connection.prepareStatement(sql);
            
            pstmt.setInt(1, id);
            
            ResultSet rs  = pstmt.executeQuery();
            
            QuestionDAO questionDAO = new QuestionDAO(this.connection);
            RoomDAO roomDAO = new RoomDAO(this.connection);
            
            while (rs.next()) {
            	
               qcmList.add(new Mcq(rs.getInt("idMcq"), rs.getString("label"), rs.getInt("nb_votes"), rs.getInt("id_question"), rs.getInt("id_room")));
            }
		pstmt.close();
        } catch (SQLException e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(String.format("Error : %s", e.getMessage()), e);
			}
        }

        return qcmList;  	
	}


	public List<Mcq> selectMcqByIdSalon(int id) throws SQLException {
		List<Mcq> qcmList = new ArrayList<>();

		String sql = "SELECT idMcq, label, nb_votes, id_question, id_room FROM Mcq WHERE id_room = ?";

		try {
			PreparedStatement pstmt  = connection.prepareStatement(sql);

			pstmt.setInt(1, id);

			ResultSet rs  = pstmt.executeQuery();

			QuestionDAO questionDAO = new QuestionDAO(this.connection);
			RoomDAO roomDAO = new RoomDAO(this.connection);

			while (rs.next()) {

				qcmList.add(new Mcq(rs.getInt("idMcq"), rs.getString("label"), rs.getInt("nb_votes"), rs.getInt("id_question"), rs.getInt("id_room")));
			}
		pstmt.close();
		} catch (SQLException e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(String.format("Error : %s", e.getMessage()), e);
			}
		}

		return qcmList;
	}
	
}
