package com.sun.javafx.scene.control.skin;

import fr.univlr.debathon.application.taskmanager.Task_Custom;
import javafx.beans.binding.Bindings;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.controlsfx.control.TaskProgressView;

/**
 * Custom skin for {@link TaskProgressView} that change the erase button to a retry button.
 *
 * @author Gaetan Brenckle
 * @param <T> - extends {@link Task}
 */
public class CustomTaskProgressViewSkin<T extends Task_Custom<?>> extends SkinBase<TaskProgressView<T>> {

    public CustomTaskProgressViewSkin(TaskProgressView<T> monitor) {
        super(monitor);
        BorderPane borderPane = new BorderPane();
        borderPane.getStyleClass().add("box");
        ListView<T> listView = new ListView();
        listView.setPrefSize(500.0D, 400.0D);
        listView.setPlaceholder(new Label("No tasks running"));
        listView.setCellFactory((param) -> new TaskCell());
        listView.setFocusTraversable(false);
        Bindings.bindContent(listView.getItems(), monitor.getTasks());
        borderPane.setCenter(listView);
        this.getChildren().add(listView);
    }

    class TaskCell extends ListCell<T> {
        private final ProgressBar progressBar;
        private final Label titleText = new Label();
        private final Label messageText;
        private final Button retryButton;

        private T task;
        private final BorderPane borderPane;

        public TaskCell() {
            this.titleText.getStyleClass().add("task-title");
            this.messageText = new Label();
            this.messageText.getStyleClass().add("task-message");

            this.progressBar = new ProgressBar();
            this.progressBar.setMaxWidth(1.7976931348623157E308D);
            this.progressBar.setMaxHeight(8.0D);
            this.progressBar.getStyleClass().add("task-progress-bar");

            retryButton = new Button("Retry");
            if (task != null) {
                retryButton.disableProperty().bind(task.canRetryProperty().not());
            } else {
                retryButton.setDisable(true);
            }
            retryButton.getStyleClass().add("task-cancel-button");
            retryButton.setTooltip(new Tooltip("Retry Task"));
            retryButton.setOnAction(evt -> {
                if (task != null) {
                    task.retryExecution();
                }
            });

            VBox vbox = new VBox();
            vbox.setSpacing(4.0D);
            vbox.getChildren().add(this.titleText);
            vbox.getChildren().add(this.progressBar);
            vbox.getChildren().add(this.messageText);

            BorderPane.setAlignment(retryButton, Pos.CENTER);
            BorderPane.setMargin(retryButton, new Insets(0, 0, 0, 4));

            this.borderPane = new BorderPane();
            this.borderPane.setCenter(vbox);
            borderPane.setRight(retryButton);
            this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }

        public void updateIndex(int index) {
            super.updateIndex(index);
            if (index == -1) {
                this.setGraphic((Node)null);
                this.getStyleClass().setAll("task-list-cell-empty");
            }

        }

        protected void updateItem(T task, boolean empty) {
            super.updateItem(task, empty);
            this.task = task;
            if (!empty && task != null) {
                if (task != null) {
                    this.getStyleClass().setAll("task-list-cell");
                    this.progressBar.progressProperty().bind(task.progressProperty());
                    this.titleText.textProperty().bind(task.titleProperty());
                    this.messageText.textProperty().bind(task.messageProperty());
                    this.retryButton.disableProperty().bind(task.canRetryProperty().not());

                    Callback<T, Node> factory = ((TaskProgressView) CustomTaskProgressViewSkin.this.getSkinnable()).getGraphicFactory();
                    if (factory != null) {
                        Node graphic = (Node)factory.call(task);
                        if (graphic != null) {
                            BorderPane.setAlignment(graphic, Pos.CENTER);
                            BorderPane.setMargin(graphic, new Insets(0.0D, 4.0D, 0.0D, 0.0D));
                            this.borderPane.setLeft(graphic);
                        }
                    } else {
                        this.borderPane.setLeft((Node)null);
                    }

                    this.setGraphic(this.borderPane);
                } else {
                    retryButton.setDisable(true);
                }
            } else {
                this.getStyleClass().setAll("task-list-cell-empty");
                this.setGraphic((Node)null);
            }

        }
    }
}


