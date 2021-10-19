package fr.univlr.debathon.application.taskmanager;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.image.Image;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Dummy task to create a ThreadArray without a task executed.
 * This class contain code used with the TaskManager to work.
 *
 * @author Gaetan Brenckle
 */
public class Task_Dummy extends Task<Void> implements ThreadFunctions<Void> {

    private final Task currentTask = null;
    private CountDownLatch latch = null;
    private Duration delayUntilStart_ = Duration.ZERO;

    private final Image image_ = null;

    /**
     * Default Constructor
     *
     * @author Gaetan Brenckle
     */
    public Task_Dummy() {
    }

    /**
     * Set the delay before to wait before start the task.
     *
     * Default to 0sec.
     *
     * @param delayUntilStart_ - {@link Duration} - duration to set.
     * @return - {@link Task_Dummy} - builder pattern.
     */
    public Task_Dummy setDelayUntilStart(Duration delayUntilStart_) {
        this.delayUntilStart_ = delayUntilStart_;
        return this;
    }


    @Override
    public boolean executeTask(ScheduledExecutorService executor) {
        Platform.runLater(() -> executor.schedule(this, this.delayUntilStart_.toMillis(), TimeUnit.MILLISECONDS));
        return true;
    }

    @Override
    protected Void call() {
        if (latch != null) {
            latch.countDown();
        }
        return null;
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
        return String.format("[Task_Dummy %s]", super.toString());
    }
}
