package fr.univlr.debathon.application.taskmanager;

import fr.univlr.debathon.log.generate.CustomLogger;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.concurrent.Task;
import org.controlsfx.control.TaskProgressView;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Task manager, create the {@link ExecutorService} and determine the number of thread of the application.
 * Use a builder pattern.
 *
 * @author Gaetan Brenckle
 */
public class TaskManager {

    private static final CustomLogger LOGGER = CustomLogger.create(TaskManager.class.getName());
    private static final int MAX_THREADS = 10;

    protected final ObjectProperty<ThreadArray<?>> currentTask = new SimpleObjectProperty<>(null);
    private final BooleanProperty isExecutedSomething_ = new SimpleBooleanProperty(false);

    protected final ConcurrentLinkedQueue<ThreadArray<?>> taskArrays = new ConcurrentLinkedQueue<>();
    protected final ConcurrentLinkedQueue<TaskProgressView<?>> listTaskManagerBinded_ = new ConcurrentLinkedQueue<>();
    protected final ConcurrentLinkedQueue<Task<?>> listTaskShowed_ = new ConcurrentLinkedQueue<>();
    private Task<Void> task_executeTasks = null;

    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(MAX_THREADS, runnable -> {
        Thread t = new Thread(runnable);
        t.setDaemon(true);
        return t;
    });

    /**
     * Default Constructor
     * Dont forget to call the method {@link TaskManager#setup()}
     *
     * @see TaskManager#setup()
     *
     * @author Gaetan Brenckle
     */
    public TaskManager() {
        isExecutedSomething_.bind(Bindings.createBooleanBinding(() -> currentTask.get() != null, currentTask));
    }

    /**
     * Setup need to be called to start the usage of the taskManager.
     * It will create a task that check each 30seconds and when he is notified, if a task need to be proceded on the queue.
     * After the node of the task is traited, it will clean and update the list of thread showed by the taskManager view.
     *
     * @author Gaetan Brenckle
     *
     */
    public void setup() {
        //Use real thread here to avoid a lock when a lot of root of thread are executed.
        if (task_executeTasks == null) {
            this.task_executeTasks = new Task<Void>() {
                @SuppressWarnings("InfiniteLoopStatement")
                @Override
                protected Void call() throws Exception {

                    do {
                        synchronized (taskArrays) {
                            taskArrays.wait(30000);
                            if (!taskArrays.isEmpty()) {

                                Task<Void> task_running = new Task<Void>() {
                                    @Override
                                    protected Void call() {

                                        while (taskArrays.size() > 0) {

                                            ThreadArray array = taskArrays.poll();
                                            currentTask.set(array);

                                            try {
                                                if (array != null) {
                                                    array.execute(executorService);
                                                }
                                            } catch (InterruptedException e) {
                                                if (LOGGER.isErrorEnabled()) {
                                                    LOGGER.error(String.format("Error when trying to run task: %s", e.getMessage()), e);
                                                }
                                            } finally {
                                                Platform.runLater(() -> {
                                                    synchronized (listTaskShowed_) {
                                                        synchronized (listTaskManagerBinded_) {
                                                            List<Task> arrayAfter = array.makeList();
                                                            listTaskShowed_.removeAll(arrayAfter);

                                                            for (Task task : listTaskShowed_) {
                                                                for (TaskProgressView taskProgressView : listTaskManagerBinded_) {
                                                                    if (task.isDone()) {
                                                                        listTaskShowed_.remove(task);
                                                                        taskProgressView.getTasks().remove(task);

                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                        currentTask.set(null);
                                        return null;
                                    }

                                    @Override
                                    protected void setException(Throwable t) {
                                        if (LOGGER.isErrorEnabled()) {
                                            LOGGER.error(String.format("[%s] Exception when running tasks : %s", "TaskManager", t.getMessage()), t);
                                        }
                                        super.setException(t);
                                    }
                                };

                                Thread t = new Thread(task_running);
                                t.setDaemon(true);
                                t.start();
                            }
                        }
                    } while (true);

                }

                @Override
                protected void setException(Throwable t) {
                    if (LOGGER.isErrorEnabled()) {
                        LOGGER.error(String.format("[%s] Exception when execute tasks : %s", "TaskManager", t.getMessage()), t);
                    }
                    super.setException(t);
                }
            };

            Thread t = new Thread(task_executeTasks);
            Runtime.getRuntime().addShutdownHook(t);
            t.setDaemon(true);
            t.start();
        }

    }

    /**
     * Add a Array of task to be traited.
     * Also decompose the array with a list of task to add them all on both a list of task showed and all the viewManager registred.
     *
     * @author Gaetan Brenckle
     *
     * @param array - {@link ThreadArray} - array of task that is wanted to be traited.
     * @return - {@link TaskManager} - builder pattern
     */
    public TaskManager addArray(ThreadArray array) {

        //Platform runlater used to avoid exception throwed by the TaskManagerView when modified.
        Platform.runLater(() -> {
            synchronized (taskArrays) {
                this.taskArrays.add(array);

                for (TaskProgressView view : listTaskManagerBinded_) {
                    List arrayTask = array.makeList();
                    listTaskShowed_.addAll(arrayTask);
                    view.getTasks().addAll(arrayTask);
                }

                this.taskArrays.notifyAll();
            }
        });
        return this;
    }

    /**
     * Register a taskManagerView.
     *
     * @author Gaetan Brenckle
     *
     * @param view - {@link TaskProgressView} - a taskManagerView
     */
    public void addTaskManager(TaskProgressView view) {
        view.getTasks().addAll(listTaskShowed_);
        listTaskManagerBinded_.add(view);
    }

    /**
     * Unregister a taskManagerView
     *
     * @author Gaetan Brenckle
     *
     * @param view - {@link TaskProgressView} - a taskManagerView
     */
    public void removeTaskManager(TaskProgressView view) {
        listTaskManagerBinded_.remove(view);
    }


    /**
     * Remove a task that is showed
     * This method is used to remove directly on the {@link ThreadArray} to a better visibility
     *
     * @param thread - {@link ThreadFunctions} - a task that implement the {@link ThreadFunctions} interface
     */
    @SuppressWarnings("SuspiciousMethodCalls")
    public void removeTaskShowed(ThreadFunctions thread) {
        listTaskShowed_.remove(thread);
    }


    /**
     * Property of the variable isExecutedSomething_.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link ReadOnlyBooleanProperty} - return the property of the variable isExecutedSomething_.
     */
    public ReadOnlyBooleanProperty isExecutedSomething_Property() {
        return isExecutedSomething_;
    }


    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();

        taskArrays.forEach(array -> builder.append(array.toString()));
        return builder.toString();
    }
}
