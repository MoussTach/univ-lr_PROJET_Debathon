package fr.univlr.debathon.application.communication;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        AppCommunication comm = new AppCommunication();
        Thread t = new Thread(comm);
        t.start();

        // comm.requestHome(); //TEST TO GET ALL ROOMS INFOS

        comm.requestRoom(1); //TEST TO GET ROOM INFO WITH ID : 1

        while (true) {

        }

    }

}
