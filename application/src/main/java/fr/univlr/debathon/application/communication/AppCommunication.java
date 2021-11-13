package fr.univlr.debathon.application.communication;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.univlr.debathon.job.db_project.jobclass.Comment;
import fr.univlr.debathon.job.db_project.jobclass.Question;
import fr.univlr.debathon.job.db_project.jobclass.Room;


import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
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


    public void requestHome () throws JsonProcessingException {
        System.out.println("TEST ALL ROOMS IN DB");
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode root = objectMapper.createObjectNode();
        all_rooms.clear();
        root.put("methods", "GET");
        root.put("request", "HOME");

        out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(root));
        out.flush();
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
        out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(root));
        out.flush();

    }


    public void useHome () {

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


    private void analyseData(String data) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode dataJson = objectMapper.readTree(data);
        switch (dataJson.get("methods").asText()) {
            case "RESPONSE":
                methodsRESPONSE(dataJson, objectMapper);
                break;
            case "RESPONSEDETAILS":
                methodsRESPONSEDETAILS(dataJson,objectMapper);
                break;
        }
    }

    //Fonction call pour avoir les details des rooms de l'accueil
    private void methodsRESPONSE(JsonNode dataJson, ObjectMapper objectMapper) throws IOException {

        //Boucle affetant chaque salon dans la list de salon
        for(int i = 0;i<dataJson.get("rooms").size();i++){
            Room room = objectMapper.readValue(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(dataJson.get("rooms").get(i)), Room.class);
            all_rooms.add(room);
        }
        ShowRoomsDetails();
    }

    //Fonction call pour avoir les details d'une room
    private void methodsRESPONSEDETAILS(JsonNode dataJson, ObjectMapper objectMapper) throws IOException{
        //Affecte la room select
        selected_room = objectMapper.readValue(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(dataJson.get("room_selected").get(0)),Room.class);
        //Boucle affectant chaque question de la room
        for(int i = 0;i<dataJson.get("questions_room").size();i++){
            questions_select_room.add(objectMapper.readValue(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(dataJson.get("questions_room").get(i)),Question.class));
        }
        //Affichage test des elements de la room
        ShowQuestionsAndComments();
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
}
