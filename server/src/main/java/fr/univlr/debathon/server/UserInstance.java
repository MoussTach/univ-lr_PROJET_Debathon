package fr.univlr.debathon.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.univlr.debathon.job.db_project.dao.CommentDAO;
import fr.univlr.debathon.job.db_project.dao.QuestionDAO;
import fr.univlr.debathon.job.db_project.dao.RoomDAO;
import fr.univlr.debathon.job.db_project.jobclass.Comment;
import fr.univlr.debathon.job.db_project.jobclass.Question;
import fr.univlr.debathon.job.db_project.jobclass.Room;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class UserInstance implements Runnable {

    public  final Socket socket;
    public BufferedReader in;
    public PrintWriter out;
    private int whereIam = -1;

    private ObjectMapper objectMapper;


    public UserInstance(Socket userSocket) throws IOException, SQLException {

        this.socket = userSocket;
        in = new BufferedReader(new InputStreamReader(userSocket.getInputStream()));
        out = new PrintWriter(userSocket.getOutputStream());
        this.objectMapper = new ObjectMapper();

        this.testSendNewComment ();
        this.testSendNewQuestion();
        this.testSendNewRoom();
    }



    public int getWhereIam () {
        return this.whereIam;
    }


    private void sendData (ObjectNode root) throws JsonProcessingException {
        out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(root));
        out.flush();
    }


    public void analyseData (String data) throws JsonProcessingException, SQLException {
        System.out.println(data);
        Map dataJson = objectMapper.readValue(data, new TypeReference<Map>() {});

        switch ((String) dataJson.get("methods")) {
            case "GET":
                methodsGET(dataJson);
                break;
            case "UPDATE":
                methodsUPDATE (dataJson);
                break;

        }

    }


    private void methodsUPDATE(Map dataJson) throws SQLException, JsonProcessingException {

        Map data = null;
        switch ((String) data.get("request")) {
            case "HOME":
                // this.caseHOME(data);
                break;
            case "ROOM": //Cas ROOM souhaite
                // this.caseROOM (data);
                break;
        }

    }

    public void methodsGET(Map data) throws SQLException, JsonProcessingException {
        switch ((String) data.get("request")) {
            case "HOME":
                this.caseGetHOME(data);
                break;
            case "ROOM": //Cas ROOM souhaite
                this.caseGetROOM(data);
                break;
        }
    }


    // CASE GET

    private void caseGetHOME(Map data) throws SQLException, JsonProcessingException {
        this.whereIam = -1;
        try {
            RoomDAO roomDAO = new RoomDAO(Server.c);
            List<Room> list = roomDAO.selectAll();

            ObjectNode root = objectMapper.createObjectNode();
            root.put("methods", "RESPONSE");
            ArrayNode rooms = root.putArray("rooms");

            for (Room room : list) {
                rooms.addPOJO(room);
            }
            this.sendData(root);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void caseGetROOM(Map data) throws SQLException, JsonProcessingException {
        this.whereIam = (int) data.get("id");

        ObjectNode rootRoom = objectMapper.createObjectNode();
        //utilisation methode RESPONSEDETAILS cote client
        rootRoom.put("methods","RESPONSEDETAILS");
        //ajout de la room selectionne
        ArrayNode room = rootRoom.putArray("room_selected");

        RoomDAO roomDAO = new RoomDAO(Server.c);
        //Selection de la room avec son id
        Room roomSelected = roomDAO.select((int) data.get("id"));
        room.addPOJO(roomSelected);

        this.sendData(rootRoom);
    }

    private <T> ObjectNode getObjetNode (String methods, String propretyName, T object) {
        ObjectNode root = objectMapper.createObjectNode();

        root.put("methods",methods);

        ArrayNode c = root.putArray(propretyName);
        c.addPOJO(object);

        return root;
    }


    public void sendNewComment (Comment comment) throws JsonProcessingException {
        this.sendData(this.getObjetNode("NEWCOMMENT", "new_comment", comment));
    }
    private void testSendNewComment() throws SQLException, JsonProcessingException {
        CommentDAO commentDAO = new CommentDAO(Server.c);
        this.sendNewComment(commentDAO.select(1));
    }


    public void sendNewQuestion (Question question) throws JsonProcessingException {
        this.sendData(this.getObjetNode("NEWQUESTION", "new_question", question));
    }
    private void testSendNewQuestion () throws SQLException, JsonProcessingException {
        QuestionDAO questionDAO = new QuestionDAO(Server.c);
        this.sendNewQuestion(questionDAO.select(1));
    }

    public void sendNewRoom (Room room) throws JsonProcessingException {
        this.sendData(this.getObjetNode("NEWROOM", "new_room", room));
    }
    private void testSendNewRoom () throws SQLException, JsonProcessingException {
        RoomDAO roomDAO = new RoomDAO(Server.c);
        this.sendNewRoom(roomDAO.select(1));
    }

















    @Override
    public void run() {

        try {
            String data = in.readLine();

            String dataReceveid = "";

            while(data!=null){
                dataReceveid += data;

                if (data.equals("}")) {
                    analyseData(dataReceveid);
                    dataReceveid = "";
                }

                data = in.readLine();
            }
            System.out.println("Server out of service");
            // out.close();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}
