package fr.univlr.debathon.application.taskmanager;

import javafx.beans.property.BooleanProperty;
import javafx.concurrent.Task;
import javafx.scene.image.Image;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Interface needed when a task want to be compliant with the TaskManager.
 *
 * @author Gaetan Brenckle
 *
 * @param <T> - the type of return wanted, for further application
 */
public interface ThreadFunctions<T> {

    boolean executeTask(ScheduledExecutorService executor);

    /**
     * Show a image stocked that represent the task for the taskManagerView
     *
     * @author Gaetan Brenckle
     *
     * @return - {@link Image} - the image wanted
     */
    Image getImage();

    /**
     * Getter for the task.
     * Used with {@link ThreadArray#makeList()} to be sure that the return is a task.
     * Created is you want to implement a service or something that is not directly a task.
     *
     * @author Gaetan Brenckle
     *
     * @return - {@link Task} - return the task of the class.
     */
    Task<?> getTask();


    /**
     * getter to know is the thread is retrying the execution or not.
     *
     * @author Gaetan Brenckle
     *
     * @return BooleanProperty - {@link BooleanProperty} - return the property
     */
    default BooleanProperty canRetryProperty() {
        return null;
    }


    /**
     * Setter of a latch, that is reduced when the task is finished.
     *
     * @author Gaetan Brenckle
     *
     * @param latch - {@link CountDownLatch} - set the latch used by the task
     */
    void setLatch(CountDownLatch latch);
}
