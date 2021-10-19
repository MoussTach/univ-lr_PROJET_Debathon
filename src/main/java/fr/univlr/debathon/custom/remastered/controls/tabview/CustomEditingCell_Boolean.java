package fr.univlr.debathon.custom.remastered.controls.tabview;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.input.KeyCode;
import javafx.util.Callback;

/**
 * Boolean cell for table view.
 *
 * Implement with {@link javafx.scene.control.TableColumn#setCellFactory(Callback)}.
 * Use it with on {@link CustomTableView}.
 *
 * @author Brenckle Gaetan
 */
public class CustomEditingCell_Boolean<T> extends TableCell<T, Boolean> {
    private CheckBox checkbox = null;
    private boolean canBeChanged = true;

    /**
     * Defaut Constructor.
     * Initialize the size of the checkBox.
     * Implement a event when some key are pressed:
     *      - ENTER : reverse the current boolean and commit
     *      - SPACE : reverse the current boolean and commit
     *      - TAB   : commit
     *      - ESCAPE : cancel the current edit
     *
     * @author Gaetan Brenckle
     */
    public CustomEditingCell_Boolean() {

        if (checkbox == null) {
            checkbox = new CheckBox();
        }

        checkbox.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);

        checkbox.setText("");
        setGraphic(null);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        checkbox.setAlignment(Pos.CENTER);
        setAlignment(Pos.CENTER);

        checkbox.setOnKeyPressed(event -> {
            if (canBeChanged) {
                if (event.getCode() == KeyCode.ENTER) {
                    checkbox.setSelected(!checkbox.isSelected());
                    commitEdit(checkbox.isSelected());
                } else if (event.getCode() == KeyCode.SPACE) {
                    checkbox.setSelected(!checkbox.isSelected());
                    commitEdit(checkbox.isSelected());
                } else if (event.getCode() == KeyCode.TAB) {
                    commitEdit(checkbox.isSelected());
                } else if (event.getCode() == KeyCode.ESCAPE) {
                    cancelEdit();
                }
            }
        });
    }

    /**
     * Constructo with a indicator to unable or not the change when a action occurs.
     *
     * @author Gaetan Brenckle
     *
     * @param canBeChanged - boolean - state of the change
     */
    public CustomEditingCell_Boolean(boolean canBeChanged) {
        this();
        this.canBeChanged = canBeChanged;

        if (!canBeChanged) {
            checkbox = new CheckBox() {
                @Override
                public void arm() {
                    // intentionally do nothing
                }
            };
        }

    }

    /**
     * Method invoked when you start to edit the cell. called after the usage of {@link TableCell#commitEdit(Object)}.
     * Set the focus on the checkbox when called.
     *
     * @author Gaetan Brenckle
     */
    @Override
    public void startEdit() {
        super.startEdit();
        Platform.runLater(checkbox::requestFocus);
    }

    /**
     * Method invoked when the value is updated.
     * Change the value of the checkBox according to the current item.
     *
     * @author Gaetan Brenckle
     *
     * @param item - {@link Boolean} - current object
     * @param empty - boolean - true if the current row is empty
     */
    @Override
    public void updateItem(Boolean item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {
            setGraphic(checkbox);
            if (isEditing()) {
                checkbox.setSelected(item);
                setItem(item);
            } else {
                checkbox.setSelected(!getItem());
            }
        }
    }
}