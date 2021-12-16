package fr.univlr.debathon.custom.remastered.mvvm.utils;

import de.saxsys.mvvmfx.utils.commands.Command;
import de.saxsys.mvvmfx.utils.commands.CommandBase;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.DoubleExpression;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

/**
 * A {@link de.saxsys.mvvmfx.utils.commands.CompositeCommand} with a new binding for the progress of the execution and the
 * possibility to get the current sire of the list of command.
 *
 * It's useful because you now know on a progressBar the % of command already executed
 */
public class CompositeCommand_Remastered extends CommandBase {

    private final ObservableList<Command> registeredCommands = FXCollections.synchronizedObservableList(FXCollections.observableArrayList());

    final ReadOnlyDoubleWrapper progress = new ReadOnlyDoubleWrapper();
    final DoubleProperty progress_execution = new SimpleDoubleProperty(0);


    /**
     * Creates a {@link CompositeCommand_Remastered} with given commands.
     *
     * @param commands
     *            to aggregate
     */
    public CompositeCommand_Remastered(Command... commands) {
        initRegisteredCommandsListener();

        this.registeredCommands.addAll(commands);
    }

    /**
     * Registers a new {@link Command} for aggregation.
     *
     * @param command
     *            to register
     */
    public void register(Command command) {
        registeredCommands.add(command);
    }

    /**
     * Unregisters a {@link Command} from aggregation.
     *
     * @param command
     *            to unregister
     */
    public void unregister(Command command) {
        registeredCommands.remove(command);
    }

    private void initRegisteredCommandsListener() {
        this.registeredCommands.addListener((ListChangeListener<Command>) c -> {
            while (c.next()) {
                if (registeredCommands.isEmpty()) {
                    executable.unbind();
                    running.unbind();
                    progress.unbind();
                } else {
                    BooleanBinding executableBinding = constantOf(true);
                    BooleanBinding runningBinding = constantOf(false);

                    for (Command registeredCommand : registeredCommands) {
                        ReadOnlyBooleanProperty currentExecutable = registeredCommand.executableProperty();
                        ReadOnlyBooleanProperty currentRunning = registeredCommand.runningProperty();
                        executableBinding = executableBinding.and(currentExecutable);
                        runningBinding = runningBinding.or(currentRunning);
                    }
                    executable.bind(executableBinding);
                    running.bind(runningBinding);

                    initProgressBinding();
                }
            }
        });
    }

    private void initProgressBinding() {
        DoubleExpression tmp = constantOf(0);

        for (Command command : registeredCommands) {
            final ReadOnlyDoubleProperty progressProperty = command.progressProperty();

            /*
             * When the progress of a command is "undefined", the progress property has a value of -1.
             * But in our use case we like to have a value of 0 in this case.
             * Therefore we create a custom binding here.
             */
            final DoubleBinding normalizedProgress = Bindings
                    .createDoubleBinding(() -> (progressProperty.get() == -1) ? 0.0 : progressProperty.get(),
                            progressProperty);

            tmp = tmp.add(normalizedProgress);
        }

        int divisor = registeredCommands.isEmpty() ? 1 : registeredCommands.size();
        progress.bind(Bindings.divide(tmp, divisor));
    }

    @Override
    public void execute() {
        if (!isExecutable()) {
            throw new RuntimeException("Not executable");
        } else {
            if (!registeredCommands.isEmpty()) {

                double i = 0;

                for (Command current_command : registeredCommands) {

                    current_command.execute();
                    progress_execution.set(((i++ + 1) / getSize()));
                }
            }
        }
    }

    public void reinitialize() {
        progress_execution.set(0);
    }

    @Override
    public double getProgress() {
        return progressProperty().get();
    }

    public int getSize() {
        return registeredCommands.size();
    }

    @Override
    public ReadOnlyDoubleProperty progressProperty() {
        return progress;
    }

    public ReadOnlyDoubleProperty progressExecutionProperty() {
        return progress_execution;
    }

    private BooleanBinding constantOf(boolean defaultValue) {
        return new BooleanBinding() {
            @Override
            protected boolean computeValue() {
                return defaultValue;
            }
        };
    }

    @SuppressWarnings("SameParameterValue")
    private DoubleBinding constantOf(double defaultValue) {
        return new DoubleBinding() {
            @Override
            protected double computeValue() {
                return defaultValue;
            }
        };
    }

}