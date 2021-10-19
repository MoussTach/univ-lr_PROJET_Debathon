package fr.univlr.debathon.custom.remastered.controls.tabview;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.input.KeyCode;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * ComboBox cell for table view.
 *
 * Implement with {@link javafx.scene.control.TableColumn#setCellFactory(Callback)}.
 * Use it with on {@link CustomTableView}.
 *
 * Based on a work of Graham Smith.
 *
 * @author Brenckle Gaetan
 */
public class CustomEditingCell_Date<T> extends TableCell<T, LocalDate> {

    private final ObjectProperty<LocalDate> currentObject = new SimpleObjectProperty<>(null);
    private DatePicker datePicker = null;

    private DateTimeFormatter formatter = null;

    /**
     * Defaut Constructor.
     * Initialize the size of the combobox.
     * Implement a event when some key are pressed:
     *      - CONTROL : show the popover (Use Escape to continue, enter to valid)
     *      - ENTER : commit
     *      - TAB   : commit
     *      - ESCAPE : cancel the current edit
     *
     * Create a listener to commit when the combobox is focused.
     *
     * @author Gaetan Brenckle
     */
    public CustomEditingCell_Date() {

        this.datePicker = new DatePicker();
        setupDatePicker(datePicker);
    }

    /**
     * Constructor with a pattern.
     *
     * @see CustomEditingCell_Date#CustomEditingCell_Date()
     * @param pattern - string pattern, same as the {@link DateTimeFormatter#ofPattern(String)}
     */
    public CustomEditingCell_Date(String pattern) {
        this();

        if (pattern != null && !pattern.isEmpty()) {
            formatter = DateTimeFormatter.ofPattern(pattern);

            datePicker.setConverter(new StringConverter<LocalDate>() {
                final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

                {
                    datePicker.setPromptText(pattern.toLowerCase());
                }

                @Override
                public String toString(LocalDate date) {
                    if (date != null) {
                        return dateFormatter.format(date);
                    } else {
                        return "";
                    }
                }

                @Override
                public LocalDate fromString(String string) {
                    if (string != null && !string.isEmpty()) {
                        return LocalDate.parse(string, dateFormatter);
                    } else {
                        return null;
                    }
                }
            });
        }
    }

    /**
     * Constructor with a datePicker, used when you have to modify the datePicker before use it.
     *
     * @param datePicker - a datePicker
     */
    public CustomEditingCell_Date(DatePicker datePicker) {
        this.datePicker = datePicker;
        setupDatePicker(datePicker);
    }

    /**
     * Constructor with a datePicker, used when you have to modify the datePicker before use it.
     *
     * @param datePicker - a datePicker
     * @param pattern - string pattern, same as the {@link DateTimeFormatter#ofPattern(String)}
     */
    public CustomEditingCell_Date(DatePicker datePicker, String pattern) {
        this.datePicker = datePicker;
        setupDatePicker(datePicker);
        formatter = DateTimeFormatter.ofPattern(pattern);
    }

    /**
     * Setup to initialize some text for the datePicker
     *
     * @author Gaetan Brenckle
     *
     * @param datePicker - the affected datePicker
     */
    private void setupDatePicker(DatePicker datePicker) {
        datePicker.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);

        datePicker.setOnKeyPressed(t -> {
            if (t.getCode() == KeyCode.CONTROL) {
                datePicker.hide();
                datePicker.show();
            } else if (t.getCode() == KeyCode.ENTER) {
                commitEdit(currentObject.get());
            } else if (t.getCode() == KeyCode.TAB) {
                commitEdit(currentObject.get());
            } else if (t.getCode() == KeyCode.ESCAPE) {
                cancelEdit();
            }
        });

        datePicker.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue && datePicker != null) {
                commitEdit(currentObject.get());
            }
        });

        currentObject.bind(datePicker.valueProperty());
    }

    /**
     * Set the editable check the value.
     *
     * @author Gaetan Brenckle
     *
     * @param editable - boolean - change the value
     * @return - {@link CustomEditingCell_Date} - builder pattern
     */
    public CustomEditingCell_Date<T> setEditable_Date(boolean editable) {

        if (editable) {
            datePicker.setEditable(true);
            datePicker.setOnShown(event -> {
            });
        } else {
            datePicker.setEditable(false);
            datePicker.setOnShown(event ->
                    datePicker.hide());
        }
        return this;
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
        setGraphic(datePicker);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        Platform.runLater(datePicker::requestFocus);
    }

    /**
     * Cancel the current edit and hide the combobox.
     *
     * @author Gaetan Brenckle
     */
    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(getDate_string());
        datePicker.setValue(currentObject.get());
        setContentDisplay(ContentDisplay.TEXT_ONLY);
    }

    /**
     * Method invoked when the value is updated.
     * Change the value of the Combobox according to the current item.
     *
     * @author Gaetan Brenckle
     *
     * @param item - {@link LocalDate} - current object
     * @param empty - boolean - true if the current row is empty
     */
    @Override
    public void updateItem(LocalDate item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                if (datePicker != null) {
                    datePicker.setValue(currentObject.get());
                }
                setGraphic(datePicker);
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            } else {
                setText(getDate_string());
                datePicker.setValue(getItem());
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
    private String getDate_string() {
        return ((getItem() != null) ?
                (formatter == null) ?
                        getItem().toString()
                        : getItem().format(formatter)
                : "");
    }
}