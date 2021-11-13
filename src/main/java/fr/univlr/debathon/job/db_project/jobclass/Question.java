package job.db_project.jobclass;

import java.util.List;

public class Question {
    
    private int id;

    private String label;
    private String context;
    private String type;

    private boolean is_active;

    private Room room;

    private List<Comment> listComment;
    private User user;


    public Question(int id, String label, String context, String type, boolean is_active, Room room, List<Comment> listComment, User user) {
        this.id = id;
        this.label = label;
        this.context = context;
        this.type = type;
        this.is_active = is_active;
        this.room = room;
        this.listComment = listComment;
        this.user = user;
    }

    public Question(Question clone) {
        this.id = clone.getId();
        this.label = clone.getLabel();
        this.context = clone.getContext();
        this.type = clone.getType();
        this.is_active = clone.getIs_active();
        this.room = clone.getRoom();
        this.listComment = clone.getListComment();
        this.user = clone.getUser();
    }



    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getContext() {
        return this.context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getType () {
    	return this.type;
    }
    
    public void setType (String type) {
    	this.type = type;
    }
    
    public boolean isIs_active() {
        return this.is_active;
    }

    public boolean getIs_active() {
        return this.is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    public Room getRoom() {
        return this.room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public List<Comment> getListComment() {
        return this.listComment;
    }

    public void setListComment(List<Comment> listComment) {
        this.listComment = listComment;
    }

    public void addCommentaire (Comment commentaire) {
        this.listComment.add(commentaire);
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }



}
