package fr.univlr.debathon.custom.remastered.controls.tabview;

import fr.univlr.debathon.custom.remastered.controls.list.CustomComboBox_R;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.input.KeyCode;
import javafx.util.Callback;

/**
 * ComboBox cell for table view.
 *
 * Implement with {@link javafx.scene.control.TableColumn#setCellFactory(Callback)}.
 * Use it with on {@link CustomTableView}.
 *
 * @author Brenckle Gaetan
 */
public class CustomEditingCell_Combobox<T, V> extends TableCell<T, V> {

    private final CustomComboBox_R<V> comboBox = new CustomComboBox_R<>();
    private final ObjectProperty<V> currentObject = new SimpleObjectProperty<>(null);

    /**
     * Defaut Constructor.
     * Initialize the size of the combobox.
     * Implement a event when some key are pressed:
     *      - ENTER : commit
     *      - TAB   : commit
     *      - ESCAPE : cancel the current edit
     *
     * Create a listener to commit when the combobox is focused.
     *
     * @author Gaetan Brenckle
     *
     * @param linkedList - {@link ListProperty} - list linked
     *
     */
    public CustomEditingCell_Combobox(ListProperty<V> linkedList) {
        comboBox.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);

        comboBox.listItemsProperty().bind(linkedList);

        comboBox.setOnKeyPressed(t -> {
            if (t.getCode() == KeyCode.CONTROL) {
                comboBox.hide();
                comboBox.show();
            } else if (t.getCode() == KeyCode.ENTER) {
                commitEdit(currentObject.get());
            } else if (t.getCode() == KeyCode.TAB) {
                commitEdit(currentObject.get());
            } else if (t.getCode() == KeyCode.ESCAPE) {
                cancelEdit();
            }
        });

        comboBox.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (Boolean.TRUE.equals(!newValue) && comboBox != null) {
                commitEdit(currentObject.get());
            }
        });

        currentObject.bind(comboBox.valueProperty());
    }

    /**
     * Method invoked when you start to edit the cell. called after the usage of {@link TableCell#commitEdit(Object)}.
     * Display and set the focus on the combobox when called.
     *
     * @author Gaetan Brenckle
     */
    @Override
    public void startEdit() {
        super.startEdit();
        setGraphic(comboBox);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        Platform.runLater(comboBox::requestFocus);
    }

    /**
     * Cancel the current edit and hide the combobox.
     *
     * @author Gaetan Brenckle
     */
    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(getString());
        comboBox.getSelectionModel().select(currentObject.get());
        setContentDisplay(ContentDisplay.TEXT_ONLY);
    }

    /**
     * Method invoked when the value is updated.
     * Change the value of the Combobox according to the current item.
     *
     * @author Gaetan Brenckle
     *
     * @param item - {@link Boolean} - current object
     * @param empty - boolean - true if the current row is empty
     */
    @Override
    public void updateItem(V item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                if (comboBox != null) {
                    comboBox.getSelectionModel().select(currentObject.get());
                }
                setGraphic(comboBox);
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            } else {
                setText(getString());
                comboBox.getSelectionModel().select(getItem());
                setContentDisplay(ContentDisplay.TEXT_ONLY);
            }
        }
    }

    /**
     * get a string of the current value (use the toString value of the item class).
     *
     * @author Gaetan Brenckle
     *
     * @return the current string.
     */
    private String getString() {
        return (getItem() == null) ? "" : getItem().toString();
    }

    public CustomEditingCell_Combobox<T, V> setEditableComboBox(boolean edit) {
        comboBox.setEditable(edit);
        return this;
    }
}