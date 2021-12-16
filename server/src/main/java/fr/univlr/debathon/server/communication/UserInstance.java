package fr.univlr.debathon.server.communication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonDeserializer;
import fr.univlr.debathon.job.db_project.dao.*;
import fr.univlr.debathon.job.db_project.jobclass.*;
import fr.univlr.debathon.server.pdf.PDFdata;
import org.hildan.fxgson.FxGson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class UserInstance extends Thread implements Runnable {

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

        //this.testSendNewComment ();
        //this.testSendNewQuestion();
        //this.testSendNewRoom();
    }


    private <T> T getUnserialisation(String objects, Class<T> classT) {
        return FxGson.coreBuilder().registerTypeAdapter(
                        LocalDate.class,
                        (JsonDeserializer<LocalDate>) (json, type, jsonDeserializationContext) ->
                                LocalDate.parse(json.getAsJsonPrimitive().getAsString()))
                .enableComplexMapKeySerialization().create()
                .fromJson(objects, classT);

    }


    public int getWhereIam () {
        return this.whereIam;
    }


    public void sendData (ObjectNode root) throws JsonProcessingException {

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
                methodsUPDATE (dataJson, data);
                break;
            case "INSERT":
                methodsINSERT (dataJson, data);
                break;
            case "MAIL":
                methodsMAIL (dataJson, data);
                break;
        }

    }


    private void methodsMAIL(Map dataJson, String data) throws SQLException, JsonProcessingException {

        switch ((String) dataJson.get("request")) {
            case "NEW":
                this.caseNEW_MAIL(data);
                break;

        }

    }

    private void methodsUPDATE (Map dataJson, String data) throws SQLException, JsonProcessingException {

        switch ((String) dataJson.get("request")) {
            case "HOME":
                // this.caseHOME(data);
                break;
            case "COMMENT": //Cas ROOM souhaite
                // this.caseROOM (data);
                break;
            case "MCQ": //Cas ROOM souhaite
                switch ((String) dataJson.get("type")) {
                    case "VOTE":
                        this.caseUpdateLikeMCQ (dataJson, data);
                        break;
                }

                break;
        }

    }

    public void methodsINSERT (Map dataJson, String data) throws SQLException, JsonProcessingException {
        switch ((String) dataJson.get("request")) {
            case "ROOM": //Cas ROOM souhaite
                this.caseInsertROOM(dataJson, data);
                break;
            case "QUESTION": //Cas ROOM souhaite
                this.caseInsertQUESTION(dataJson, data);
                break;
            case "COMMENT": //Cas ROOM souhaite
                this.caseInsertCOMMENT(dataJson, data);
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
            RoomDAO roomDAO = new RoomDAO(Server.CONNECTION);
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
        ArrayNode mcq = rootRoom.putArray("mcq");

        RoomDAO roomDAO = new RoomDAO(Server.CONNECTION);
        //Selection de la room avec son id
        Room roomSelected = roomDAO.select((int) data.get("id"));
        room.addPOJO(roomSelected);

        McqDAO mcqDAO = new McqDAO(Server.CONNECTION);
        List<Mcq> mcqList = mcqDAO.selectMcqByIdSalon(roomSelected.getId());
        mcq.addPOJO(mcqList);

        System.out.println(rootRoom);

        this.sendData(rootRoom);
    }



    // CASE INSERT

    private void caseInsertROOM(Map dataMap, String data) throws JsonProcessingException, SQLException {

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode dataJson = objectMapper.readTree(data);
        RoomDAO roomDAO = new RoomDAO(Server.CONNECTION);
        TagDAO tagDAO = new TagDAO(Server.CONNECTION);

        Room room = this.getUnserialisation(dataJson.get("new_room").get(0).toString(), Room.class);


        for (Tag tag : room.getListTag())
        {
            if (tag.getId() == -1) {
                int id = tagDAO.insertAndReturnId(tag);
                tag.setId(id);
            }
        }

        Room roomRes = roomDAO.insertAndGetId(room);
        System.out.println("Nouveau salon d'id : " + roomRes.getId());

        if (roomRes != null) {
            this.sendNewRoom(roomRes);
        }

    }

    private void caseInsertQUESTION(Map dataMap, String data) throws JsonProcessingException, SQLException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode dataJson = objectMapper.readTree(data);

        QuestionDAO questionDAO = new QuestionDAO(Server.CONNECTION);
        McqDAO mcqDAO = new McqDAO(Server.CONNECTION);

        Question question = this.getUnserialisation(dataJson.get("new_question").get(0).toString(), Question.class);

        int id = questionDAO.insertAndGetId(question);
        Question q = questionDAO.select(id);
        System.out.println(">>>>> id :");
        System.out.println("\t" + id);

        for (Mcq mcq : question.getListMcq()) {
            mcq.setId_question(q.getId());
            Mcq r = mcqDAO.insertAndGetId(mcq);
            q.addMcq(r);
        }

        if (q != null) {
            this.sendNewQuestion(q);
        }

        System.out.println("Nouvelle question d'id : " + id);
        
    }

    private void caseInsertCOMMENT(Map dataMap, String data) throws JsonProcessingException, SQLException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode dataJson = objectMapper.readTree(data);

        CommentDAO commentDAO = new CommentDAO(Server.CONNECTION);

        Comment comment = this.getUnserialisation(dataJson.get("new_comment").get(0).toString(), Comment.class);

        int id = commentDAO.insertAndGetId(comment);

        Comment c = commentDAO.select(id);

        if (c != null) {
            this.sendNewComment(c);
        }

        System.out.println("Nouveau commentaire d'id : " + id);

    }


    // CASE UPDATE


    private void caseUpdateLikeMCQ (Map dataMap, String data) throws JsonProcessingException, SQLException {

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode dataJson = objectMapper.readTree(data);

        int id = dataJson.get("id").asInt();

        McqDAO mcqDAO = new McqDAO(Server.CONNECTION);
        mcqDAO.updateNewLike(id);

    }


    // CASE MAIL

    private void caseNEW_MAIL (String data) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode dataJson = objectMapper.readTree(data);

        int id = dataJson.get("id").asInt();
        String email = dataJson.get("email").asText();

        PDFdata.insertNewEmail(id, email);

    }














    private <T> ObjectNode getObjetNode (String methods, String propretyName, T object) {
        ObjectNode root = objectMapper.createObjectNode();

        root.put("methods",methods);

        ArrayNode c = root.putArray(propretyName);
        c.addPOJO(object);

        return root;
    }


    public void sendNewComment (Comment comment) throws JsonProcessingException {
        for (UserInstance ui : Server.USERINSTANCELIST) {
            if (ui != null && ui.getWhereIam() == comment.getRoom().getId()) {
                ui.sendData(this.getObjetNode("NEWCOMMENT", "new_comment", comment));
            }
        }
    }
    private void testSendNewComment() throws SQLException, JsonProcessingException {
        CommentDAO commentDAO = new CommentDAO(Server.CONNECTION);
        this.sendNewComment(commentDAO.select(1));
    }


    public void sendNewQuestion (Question question) throws JsonProcessingException {
        for (UserInstance ui : Server.USERINSTANCELIST) {
            if (ui != null && ui.getWhereIam() == question.getRoom().getId()) {
                ui.sendData(this.getObjetNode("NEWQUESTION", "new_question", question));
            }
        }
    }
    private void testSendNewQuestion () throws SQLException, JsonProcessingException {
        QuestionDAO questionDAO = new QuestionDAO(Server.CONNECTION);
        this.sendNewQuestion(questionDAO.select(1));
    }

    public void sendNewRoom (Room room) throws JsonProcessingException {
        for (UserInstance ui : Server.USERINSTANCELIST) {
            if (ui != null && ui.getWhereIam() == -1) {
                System.out.println("------> " + ui.getName());
                ui.sendData(this.getObjetNode("NEWROOM", "new_room", room));
            }
        }
    }
    private void testSendNewRoom () throws SQLException, JsonProcessingException {
        RoomDAO roomDAO = new RoomDAO(Server.CONNECTION);
        this.sendNewRoom(roomDAO.select(1));
    }
















    @Override
    public void run() {

        try {
            String data = in.readLine();

            StringBuilder dataReceveid = new StringBuilder();

            while(data!=null){
                dataReceveid.append(data);

                if (data.equals("}")) {
                    analyseData(dataReceveid.toString());
                    dataReceveid = new StringBuilder();
                }

                data = in.readLine();
            }
            System.out.println("Server out of service");
            Server.USERINSTANCELIST.remove(this);
            // out.close();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}
