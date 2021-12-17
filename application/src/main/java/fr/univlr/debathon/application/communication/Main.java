package fr.univlr.debathon.application.communication;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        AppCommunication comm = new AppCommunication();
        comm.start();

        //comm.requestHome(); //TEST TO GET ALL ROOMS INFOS
        //comm.requestKey();

        //comm.requestRoom(1); //TEST TO GET ROOM INFO WITH ID : 1
        comm.requestEndDebate(1);

    }

}
