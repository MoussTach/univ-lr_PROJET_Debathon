package fr.univlr.debathon.taskmanager;

import javafx.concurrent.Task;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Array created to follow a schema of thread application.
 * When you create a {@link TaskArray}, you have to specify the type of task this array take.
 *
 * {@link ExecutionType} :
 *          - {@link ExecutionType#PARALLEL}
 *              Used when you want that all thread of this node execute at the same time.
 *
 *          - {@link ExecutionType#SEQUENTIAL}
 *              Used when you want that each thread of this node execute only when the last thread is finished.
 *
 *          - {@link ExecutionType#END}
 *              Used when you don't want to create a other node, only specify a new task for the current node.
 *
 * It always the node the most deep that are executed in first.
 *
 * @author Gaetan Brenckle
 * @param <T> - extends {@link ThreadFunctions}
 */
public class TaskArray<T extends ThreadFunctions> implements ThreadArray<T> {

    private int level_ = 0;

    private final ExecutionType type_;
    private final ConcurrentLinkedQueue<Pair<T, ThreadArray>> task_linked_ = new ConcurrentLinkedQueue<>();

    /**
     * Default Constructor
     * Specify a {@link ExecutionType} to know what this array is for.
     *
     * @author Gaetan Brenckle
     *
     * @param type - {@link ExecutionType} - type of execution wanted.
     */
    public TaskArray(ExecutionType type) {
        this.type_ = type;
    }


    @Override
    public List<Task> makeList() {
        List<Task> retList = new ArrayList<>();

        for (Pair<T, ThreadArray> pair : task_linked_) {
            Task ctask = pair.getKey().getTask();

            if (ctask != null) {
                retList.add(ctask);
            }
        }

        for (Pair<T, ThreadArray> pair : task_linked_) {
            retList.addAll(pair.getValue().makeList());
        }

        return retList;
    }

    @Override
    public void execute(ScheduledExecutorService executorService_parallel) throws InterruptedException {

        switch (type_) {
            case PARALLEL:

                CountDownLatch latchParallel = new CountDownLatch(task_linked_.size());
                for (Pair<T, ThreadArray> pair : task_linked_) {
                    pair.getValue().execute(executorService_parallel);
                }

                for (Pair<T, ThreadArray> pair : task_linked_) {

                    T task_called = pair.getKey();
                    task_called.setLatch(latchParallel);

                    task_called.executeTask(executorService_parallel);
                }
                latchParallel.await();
                break;

            case SEQUENTIAL:

                for (Pair<T, ThreadArray> pair : task_linked_) {
                    CountDownLatch latchSequential = new CountDownLatch(1);
                    pair.getValue().execute(executorService_parallel);

                    T task_called = pair.getKey();
                    task_called.setLatch(latchSequential);

                    task_called.executeTask(executorService_parallel);
                    latchSequential.await();
                }
                break;
        }
    }

    @Override
    public TaskArray addTask(Pair<T, ThreadArray> pair) {
        this.task_linked_.add(pair);
        return this;
    }


    @Override
    public TaskArray setLevel(int level_) {
        this.level_ = level_;
        return this;
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        int numExecution = 0;
        final int[] numTask = {0};

        builder.append(String.format("⤷ [%s] [%d] [nb Task:%d]", type_, level_, task_linked_.size()));
        task_linked_.forEach(task_customTaskArrayPair -> {
            builder.append("\n");
            builder.append("\t".repeat(Math.max(0, level_ + 1)));
            builder.append(String.format("[%s]%s", task_customTaskArrayPair.getKey(), (numTask[0]++ >= task_linked_.size()) ? "": ", "));
            builder.append("\t".repeat(Math.max(0, level_ + 1)));
            builder.append(task_customTaskArrayPair.getValue());
        });
        return builder.toString();
    }
}
