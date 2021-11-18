package fr.univlr.debathon.application.communication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonDeserializer;
import fr.univlr.debathon.job.db_project.jobclass.Comment;
import fr.univlr.debathon.job.db_project.jobclass.Question;
import fr.univlr.debathon.job.db_project.jobclass.Room;
import org.hildan.fxgson.FxGson;


import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.util.*;

public class AppCommunication implements Runnable {

    final Socket userSocket; // socket used by client to send and recieve data from server
    final BufferedReader in;   // object to read data from socket
    final PrintWriter out;     // object to write data into socket

    private ArrayList<Room> all_rooms = new ArrayList<Room>();
    private Room selected_room;
    private ArrayList<Question> questions_select_room = new ArrayList<Question>();

    public AppCommunication () throws IOException {
        userSocket = new Socket("127.0.0.1",9878);
        out = new PrintWriter(userSocket.getOutputStream());
        in = new BufferedReader(new InputStreamReader(userSocket.getInputStream()));

    }

    private <T> T getUnserialisation(String objects, Class<T> classT) {
        return FxGson.coreBuilder().registerTypeAdapter(
                        LocalDate.class,
                        (JsonDeserializer<LocalDate>) (json, type, jsonDeserializationContext) ->
                                LocalDate.parse(json.getAsJsonPrimitive().getAsString()))
                .enableComplexMapKeySerialization().create()
                .fromJson(objects, classT);
    }

    private void sendData (ObjectMapper objectMapper, ObjectNode root) throws JsonProcessingException {
        out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(root));
        out.flush();
    }





    private void analyseData(String data) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode dataJson = objectMapper.readTree(data);
        switch (dataJson.get("methods").asText()) {
            case "RESPONSE":
                methodsRESPONSE(dataJson, objectMapper);
                break;
            case "RESPONSEDETAILS":
                methodsRESPONSEDETAILS(dataJson);
                break;
            case "NEWCOMMENT":
                methodsNEWCOMMENT (dataJson);
                break;
            case "NEWQUESTION":
                methodsNEWQUESTION (dataJson);
                break;
            case "NEWROOM":
                methodsNEWROOM (dataJson);
                break;
        }
    }





    //Fonction call pour avoir les details des rooms de l'accueil
    private void methodsRESPONSE(JsonNode dataJson, ObjectMapper objectMapper) throws IOException {

        //Boucle affetant chaque salon dans la list de salon
        for(int i = 0;i<dataJson.get("rooms").size();i++){


            Room room = this.getUnserialisation(dataJson.get("rooms").get(i).toString(), Room.class);

            Debathon.getInstance().getDebates().add(room);
        }
    }

    //Fonction call pour avoir les details d'une room
    private void methodsRESPONSEDETAILS(JsonNode dataJson) throws IOException{

        selected_room = this.getUnserialisation(dataJson.get("room_selected").get(0).toString(), Room.class);

    }

    private void methodsNEWCOMMENT(JsonNode dataJson) {
        Comment comment = this.getUnserialisation(dataJson.get("new_comment").get(0).toString(), Comment.class);
        System.out.println(comment);
    }

    private void methodsNEWQUESTION (JsonNode dataJson) {
        Question question = this.getUnserialisation(dataJson.get("new_question").get(0).toString(), Question.class);
        System.out.println(question);
    }

    private void methodsNEWROOM (JsonNode dataJson) {
        Room room = this.getUnserialisation(dataJson.get("new_room").get(0).toString(), Room.class);
        System.out.println(room);
    }












    public void requestHome () throws JsonProcessingException {
        System.out.println("TEST ALL ROOMS IN DB");
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        all_rooms.clear();
        root.put("methods", "GET");
        root.put("request", "HOME");

        this.sendData(mapper, root);
    }


    //Fonction avec id en parametre pour recuperer info d'une room
    public void requestRoom(int id) throws  JsonProcessingException{
        System.out.println("TEST GET ROOM INFOS IN DB WITH ID = 1");
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();

        //GET pour recup donnees au client
        root.put("methods","GET");
        //ROOM pour preciser la recup d'une room precise
        root.put("request","ROOM");
        //id pour preciser l'id de la room
        root.put("id",id);
        this.sendData(mapper, root);

    }







    private void ShowRoomsDetails(){ //Fonction test pour afficher les details de chaque salon
        for(Room room : all_rooms){
            System.out.println("=================");
            System.out.println(room.getLabel());
            System.out.println(room.getDescription());
            System.out.println("=================");
        }
    }

    private void ShowQuestionsAndComments(){ //Fonction test pour afficher les questions et commentaires d'une room
        for(Question q : questions_select_room){
            System.out.println("===== "+q.getId()+" - "+q.getLabel()+" =====");
            for(Comment com : q.getListComment()){
                ShowCommentDetails(com);
            }
        }
    }

    private void ShowCommentDetails(Comment c){ //Fonction test pour afficher les details d'un commentaire
        System.out.println("--- "+c.getId()+" ---");
        System.out.println(c.getComment());
        System.out.println("------");
    }





    @Override
    public void run() {

        String data;

        try {
            data = in.readLine();

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
            out.close();
            userSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
