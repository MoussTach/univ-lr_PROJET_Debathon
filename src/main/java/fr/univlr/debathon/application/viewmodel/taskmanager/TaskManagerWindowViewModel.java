package fr.univlr.debathon.application.viewmodel.taskmanager;

import fr.univlr.debathon.application.viewmodel.ViewModel_SceneCycle;
import fr.univlr.debathon.log.generate.CustomLogger;

/**
 * ViewModel of the view {@link fr.univlr.debathon.application.view.taskmanager.TaskManagerWindowView}.
 * This ViewModel process the change of the taskManagerView
 *
 * @author Gaetan Brenckle
 */
public class TaskManagerWindowViewModel extends ViewModel_SceneCycle {

    private static final CustomLogger LOGGER = CustomLogger.create(TaskManagerWindowViewModel.class.getName());

    /**
     * Default Constructor
     *
     * @author Gaetan Brenckle
     */
    public TaskManagerWindowViewModel() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][constructor] Creation of the TaskManagerWindowViewModel() object.");
        }
    }


    @Override
    public void onViewAdded_Cycle() {
    }

    @Override
    public void onViewRemoved_Cycle() {
    }
}
