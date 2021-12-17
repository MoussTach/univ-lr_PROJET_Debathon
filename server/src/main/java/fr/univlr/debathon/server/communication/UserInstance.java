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
import fr.univlr.debathon.log.generate.CustomLogger;
import fr.univlr.debathon.server.pdf.PDFGenerator;
import fr.univlr.debathon.server.pdf.PDFdata;
import org.hildan.fxgson.FxGson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserInstance extends Thread implements Runnable {

    private static final CustomLogger LOGGER = CustomLogger.create(UserInstance.class.getName());

    public  final Socket socket; // socket
    public BufferedReader in; // buffer ou les données arrivent
    public PrintWriter out; // buffer depuis lequel les données sont envoyés
    private int whereIam = -1; // salon dans lequel se trouve l'utilisateur -1 pour l'accueil

    private final ObjectMapper objectMapper; // objet qui permet de de mapper pour les json


    /**
     * Constructeur prenant en parametre une connexion
     * @param userSocket socket
     * @throws IOException execption
     */
    public UserInstance(Socket userSocket) throws IOException {

        this.socket = userSocket;
        in = new BufferedReader(new InputStreamReader(userSocket.getInputStream()));
        out = new PrintWriter(userSocket.getOutputStream());
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Cette fonction permet de transformer un objet string json en objet T
     * @param objects l'objet
     * @param classT la classe
     * @param <T> le type de l'objet
     * @return un objet
     */
    private <T> T getUnserialisation(String objects, Class<T> classT) {
        return FxGson.coreBuilder().registerTypeAdapter(
                        LocalDate.class,
                        (JsonDeserializer<LocalDate>) (json, type, jsonDeserializationContext) ->
                                LocalDate.parse(json.getAsJsonPrimitive().getAsString()))
                .enableComplexMapKeySerialization().create()
                .fromJson(objects, classT);

    }

    /**
     *
     * @return l'id du salon dans lequel est l'utilisateur
     */
    public int getWhereIam () {
        return this.whereIam;
    }


    /**
     * Envoie les données à l'application
     * @param root les données
     * @throws JsonProcessingException exeption
     */
    public void sendData (ObjectNode root) throws JsonProcessingException {
        out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(root));
        out.flush();
    }


    /**
     * Analyse les données reçu pour les rediriger à la bonne fonction
     * @param data les données à analyser
     * @throws JsonProcessingException exeption
     * @throws SQLException exception
     */
    public void analyseData (String data) throws JsonProcessingException, SQLException {
        LOGGER.info("ANALYSEDATE : " + data);
        Map dataJson = objectMapper.readValue(data, new TypeReference<>() {});

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
            case "END":
                methodsEND (dataJson);
                break;
            case "DELETE":
                methodsDELETE (dataJson);
                break;
        }

    }

    /**
     * Sous dirigueur
     * @param dataJson des données
     * @throws JsonProcessingException exception
     */
    private void methodsEND(Map dataJson) throws JsonProcessingException {

        switch ((String) dataJson.get("request")) {
            case "DEBATE":
                this.caseEndDebate (dataJson);
                break;

        }

    }

    /**
     * Sous dirigeur
      * @param dataJson des données
     * @throws JsonProcessingException exception
     * @throws SQLException execption
     */
    private void methodsDELETE(Map dataJson) throws JsonProcessingException, SQLException {

        switch ((String) dataJson.get("request")) {
            case "QUESTION":
                this.caseDeleteQuestion (dataJson);
                break;

        }

    }

    /**
     * Sous dirigeur
     * @param dataJson
     * @param data
     * @throws JsonProcessingException
     */
    private void methodsMAIL(Map dataJson, String data) throws JsonProcessingException {

        switch ((String) dataJson.get("request")) {
            case "NEW":
                this.caseNEW_MAIL(data);
                break;

        }

    }

    /**
     * Sous dirigeur
     * @param dataJson
     * @param data
     * @throws SQLException
     * @throws JsonProcessingException
     */
    private void methodsUPDATE (Map dataJson, String data) throws SQLException, JsonProcessingException {

        switch ((String) dataJson.get("request")) {
            case "HOME":
                // this.caseHOME(data);
                break;
            case "COMMENT": //Cas ROOM souhaite
                this.caseUpdateLike (data);
                break;
            case "MCQ": //Cas ROOM souhaite
                switch ((String) dataJson.get("type")) {
                    case "VOTE":
                        this.caseUpdateVoteMCQ(data);
                        break;
                }

                break;
        }

    }


    /**
     * Sous dirigeur
     * @param dataJson
     * @param data
     * @throws SQLException
     * @throws JsonProcessingException
     */
    public void methodsINSERT (Map dataJson, String data) throws SQLException, JsonProcessingException {
        switch ((String) dataJson.get("request")) {
            case "ROOM": //Cas ROOM souhaite
                this.caseInsertROOM(data);
                break;
            case "QUESTION": //Cas ROOM souhaite
                this.caseInsertQUESTION(data);
                break;
            case "COMMENT": //Cas ROOM souhaite
                this.caseInsertCOMMENT(data);
                break;
        }
    }

    /**
     * Sous dirigeur
     * @param data
     * @throws SQLException
     * @throws JsonProcessingException
     */
    public void methodsGET(Map data) throws SQLException, JsonProcessingException {
        switch ((String) data.get("request")) {
            case "HOME":
                this.caseGetHOME(data);
                break;
            case "ROOM": //Cas ROOM souhaite
                this.caseGetROOM(data);
                break;
            case "KEY_HOME": //Cas ROOM souhaite
                this.caseGetKEYHOME();
                break;
        }
    }


    // CASE END

    /**
     * Fonction utiliser pour fermer un débat puis générer l pdf et l'envoie du mail
     * @param data
     * @throws JsonProcessingException
     */
    private void caseEndDebate(Map data) throws JsonProcessingException {

        int id_debate = (int) data.get("id_debate"); // recupère l'id du debat
        RoomDAO roomDAO = new RoomDAO(Server.CONNECTION); // creation de l'objet dao
        roomDAO.endDebate(id_debate); // update en base de données
        PDFGenerator.getInstance().requestPDF(id_debate); // appel pour la création du pdf et l'envoie du mail


        // Préparation du message envoyé aux apps pour dire que le débat est fini
        ObjectNode root = objectMapper.createObjectNode();
        root.put("methods", "ENDDEBATE");
        root.put("id_debate", id_debate);

        for (UserInstance ui : Server.USERINSTANCELIST) {
            if (ui != null)
                ui.sendData(root);
        }

    }

    /**
     * Supprime une question
     * @param data
     * @throws JsonProcessingException
     * @throws SQLException
     */
    private void caseDeleteQuestion (Map data) throws JsonProcessingException, SQLException {

        int id_question = (int) data.get("id_question");
        QuestionDAO questionDAO = new QuestionDAO(Server.CONNECTION);
        McqDAO mcqDAO = new McqDAO(Server.CONNECTION);

        Question question = questionDAO.select(id_question);
        questionDAO.delete(question);
        List<Mcq> list = mcqDAO.selectMcqByIdQuestion(id_question);

        for (Mcq mcq : list)
            mcqDAO.delete(mcq);

        // Préparer le message envoyé aux apps pour enlever la question
        ObjectNode root = objectMapper.createObjectNode();
        root.put("methods", "DELETEQUESTION");
        root.put("id_question", id_question);
        root.put("id_room", question.getRoom().getId());

        for (UserInstance ui : Server.USERINSTANCELIST) {
            if (ui != null)
                ui.sendData(root);
        }

    }


    // CASE GET

    /**
     * Retourne tous les salons ouvert
     * @param data
     */
    private void caseGetHOME(Map data)  {
        this.whereIam = -1;
        try {
            RoomDAO roomDAO = new RoomDAO(Server.CONNECTION);
            List<Room> list = roomDAO.selectAll();
            List<Room> listDelete = new ArrayList<>();

            for (Room room : list) {
                if (!room.isIs_open())
                    listDelete.add(room);
            }
            for (Room room : listDelete)
                list.remove(room);

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

    /**
     * Envoie les informations d'un salon
     * @param data
     * @throws SQLException
     * @throws JsonProcessingException
     */
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


        this.sendData(rootRoom);
    }

    /**
     * Appelé au démarage de l'app pour recevoir le code et le user
     * @throws JsonProcessingException
     */
    private void caseGetKEYHOME() throws JsonProcessingException {

        ObjectNode root = objectMapper.createObjectNode();
        root.put("methods", "KEY_HOME");
        root.put("key", Server.CREATERIGHTS_KEY);
        ArrayNode c = root.putArray("user");
        c.addPOJO(Server.getUser());
        
        this.sendData(root);

    }



    // CASE INSERT

    /**
     * Fonction pour ajouter un nouveau salon et l'envoyé aux apps
     * @param data
     * @throws JsonProcessingException
     * @throws SQLException
     */
    private void caseInsertROOM(String data) throws JsonProcessingException, SQLException {

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

        if (roomRes != null) {
            this.sendNewRoom(roomRes);
        }

    }

    /**
     * Fonction pour ajouter une nouvelle question et l'envoyé aux apps
     * @param data
     * @throws JsonProcessingException
     * @throws SQLException
     */
    private void caseInsertQUESTION(String data) throws JsonProcessingException, SQLException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode dataJson = objectMapper.readTree(data);

        QuestionDAO questionDAO = new QuestionDAO(Server.CONNECTION);
        McqDAO mcqDAO = new McqDAO(Server.CONNECTION);

        Question question = this.getUnserialisation(dataJson.get("new_question").get(0).toString(), Question.class);

        int id = questionDAO.insertAndGetId(question);
        Question q = questionDAO.select(id);

        for (Mcq mcq : question.getListMcq()) {
            mcq.setId_question(q.getId());
            Mcq r = mcqDAO.insertAndGetId(mcq);
            q.addMcq(r);
        }

        if (q != null) {
            this.sendNewQuestion(q);
        }

    }

    /**
     * Fonction pour ajouter un nouveau commentaire et l'envoyé aux apps
     * @param data
     * @throws JsonProcessingException
     * @throws SQLException
     */
    private void caseInsertCOMMENT(String data) throws JsonProcessingException, SQLException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode dataJson = objectMapper.readTree(data);

        CommentDAO commentDAO = new CommentDAO(Server.CONNECTION);

        Comment comment = this.getUnserialisation(dataJson.get("new_comment").get(0).toString(), Comment.class);
        int id = commentDAO.insertAndGetId(comment);

        Comment c = commentDAO.select(id);

        if (c != null) {
            this.sendNewComment(c);
        }

    }


    // CASE UPDATE

    /**
     * Ajoute le vote au choix MCQ
     * @param data
     * @throws JsonProcessingException
     * @throws SQLException
     */
    private void caseUpdateVoteMCQ(String data) throws JsonProcessingException, SQLException {

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode dataJson = objectMapper.readTree(data);

        int id = dataJson.get("id").asInt();

        McqDAO mcqDAO = new McqDAO(Server.CONNECTION);
        mcqDAO.updateNewLike(id);

    }

    /**
     * Ajoute un like ou dislike sur un commentaire
     * @param data
     * @throws JsonProcessingException
     * @throws SQLException
     */
    private void caseUpdateLike (String data) throws JsonProcessingException, SQLException {

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode dataJson = objectMapper.readTree(data);

        int id = dataJson.get("id").asInt();
        boolean positif = dataJson.get("positif").asBoolean();

        CommentDAO commentDAO = new CommentDAO(Server.CONNECTION);
        if (positif) { // positif veut dire like et négatif dislike
            commentDAO.updateLike(id);
        } else {
            commentDAO.updateDislike(id);
        }

    }


    // CASE MAIL

    /**
     * Enregistrer en base de données le mail d'un utilisateur souhaitant etre notifier du compte rendu
     * @param data
     * @throws JsonProcessingException
     */
    private void caseNEW_MAIL (String data) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode dataJson = objectMapper.readTree(data);

        int id = dataJson.get("id").asInt();
        String email = dataJson.get("email").asText();

        PDFdata.insertNewEmail(id, email);

    }


    /**
     * Permet de créer des ObjetNode pour l'envoie de donnée Json
     * @param methods
     * @param propretyName
     * @param object
     * @param <T>
     * @return
     */
    private <T> ObjectNode getObjetNode (String methods, String propretyName, T object) {
        ObjectNode root = objectMapper.createObjectNode();

        root.put("methods",methods);

        ArrayNode c = root.putArray(propretyName);
        c.addPOJO(object);

        return root;
    }


    /**
     * Envoie le nouveau commentaire
     * @param comment
     * @throws JsonProcessingException
     */
    public void sendNewComment (Comment comment) throws JsonProcessingException {
        for (UserInstance ui : Server.USERINSTANCELIST) {
            if (ui != null && ui.getWhereIam() == comment.getRoom().getId()) {
                ui.sendData(this.getObjetNode("NEWCOMMENT", "new_comment", comment));
            }
        }
    }

    /**
     * Envoie la nouvelle quesrtion
     * @param question
     * @throws JsonProcessingException
     */
    public void sendNewQuestion (Question question) throws JsonProcessingException {
        for (UserInstance ui : Server.USERINSTANCELIST) {
            if (ui != null && ui.getWhereIam() == question.getRoom().getId()) {
                ui.sendData(this.getObjetNode("NEWQUESTION", "new_question", question));
            }
        }
    }

    /**
     * Envoie le nouveau salon
     * @param room
     * @throws JsonProcessingException
     */
    public void sendNewRoom (Room room) throws JsonProcessingException {
        for (UserInstance ui : Server.USERINSTANCELIST) {
            if (ui != null && ui.getWhereIam() == -1) {
                ui.sendData(this.getObjetNode("NEWROOM", "new_room", room));
            }
        }
    }



    @Override
    /**
     * Demarage du Thread toujours en écoute de l'instance
     */
    public void run() {

        try {
            String data = in.readLine(); // initialisation

            StringBuilder dataReceveid = new StringBuilder(); //initialisation

            while(data!=null){ // tjours en écoute
                dataReceveid.append(data); // ajoute la donnée au String

                if (data.equals("}")) { // si c'est la fin du json
                    analyseData(dataReceveid.toString()); // envoie les données a l'analysateur
                    dataReceveid = new StringBuilder(); // reinitialisation
                }

                data = in.readLine(); // attend de recevoir des données
            }
            Server.USERINSTANCELIST.remove(this); // s'enleve de la liste des utilisateurs actif
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}
