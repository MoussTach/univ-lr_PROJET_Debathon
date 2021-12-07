package fr.univlr.debathon.server;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PDFdata {

    public static void fillQuestion (PDFquestion fquestion) {
        String sql = "SELECT label, type FROM Question WHERE idQuestion = ?";


        try {
            PreparedStatement pstmt = PDFGenerator.c.prepareStatement(sql);

            pstmt.setInt(1, fquestion.getIdQuestion());

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                fquestion.setLabelQuestion(rs.getString("label"));
                String choice = rs.getString("type");

                switch (choice) {
                    case "unique":
                        fquestion.setUniqueChoice(true);
                        break;
                    case "multiple":
                        fquestion.setUniqueChoice(false);
                        break;
                    default:
                        break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static List<PDFquestion> getRequest1 (int debate_id) {

        List<PDFquestion> list = new ArrayList<>();

        String sql = "SELECT DISTINCT M.id_question, M.label, M.nb_votes*100/(SELECT SUM(M2.nb_votes) "+
                                                                    "FROM MCQ M2 "+
                                                                    "WHERE M2.id_room = ? "+
                                                                    "AND M2.id_question = M.id_question) as p "+
                                                                "FROM MCQ M "+
                                                                "WHERE M.id_room = ? "+
                                                                "ORDER BY M.id_question, M.nb_votes DESC";

        try {
            PreparedStatement pstmt  = PDFGenerator.c.prepareStatement(sql);

            pstmt.setInt(1, debate_id);
            pstmt.setInt(2, debate_id);

            ResultSet rs  = pstmt.executeQuery();

            int id;
            float  pourcentage;
            String label;
            while (rs.next()) {
                id = rs.getInt("id_question");
                pourcentage = rs.getInt("p");
                label = rs.getString("label");

                boolean exist = false;
                for (PDFquestion q : list) {
                    if (q.getIdQuestion() == id) {
                        q.getMapResponse().put(label, pourcentage);
                        exist = true;
                    }
                }
                if (!exist) {
                    PDFquestion p = new PDFquestion(id);
                    p.getMapResponse().put(label, pourcentage);
                    list.add(p);
                }
            }


            for (PDFquestion p : list)
                fillQuestion(p);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;

    }


    public static List<String> getEmail (int id_debate) {

        List<String> list = new ArrayList<>();

        String sql = "SELECT email FROM EMAIL_ROOM WHERE id_room = ?";



        try {
            PreparedStatement pstmt = PDFGenerator.c.prepareStatement(sql);

            pstmt.setInt(1, id_debate);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next())
                list.add(rs.getString("email"));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;

    }

}
