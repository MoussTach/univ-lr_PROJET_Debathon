package fr.univlr.debathon.application.taskmanager;

import javafx.concurrent.Task;
import javafx.util.Pair;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Interface needed when a array want to be compliant with the TaskManager.
 *
 * @author Gaetan Brenckle
 *
 * @param <T> - extends {@link ThreadFunctions} - it a thread designed to be compliant with a taskManager
 */
public interface ThreadArray<T extends ThreadFunctions> {

    enum ExecutionType {
        SEQUENTIAL,
        PARALLEL,
        END
    }

    /**
     * Add a new task on the tree
     *
     * @author Gaetan Brenckle
     *
     * @param pair - {@link Pair} reference of a task and a array, to continue the tree
     * @return - {@link ThreadArray} - builder pattern
     */
    ThreadArray addTask(Pair<T, ThreadArray> pair);

    /**
     * Make a list of all task that compose the tree.
     * This method is called recursively.
     *
     * @author Gaetan Brenckle
     *
     * @return - {@link List}
     */
    List<Task> makeList();

    /**
     * Execute the tree with a different approch depend of the type of execution ({@link ExecutionType}).
     * This method use a latch on each case to contain each line of the tree.
     *
     * @author Gaetan Brenckle
     *
     * @param executorService_parallel - {@link ScheduledExecutorService} - the Executor used, to limit the usage of threading
     * @throws InterruptedException - {@link InterruptedException} - continue the throwing of the exception
     */
    void execute(ScheduledExecutorService executorService_parallel) throws InterruptedException;


    /**
     * Method used to specify the level of the tree, to print the tree with the toString method with a number predetermined of \t.
     *
     * @param level_ - int - level of the tree
     * @return - {@link ThreadArray} - builder pattern
     */
    ThreadArray setLevel(int level_);
}
