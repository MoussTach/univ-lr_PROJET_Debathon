package fr.univlr.debathon.server.communication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserManager {

    private static UserManager instance;
    private Map<Integer, List<UserInstance>> mapUser;


    public static UserManager getInstance () {
        if (instance == null)
            instance = new UserManager();
        return instance;
    }

    private UserManager () {
        this.mapUser = new HashMap<>();
        this.mapUser.put(-1, new ArrayList<>()); // -1 est "l'id" de l'accueil
    }


    public void addRooms (List<Integer> listId) {
        for (Integer id : listId) {
            if (!this.mapUser.containsKey(id))
                this.mapUser.put(id, new ArrayList<>());
        }
    }

    public void addRoom (int id) {
        if (!this.mapUser.containsKey(id))
            this.mapUser.put(id, new ArrayList<>());
    }

    public void removeRoom (int id) {
        if (this.mapUser.containsKey(id))
            this.mapUser.remove(id);
    }

    public void setRoomUser (UserInstance ui, int new_id, int last_id) {
        this.revomeUser(ui, last_id);

        this.addUser(ui, new_id);
    }

    public void revomeUser (UserInstance ui, int id) {
        if (this.mapUser.get(id).contains(ui))
            this.mapUser.get(id).remove(ui);
    }

    public void addUser (UserInstance ui, int id) {

        if (!this.mapUser.get(id).contains(ui))
            this.mapUser.get(id).add(ui);

    }

    public List<UserInstance> getIpList (int id) {
        return this.mapUser.get(id);
    }

}
