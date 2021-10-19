package fr.univlr.debathon.application.taskmanager;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
import javafx.scene.image.Image;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Custom Task to permit to create a task with a image to show them on a taskProgressManagerView
 * This class contain a lot of code used with the TaskManager to work.
 *
 * @author Gaetan Brenckle
 *
 * @param <T> The type of return wanted.
 */
public abstract class Task_Custom<T> extends Task<T> implements ThreadFunctions<T> {

    private final Task currentTask = this;
    private CountDownLatch latch = null;
    private Duration delayUntilStart_ = Duration.ZERO;

    private Image image_;

    private final BooleanProperty untilWorks_ = new SimpleBooleanProperty(false);
    private final BooleanProperty canRetry_ = new SimpleBooleanProperty(false);
    private Duration stepDurationOnFail_ = Duration.ofSeconds(5);
    private Duration maximunDurationOnFail_ = Duration.ofMinutes(5);


    /**
     * Protected constructor, used to be called on service implementation.
     *
     * @author Gaetan Brenckle
     *
     * @param title - {@link String} - title of the task
     */
    protected Task_Custom(String title) {
        this.updateTitle(title);
    }

    /**
     * Default Constructor with a Image on parameter, for the TaskProgressManagerView.
     *
     * @author Gaetan Brenckle
     *
     * @param image - {@link Image} - a image than will be showed with a TaskProgressManagerView
     * @param title - {@link String} - title of the task
     */
    public Task_Custom(Image image, String title) {
        this.updateTitle(title);
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
     * @param title - {@link String} - title of the task
     */
    public Task_Custom(Image image, String title, boolean untilWork) {
        this(image, title);
        this.untilWorks_.set(untilWork);
    }

    public Task_Custom() {

    }

    /**
     * Retry the execution when the thread is waiting.
     *
     * @author Gaetan Brenckle
     */
    public void retryExecution() {
        synchronized (untilWorks_) {
            this.untilWorks_.notify();
        }
    }

    /**
     * Set the delay before to wait before start the task.
     *
     * Default to 0sec.
     *
     * @param delayUntilStart_ - {@link Duration} - duration to set.
     * @return - {@link Task_Custom} - builder pattern.
     */
    public Task_Custom setDelayUntilStart(Duration delayUntilStart_) {
        this.delayUntilStart_ = delayUntilStart_;
        return this;
    }

    /**
     * Set the step of duration that increase each time when the task continue to fail.
     *
     * Default to 5sec.
     *
     * @param stepDurationOnFail_ - {@link Duration} - duration to set.
     * @return - {@link Task_Custom} - builder pattern.
     */
    public Task_Custom setStepDurationOnFail(Duration stepDurationOnFail_) {
        this.stepDurationOnFail_ = stepDurationOnFail_;
        return this;
    }

    /**
     * Set the maximum duration when the task continue to fail.
     *
     * Default to 5min.
     *
     * @param maximunDurationOnFail_ - {@link Duration} - duration to set.
     * @return - {@link Task_Custom} - builder pattern.
     */
    public Task_Custom setMaximunDurationOnFail(Duration maximunDurationOnFail_) {
        this.maximunDurationOnFail_ = maximunDurationOnFail_;
        return this;
    }


    @Override
    public boolean executeTask(ScheduledExecutorService executor) {

        this.setOnCancelled(event -> this.untilWorks_.set(false));

        Platform.runLater(() -> executor.schedule(this, this.delayUntilStart_.toMillis(), TimeUnit.MILLISECONDS));
        return true;
    }

    @Override
    protected T call() throws Exception {
        Duration durationStep = this.stepDurationOnFail_;
        boolean alwaysRunning = untilWorks_.get();
        T retValue = null;

        try {
            do {
                try {
                    retValue = call_Task();
                    alwaysRunning = false;

                } catch (Exception e) {
                    System.out.println(String.format("[CUSTOM TASK][untilWorks=%b] EXCEPTION -> %s", untilWorks_.get(), e.getMessage()));
                    e.printStackTrace();
                    if (!untilWorks_.get()) {
                        throw e;
                    }

                    synchronized (untilWorks_) {
                        updateMessage(String.format("Retry after %d:%02d:%02d", durationStep.toHours(), durationStep.toMinutes(), durationStep.getSeconds() % 60));
                        canRetry_.set(true);
                        untilWorks_.wait(durationStep.toMillis());
                        canRetry_.set(false);
                        if (durationStep.compareTo(this.maximunDurationOnFail_) <= 0) {
                            durationStep = durationStep.plus(durationStep);
                        }
                    }
                }
            } while (alwaysRunning);

        } finally {
            if (latch != null) {
                latch.countDown();
            }
        }
        return retValue;
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
    public BooleanProperty canRetryProperty() {
        return this.canRetry_;
    }

    @Override
    public void setLatch(CountDownLatch latch) {
        this.latch = latch;
    }


    @Override
    public String toString() {
        return String.format("[Task_Custom %s]", super.toString());
    }


    /** this method is a encapsulation of the classic call method.
     *               USE THIS INSTEAD OF CALL
     * the classic call method will call this method after some necessary check for the taskManager to work.
     *
     * return the same type as defined with the creation of the task.
     *
     * @author Gaetan Brenckle
     *
     * @return - {@link T} - return the object specified
     * @throws Exception - {@link Exception}
     */
    protected abstract T call_Task() throws Exception;
}
