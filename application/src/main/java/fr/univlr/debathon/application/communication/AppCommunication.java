package fr.univlr.debathon.application.communication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonDeserializer;
import fr.univlr.debathon.job.db_project.jobclass.*;
import org.hildan.fxgson.FxGson;


import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.util.*;

public class AppCommunication extends Thread implements Runnable {

    final Socket userSocket; // socket used by client to send and recieve data from server
    final BufferedReader in;   // object to read data from socket
    final PrintWriter out;     // object to write data into socket

    private ArrayList<Room> all_rooms = new ArrayList<Room>();
    private Room selected_room;
    private ArrayList<Question> questions_select_room = new ArrayList<Question>();

    public AppCommunication () throws IOException {
        userSocket = new Socket("localhost",9878);
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

    public void sendData (ObjectMapper objectMapper, ObjectNode root) throws JsonProcessingException {
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
            case "NEWMCQ":
                methodsNEWMCQ (dataJson);
                break;
        }
    }




    //Fonction call pour avoir les details des rooms de l'accueil
    public void methodsRESPONSE(JsonNode dataJson, ObjectMapper objectMapper) throws IOException {

        //Boucle affetant chaque salon dans la list de salon
        for(int i = 0;i<dataJson.get("rooms").size();i++){
            Room room = this.getUnserialisation(dataJson.get("rooms").get(i).toString(), Room.class);
            System.out.println("---" + room);
            Debathon.getInstance().getDebates().add(room);
        }
    }

    //Fonction call pour avoir les details d'une room
    public void methodsRESPONSEDETAILS(JsonNode dataJson) throws IOException{
        Debathon.getInstance().setCurrent_debate(this.getUnserialisation(dataJson.get("room_selected").get(0).toString(), Room.class));

    }

    public void methodsNEWCOMMENT(JsonNode dataJson) throws IOException {
        Comment comment = this.getUnserialisation(dataJson.get("new_comment").get(0).toString(), Comment.class);


        for (Question question : Debathon.getInstance().getCurrent_debate().getListQuestion()) {
            if (question.getId() == comment.getQuestion().getId()) {
                question.addComment(comment);
            }
        }

    }

    public void methodsNEWQUESTION (JsonNode dataJson) throws IOException {
        Question question = this.getUnserialisation(dataJson.get("new_question").get(0).toString(), Question.class);

        Debathon.getInstance().getCurrent_debate().addQuestion(question);

    }

    public void methodsNEWROOM (JsonNode dataJson) throws IOException {
        Room room = this.getUnserialisation(dataJson.get("new_room").get(0).toString(), Room.class);

        Debathon.getInstance().getDebates().add(room);
        System.out.println(room);

    }

    public void methodsNEWMCQ(JsonNode dataJson) throws IOException {
        Mcq mcq = this.getUnserialisation(dataJson.get("new_mcq").get(0).toString(), Mcq.class);
        System.out.println(mcq);
        Debathon.getInstance().getMcq().add(mcq);
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

    public void requestInsertNewRoom (Room room) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();


        root.put("methods","INSERT");

        root.put("request","ROOM");

        ArrayNode c = root.putArray("new_room");
        c.addPOJO(room);
        this.sendData(mapper, root);

    }
    public void testRequestInsertNewRoom () throws JsonProcessingException {
        String key = AlphaNumericStringGenerator.getRandomString(6);
        Category category = new Category(1, "Catégorie");
        List<Tag> listTag = new ArrayList<>();
        listTag.add(new Tag(1, "Oui", "couleur"));
        listTag.add(new Tag(2, "Tag", "couelurur"));
        Room room = new Room("Salon de Julien", "Ceci est un nouveau salon", key,
                "mail@mail.mail", category, listTag);

        this.requestInsertNewRoom(room);
    }

    public void requestInsertNewQuestion (Question question) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();

        root.put("methods","INSERT");
        root.put("request","QUESTION");

        ArrayNode c = root.putArray("new_question");
        c.addPOJO(question);
        this.sendData(mapper, root);

    }
    public void testRequestInsertNewQuestion () throws JsonProcessingException {
        User user = new User(1, "User");
        Room room = new Room();
        room.setId(2);
        Question question = new Question("Comment ça va ?", "Il est 3h du mat et ca te casse les couilles",
                "unique", room, user);
        this.requestInsertNewQuestion(question);
    }

    public void requestInsertNewComment (Comment comment) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();

        root.put("methods","INSERT");
        root.put("request","COMMENT");

        ArrayNode c = root.putArray("new_comment");
        c.addPOJO(comment);
        this.sendData(mapper, root);
    }
    public void testRequestInsertNewComment () throws JsonProcessingException {
        Room room = new Room();
        room.setId(2);
        Question question = new Question();
        question.setId(6);

        Comment comment = new Comment("Ok m'en fous", null, question, room, new User(1, "Nom"));

        this.requestInsertNewComment(comment);
    }

    public void requestInsertNewMcq (Mcq mcq) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();

        root.put("methods","INSERT");
        root.put("request","MCQ");

        ArrayNode c = root.putArray("new_mcq");
        c.addPOJO(mcq);
        this.sendData(mapper, root);
    }
    public void testRequestInsertNewMcq () throws JsonProcessingException {

        Room room = new Room();
        room.setId(2);
        Question question = new Question();
        question.setId(6);

        Mcq mcq = new Mcq("Mcq texte", question, room);

        this.requestInsertNewMcq(mcq);

    }

    public void methodsUPDATE_VOTE_MCQ (int id_mcq) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();

        root.put("methods", "UPDATE");
        root.put("request", "MCQ");
        root.put("type", "VOTE");
        root.put("id", id_mcq);

        this.sendData(mapper, root);

    }

    public void methodsUPDATE_LIKE_COMMENT (int id_mcq, boolean positif) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();

        root.put("methods", "UPDATE");
        root.put("request", "MCQ");
        root.put("type", "VOTE");
        root.put("id", id_mcq);
        root.put("positif", positif);

        this.sendData(mapper, root);

    }



    public void uselessFonction () {
        System.out.println("I'm useless");
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
            //out.close();
            //userSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
