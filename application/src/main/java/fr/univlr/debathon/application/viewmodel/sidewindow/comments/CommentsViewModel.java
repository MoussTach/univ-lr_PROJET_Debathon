package fr.univlr.debathon.application.viewmodel.sidewindow.comments;

import fr.univlr.debathon.application.viewmodel.ViewModel_SceneCycle;
import fr.univlr.debathon.log.generate.CustomLogger;

public class CommentsViewModel extends ViewModel_SceneCycle {

    private static final CustomLogger LOGGER = CustomLogger.create(CommentsViewModel.class.getName());

    private final String lblCommentary_label;


    public CommentsViewModel(String commentary) {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][constructor] Creation of the CommentsViewModel() object.");
        }

        this.lblCommentary_label = commentary;
    }


    public String getLblCommentary_label() {
        return lblCommentary_label;
    }


    @Override
    public void onViewAdded_Cycle() {
    }

    @Override
    public void onViewRemoved_Cycle() {
    }
}
