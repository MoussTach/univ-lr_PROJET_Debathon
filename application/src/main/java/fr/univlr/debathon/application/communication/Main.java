package fr.univlr.debathon.application.communication;

import fr.univlr.debathon.job.db_project.jobclass.Category;
import fr.univlr.debathon.job.db_project.jobclass.Mcq;
import fr.univlr.debathon.job.db_project.jobclass.Room;
import fr.univlr.debathon.job.db_project.jobclass.Tag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        AppCommunication comm = new AppCommunication();
        comm.start();

        comm.requestHome(); //TEST TO GET ALL ROOMS INFOS
        comm.requestKey();

        //comm.requestRoom(1); //TEST TO GET ROOM INFO WITH ID : 1


    }

}
