package fr.univlr.debathon.custom.remastered.controls.tabview;

import fr.univlr.debathon.custom.remastered.controls.text.CustomTextField_R;
import javafx.application.Platform;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.input.KeyCode;
import javafx.util.Callback;

/**
 * String cell for table view.
 *
 * Implement with {@link javafx.scene.control.TableColumn#setCellFactory(Callback)}.
 * Use it with on {@link CustomTableView}.
 *
 * Based on a work of Graham Smith.
 *
 * @author Brenckle Gaetan
 */
public class CustomEditingCell_String<T> extends TableCell<T, String> {
    private CustomTextField_R textField;

    /**
     * Default constructor.
     *
     * @author Gaetan Brenckle
     */
    public CustomEditingCell_String() {
    }

    /**
     * Method invoked when you start to edit the cell. called after the usage of {@link TableCell#commitEdit(Object)}.
     * Display and set the focus on the textfield when called.
     *
     * @author Gaetan Brenckle
     */
    @Override
    public void startEdit() {
        super.startEdit();
        if (textField == null) {
            createTextField();
        }
        setGraphic(textField);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        Platform.runLater(() -> {
            textField.requestFocus();
            textField.selectAll();
        });
    }

    /**
     * Cancel the current edit and hide the textfield.
     *
     * @author Gaetan Brenckle
     */
    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText((String) getItem());
        textField.setText(getString());
        setContentDisplay(ContentDisplay.TEXT_ONLY);
    }

    /**
     * Method invoked when the value is updated.
     * Change the value of the Textfield according to the current item.
     *
     * @author Gaetan Brenckle
     *
     * @param item - {@link String} - current object
     * @param empty - boolean - true if the current row is empty
     */
    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                if (textField != null) {
                    textField.setText(getString());
                }
                setGraphic(textField);
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            } else {
                setText(getString());
                setContentDisplay(ContentDisplay.TEXT_ONLY);
            }
        }
    }

    /**
     * Initialize the TextField.
     * Ajust his size and implement event when a key is pressed on the field.
     *      ENTER : Commit if match the regex (if it is a number) or cancel the edit
     *      TAB : Commit if match the regex (if it is a number) or cancel the edit
     *      ESCAPE: cancel the commit
     *
     * Create a listener to commit when the combobox is focused.
     *
     * @author Gaetan Brenckle
     */
    private void createTextField() {
        textField = new CustomTextField_R(getString());
        textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);

        textField.setOnKeyPressed(t -> {
            if (t.getCode() == KeyCode.ENTER) {
                commitEdit(textField.getText());
            } else if (t.getCode() == KeyCode.TAB) {
                commitEdit(textField.getText());
            } else if (t.getCode() == KeyCode.ESCAPE) {
                cancelEdit();
            }
        });

        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue && textField != null) {
                commitEdit(textField.getText());
            }
        });
    }

    /**
     * get a string of the current value (use the toString value of the item class).
     *
     * @author Gaetan Brenckle
     *
     * @return the current string.
     */
    private String getString() {
        return getItem() == null ? "" : getItem();
    }
}