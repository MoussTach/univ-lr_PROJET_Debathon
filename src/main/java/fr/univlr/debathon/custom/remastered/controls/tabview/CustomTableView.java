package fr.univlr.debathon.custom.remastered.controls.tabview;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Create a custom tableview, that can navigate between the cell and pass on each other after a commite value.
 *
 * @author Gaetan Brenckle
 *
 * @param <T> - the main object
 */
public class CustomTableView<T> extends TableView<T> {

    /**
     * Default constructor.
     * Bind eventHandler on keyEvent.
     *
     * @author Gaetan Brenckle
     */
    public CustomTableView() {

        this.addEventFilter(KeyEvent.KEY_PRESSED, eventOnKeyPressed());
        this.addEventFilter(KeyEvent.KEY_RELEASED, eventOnKeyReleased());

        // single cell selection mode
        this.getSelectionModel().setCellSelectionEnabled(true);
    }

    /**
     * Event on key released.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link EventHandler} - return a event when a key is realease.
     */
    private EventHandler<KeyEvent> eventOnKeyReleased() {
        return event -> {
            if (event.getCode() == KeyCode.ENTER) {

                // move focus & selection
                // we need to clear the current selection first or else the selection would be added to the current selection since we are in multi selection mode
                TablePosition pos = CustomTableView.this.getFocusModel().getFocusedCell();

                if (pos.getRow() == -1) {
                    CustomTableView.this.getSelectionModel().select(0);
                }
            }
        };
    }

    /**
     * Event on key pressed.
     *      ENTER : direct return the event (will always throw the event of the cell on key pressed)
     *      TAB   : commit the current cell and change to the next.
     *      RIGHT : change to the cell at the right of the current cell.
     *      LEFT  : change to the cell at the left of the current cell.
     *      UP    : change to the cell at the top of the current cell.
     *      DOWN  : change to the cell below of the current cell.
     *
     *      ANY KEY : focus the current cell.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link EventHandler} - return a event when a key is pressed.
     */
    @SuppressWarnings({"UnnecessaryReturnStatement"})
    private EventHandler<KeyEvent> eventOnKeyPressed() {
        return event -> {
            if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.CONTROL) {
                //event.consume(); // don't consume the event or else the values won't be updated;
                return;
            } else if (event.getCode() == KeyCode.TAB) {

                Platform.runLater(() -> {
                    CustomTableView.this.getSelectionModel().selectNext();

                    TablePosition focusedCellPosition = CustomTableView.this.getFocusModel().getFocusedCell();
                    int row = focusedCellPosition.getRow();
                    TableColumn column = focusedCellPosition.getTableColumn();
                    CustomTableView.this.edit(row, column);
                });
                //event.consume(); //TODO this here correct the problem of tabulation of the DateCell, but this can change the other tab, should be tested
                return;

            } else if (event.getCode() == KeyCode.RIGHT) {

                CustomTableView.this.getSelectionModel().selectNext();

                TablePosition focusedCellPosition = CustomTableView.this.getFocusModel().getFocusedCell();
                int row = focusedCellPosition.getRow();
                TableColumn column = focusedCellPosition.getTableColumn();
                CustomTableView.this.edit(row, column);
                event.consume();

            } else if (event.getCode() == KeyCode.LEFT) {

                CustomTableView.this.selectPrevious();

                TablePosition focusedCellPosition = CustomTableView.this.getFocusModel().getFocusedCell();
                int row = focusedCellPosition.getRow();
                TableColumn column = focusedCellPosition.getTableColumn();
                CustomTableView.this.edit(row, column);
                event.consume();

            } else if (event.getCode() == KeyCode.UP) {

                CustomTableView.this.getSelectionModel().selectAboveCell();

                TablePosition focusedCellPosition = CustomTableView.this.getFocusModel().getFocusedCell();
                int row = focusedCellPosition.getRow();
                TableColumn column = focusedCellPosition.getTableColumn();
                CustomTableView.this.edit(row, column);
                event.consume();

            } else if (event.getCode() == KeyCode.DOWN) {

                CustomTableView.this.getSelectionModel().selectBelowCell();

                TablePosition focusedCellPosition = CustomTableView.this.getFocusModel().getFocusedCell();
                int row = focusedCellPosition.getRow();
                TableColumn column = focusedCellPosition.getTableColumn();
                CustomTableView.this.edit(row, column);
                event.consume();
            }
        };
    }

    /**
     * Getter of the current tableColumn.
     * A simple shortcut.
     *
     * @param column - {@link TableColumn}
     * @param offset - int
     * @return - {@link TableColumn}
     */
    private TableColumn<T,?> getTableColumn(
            final TableColumn<T,?> column,
            int offset) {
        int columnIndex = this.getVisibleLeafIndex(column);
        int newColumnIndex = columnIndex + offset;
        return this.getVisibleLeafColumn(newColumnIndex);
    }

    /**
     * Implement a method to select the previous cell.
     * the method {@link  TableViewSelectionModel#getSelectionModel()#selectPrevious()} already exist, but this
     * method is bugged when you want to access on the first element of the tableview (maybe a error with the size of the table).
     *
     * @author Gaetan Brenckle
     */
    private void selectPrevious() {
        if (this.getSelectionModel().isCellSelectionEnabled()) {
            // in cell selection mode, we have to wrap around, going from
            // right-to-left, and then wrapping to the end of the previous line
            TablePosition pos = this.getFocusModel().getFocusedCell();
            if (pos.getColumn() - 1 >= 0) {
                // go to previous row
                this.getSelectionModel().select(pos.getRow(), getTableColumn(pos.getTableColumn(), -1));
            } else if (pos.getRow() < this.getItems().size()) {
                // wrap to end of previous row
                this.getSelectionModel().select(pos.getRow() - 1,
                        this.getVisibleLeafColumn(
                                this.getVisibleLeafColumns().size() - 1));
            }
        } else {
            int focusIndex = this.getFocusModel().getFocusedIndex();
            if (focusIndex == -1) {
                this.getSelectionModel().select(this.getItems().size() - 1);
            } else if (focusIndex > 0) {
                this.getSelectionModel().select(focusIndex - 1);
            }
        }
    }
}
