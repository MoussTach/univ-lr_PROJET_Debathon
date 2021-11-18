package fr.univlr.debathon.custom.remastered.controls.tabview;

import javafx.application.Platform;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.util.Callback;

/**
 * Basic cell for table view.
 *
 * Implement with {@link javafx.scene.control.TableColumn#setCellFactory(Callback)}.
 * Use it with on {@link CustomTableView}.
 *
 * @author Brenckle Gaetan
 */
public class CustomEditingCell_Basic<T, V> extends TableCell<T, V> {

    private final Label label = new Label();

    /**
     * Default Constructor.
     * Initialize the size of the label.
     *
     * @author Gaetan Brenckle
     */
    public CustomEditingCell_Basic() {
        label.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        defaultValue();
    }

    /**
     * Method invoked when you start to edit the cell. called after the usage of {@link TableCell#commitEdit(Object)}.
     *
     * @author Gaetan Brenckle
     */
    @Override
    public void startEdit() {
        super.startEdit();
        defaultValue();
        Platform.runLater(label::requestFocus);
    }

    /**
     * Method invoked when the value is updated.
     * Change the label with the changed value though {@link CustomEditingCell_Basic#defaultValue()}
     *
     * @author Gaetan Brenckle
     *
     * @param item - {@link V} - current object
     * @param empty - boolean - true if the current row is empty
     */
    @Override
    public void updateItem(V item, boolean empty) {
        super.updateItem(item, empty);
        defaultValue();
    }

    /**
     * Default change of this class, only and always show the label.
     *
     * @author Gaetan Brenckle
     */
    private void defaultValue() {
        label.setText(getString());
        setGraphic(label);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    }

    /**
     * get a string of the current value (use the toString value of the item class).
     *
     * @author Gaetan Brenckle
     *
     * @return the current string.
     */
    public String getString() {
        return (getItem() == null) ? "" : getItem().toString();
    }
}