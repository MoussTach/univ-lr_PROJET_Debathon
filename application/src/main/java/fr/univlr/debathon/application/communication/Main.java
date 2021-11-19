package fr.univlr.debathon.application.communication;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        AppCommunication comm = new AppCommunication();
        comm.start();

        // comm.requestHome(); //TEST TO GET ALL ROOMS INFOS

        //comm.requestRoom(1); //TEST TO GET ROOM INFO WITH ID : 1
        comm.testRequestInsertNewRoom();
        /*comm.testRequestInsertNewQuestion();
        comm.testRequestInsertNewComment();
        comm.testRequestInsertNewMcq();*/
        int seconde = 2000;
        for (int i = 0; i < 30; i++) {
            Thread.sleep(seconde);
            comm.testRequestInsertNewRoom();
        }

        while (true) {

        }

    }

}
