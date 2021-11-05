package fr.univlr.debathon.application.taskmanager;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.image.Image;

import java.lang.reflect.Field;
import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Custom Service to permit to create a service with a image to show them on a taskProgressManagerView
 * This class contain a lot of code used with the TaskManager to work.
 *
 * @author Gaetan Brenckle
 *
 * @param <T> The type of return wanted.
 */
public abstract class Service_Custom<T> extends Service<T> implements ThreadFunctions<T> {

    private Task_Custom currentTask = null;

    private CountDownLatch latch = null;
    private Duration delayUntilStart_ = Duration.ZERO;

    private final Image image_;

    private Boolean untilWorks_ = false;
    private Duration stepDurationOnFail_ = Duration.ofSeconds(5);
    private Duration maximumDurationOnFail_ = Duration.ofMinutes(5);


    /**
     * Default Constructor with a Image on parameter, for the TaskProgressManagerView.
     *
     * @author Gaetan Brenckle
     *
     * @param image - {@link Image} - a image than will be showed with a TaskProgressManagerView
     */
    protected Service_Custom(Image image) {
        this.image_ = image;
    }

    /**
     * Image on parameter, for the TaskProgressManagerView.
     * the Boolean is used to know if the task need to be restarted when it fail (for connection).
     *
     * @author Gaetan Brenckle
     *
     * @param image - {@link Image} - a image than will be showed with a TaskProgressManagerView
     * @param untilWork - boolean - used to know if the task need to be restarted when it fail (for connection)
     */
    protected Service_Custom(Image image, boolean untilWork) {
        this.image_ = image;
        this.untilWorks_ = untilWork;
    }


    /**
     * Set the delay before to wait before start the task.
     *
     * Default to 0sec.
     *
     * @param delayUntilStart_ - {@link Duration} - duration to set.
     * @return - {@link Service_Custom} - builder pattern.
     */
    public Service_Custom setDelayUntilStart(Duration delayUntilStart_) {
        this.delayUntilStart_ = delayUntilStart_;
        return this;
    }

    /**
     * Set the step of duration that increase each time when the task continue to fail.
     *
     * Default to 5sec.
     *
     * @param stepDurationOnFail_ - {@link Duration} - duration to set.
     * @return - {@link Service_Custom} - builder pattern.
     */
    public Service_Custom setStepDurationOnFail(Duration stepDurationOnFail_) {
        this.stepDurationOnFail_ = stepDurationOnFail_;
        return this;
    }

    /**
     * Set the maximum duration when the task continue to fail.
     *
     * Default to 5min.
     *
     * @param maximumDurationOnFail_ - {@link Duration} - duration to set.
     * @return - {@link Service_Custom} - builder pattern.
     */
    public Service_Custom setMaximumDurationOnFail(Duration maximumDurationOnFail_) {
        this.maximumDurationOnFail_ = maximumDurationOnFail_;

        return this;
    }


    @Override
    public boolean executeTask(ScheduledExecutorService executor) {
        Platform.runLater(() -> {
            this.setOnCancelled(event -> this.untilWorks_ = false);
            this.setExecutor(executor);
            this.restart();
        });
        return true;
    }

    @Override
    protected Task<T> createTask() {
        this.currentTask = call_Task();

        this.currentTask.setLatch(latch);
        this.currentTask.setDelayUntilStart(this.delayUntilStart_);
        this.currentTask.setMaximumDurationOnFail(this.maximumDurationOnFail_);
        this.currentTask.setStepDurationOnFail(this.stepDurationOnFail_);

        if (untilWorks_) {
            try {
                final Field untilWorksField = Task_Custom.class.getDeclaredField("untilWorks_");
                untilWorksField.setAccessible(true);
                untilWorksField.set(this.currentTask, Boolean.TRUE);

            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.fillInStackTrace();
            }
        }

        return this.currentTask;
    }


    @Override
    public Image getImage() {
        return image_;
    }

    @Override
    public Task getTask() {
        return this.currentTask;
    }


    @Override
    public void setLatch(CountDownLatch latch) {
        this.latch = latch;
    }


    @Override
    public String toString() {
        return String.format("[Service_Custom %s]", super.toString());
    }


    /** this method is a encapsulation of the classic call method.
     *               USE THIS INSTEAD OF CALL
     * the classic call method will call this method after some necessary check for the taskManager to work.
     *
     * return the same type as defined with the creation of the task.
     *
     * @author Gaetan Brenckle
     *
     * @return - {@link Task_Custom} - return a task with the type specified
     */
    protected abstract Task_Custom<T> call_Task();
}
