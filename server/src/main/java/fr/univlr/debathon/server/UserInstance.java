package fr.univlr.debathon.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.univlr.debathon.job.db_project.dao.McqDAO;
import fr.univlr.debathon.job.db_project.dao.QuestionDAO;
import fr.univlr.debathon.job.db_project.dao.RoomDAO;
import fr.univlr.debathon.job.db_project.jobclass.Mcq;
import fr.univlr.debathon.job.db_project.jobclass.Question;
import fr.univlr.debathon.job.db_project.jobclass.Room;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class UserInstance implements Runnable {

    public  final Socket socket;
    public BufferedReader in;
    public PrintWriter out;

    public UserInstance(Socket userSocket) throws IOException {

        this.socket = userSocket;
        in = new BufferedReader(new InputStreamReader(userSocket.getInputStream()));
        out = new PrintWriter(userSocket.getOutputStream());
    }

    public void analyseData (String data) throws JsonProcessingException, SQLException {
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(data);
        Map dataJson = objectMapper.readValue(data, new TypeReference<Map>() {});

        switch ((String) dataJson.get("methods")) {
            case "GET":
                methodsGET(dataJson, objectMapper);
                break;
        }

    }

    public void methodsGET(Map data, ObjectMapper objectMapper) throws SQLException, JsonProcessingException {
        switch ((String) data.get("request")) {
            case "HOME":
                RoomDAO roomDAO = new RoomDAO(Server.c);
                List<Room> list = roomDAO.selectAll();
                ObjectNode root = objectMapper.createObjectNode();
                root.put("methods", "RESPONSE");
                ArrayNode rooms = root.putArray("rooms");
                for (Room room : list) {
                    rooms.addPOJO(room);
                }
                out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(root));
                out.flush();
                break;
            case "ROOM": //Cas ROOM souhaite
                ObjectNode rootRoom = objectMapper.createObjectNode();
                //utilisation methode RESPONSEDETAILS cote client
                rootRoom.put("methods","RESPONSEDETAILS");
                //ajout de la room selectionne
                ArrayNode room = rootRoom.putArray("room_selected");
                //ajout des question de cette room
                ArrayNode questions = rootRoom.putArray("questions_room");
                ArrayNode mcq = rootRoom.putArray("mcq_room");
                RoomDAO roomDAOid = new RoomDAO(Server.c);
                //Selection de la room avec son id
                Room roomSelected = roomDAOid.select((int) data.get("id"));
                room.addPOJO(roomSelected);
                QuestionDAO questionDAO = new QuestionDAO(Server.c);
                McqDAO mcqDAO = new McqDAO(Server.c);

                //Selection des questions lie a la room
                List<Question> questionsDB = questionDAO.selectByIdSalon((int) data.get("id"));
                //Boucle pour ajout des question dans node
                for(Question q : questionsDB){
                    questions.addPOJO(q);
                    for (Mcq m : mcqDAO.selectMcqByIdQuestion(q.getId())) {
                        mcq.addPOJO(m);
                    }

                }
                out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootRoom));
                out.flush();
                break;
        }
    }

    @Override
    public void run() {

        Server.userManager.addUser(this, -1);
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
