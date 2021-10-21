package fr.univlr.debathon.custom.remastered.controls.list;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;

import java.util.Comparator;

/**
 * This class is created with some code from internet.
 * This enable the combobox to retrieve information when you type few letters
 *
 * @author Gaetan Brenckle
 *
 * @param <T>- the object display with this control
 */
public class CustomComboBox_R<T> extends ComboBox<T> {

    private final StringProperty typedText = new SimpleStringProperty("");
    private final ListProperty<T> listItems = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final ListProperty<T> sortedList = new SimpleListProperty<>(
            new SortedList<>(
                    filter_ComboBox(),
                    Comparator.comparing(T::toString)
            )
    );


    /**
     * Default constructor
     * invoke default method
     *
     * Create a listerner on editableProperty to set the key event when the property is invoked.
     *
     * @author Gaetan Brenckle
     */
    public CustomComboBox_R() {

        itemsProperty().bind(sortedList);
        converter_StringToClass();

        //create a property to charge all needed information to filter the text
        this.editableProperty().addListener((observable, oldValue, newValue) -> {
            if (Boolean.TRUE.equals(newValue)) {
                this.addEventFilter(KeyEvent.KEY_RELEASED, eventOnKeyReleased());
            } else {
                this.addEventFilter(KeyEvent.KEY_RELEASED, null);
            }
        });
    }

    /**
     * initialize the text filter to be possible to search into the list with the editor of the combobox.
     * Also change the visibleRowCount according to the size on the sorted list (default is 10)
     *
     * @author Gaetan Brenckle
     *
     * @return - {@link FilteredList}
     */
    private FilteredList<T> filter_ComboBox() {
        final FilteredList<T> filteredData  = new FilteredList<>(listItems, p -> true);

        this.typedText.addListener((observable, oldValue, newValue) -> {
                    final int listSize = sortedList.size();

                    filteredData.setPredicate(value -> {
                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        }

                        return value.toString().toLowerCase().startsWith(newValue.toLowerCase());
                    });

                    if (this.isShowing() && sortedList.size() != listSize) {
                        this.hide();
                        this.setVisibleRowCount(Math.min(sortedList.size(), 10));
                        this.show();
                    }
                }
        );
        return filteredData;
    }

    /**
     * create a string converter, this will convert the current data (even if it is a object other that String)
     * This invoke the toString() method to display on the list, and return the correct data object when selected
     *
     * @author Gaetan Brenckle
     */
    private void converter_StringToClass() {

        this.setConverter(new StringConverter<T>() {

            @Override
            public String toString(T data) {
                if (data != null) {
                    return data.toString();
                } else {
                    return "";
                }
            }

            @Override
            public T fromString(String str) {

                for (T data : sortedList) {
                    if (data.toString().equals(str)) {
                        return data;
                    }
                }
                return null;
            }
        });
    }

    /**
     * Event on key realeased.
     * This is the core of the editing customed part.
     * This will be throw after each input to change the item displayed.
     *
     * @author Gaetan Brenckle
     *
     * @return - {@link EventHandler} - the event when the key are realeased
     */
    private EventHandler<KeyEvent> eventOnKeyReleased() {
        final BooleanProperty singleEvent = new SimpleBooleanProperty(false);
        return event -> {

            singleEvent.set(!singleEvent.get());
            if (!singleEvent.get()) {
                return;
            }

            if (event.isControlDown() ||
                    event.getCode() == KeyCode.ESCAPE || event.getCode() == KeyCode.CONTROL ||
                    event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.LEFT ||
                    event.getCode() == KeyCode.DELETE || event.getCode() == KeyCode.HOME ||
                    event.getCode() == KeyCode.END || event.getCode() == KeyCode.TAB ||
                    event.getCode() == KeyCode.SHIFT || event.getCode() == KeyCode.CAPS ||
                    event.getCode() == KeyCode.ALT || event.getCode() == KeyCode.ALT_GRAPH) {
                return;
            }

            if (event.getCode().equals(KeyCode.DOWN)) {

                this.show();
                if (!this.getSelectionModel().isEmpty()) {
                    T item = this.getSelectionModel().getSelectedItem();

                    this.getEditor().setText(typedText.get() + item.toString().substring(typedText.get().length()));
                    this.getEditor().positionCaret(typedText.get().length());
                    this.getEditor().selectEnd();
                }
                return;
            }

            if (event.getCode().equals(KeyCode.UP)) {

                if (!this.getSelectionModel().isEmpty()) {
                    T item = this.getSelectionModel().getSelectedItem();

                    this.getEditor().setText(typedText.get() + item.toString().substring(typedText.get().length()));
                    this.getEditor().positionCaret(typedText.get().length());
                    this.getEditor().selectEnd();
                }
                return;
            }

            int rangeSelection = this.getEditor().getText().length();
            if (event.getCode().equals(KeyCode.BACK_SPACE)
                    && rangeSelection > 0
                    && typedText.get().length() <= this.getEditor().getText().length()) {
                rangeSelection -= 1;
            }
            //Removing the last character if need. This double back_space is only to remove the last character after removing the current selection
            //by the real back_space
            typedText.set(this.getEditor().getText().substring(0, rangeSelection));

            boolean isFound = false;
            for (T item : listItems) {
                if (item != null && typedText.get() != null
                        && !item.toString().isEmpty() && !typedText.get().isEmpty()
                        && item.toString().toLowerCase().startsWith(typedText.get().toLowerCase())) {
                    try {
                        this.getEditor().setText(item.toString());
                        typedText.set(this.getEditor().getText().substring(0, typedText.get().length()));
                        this.setValue(item);
                        this.getSelectionModel().select(item);

                    } catch (Exception e) {
                        this.getEditor().setText(typedText.get());
                    }

                    this.getEditor().positionCaret(typedText.get().length());
                    this.getEditor().selectEnd();

                    isFound = true;
                    break;
                }
            }

            if (!isFound) {
                this.setValue(null);
                this.getSelectionModel().select(null);
                this.getEditor().setText(typedText.get());

                this.getEditor().positionCaret(typedText.get().length());
                this.getEditor().selectEnd();
            }
        };
    }

    /**
     * Property of the variable listItems.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link ListProperty} - return the property of the variable listItems.
     */
    public ListProperty<T> listItemsProperty() {
        return listItems;
    }
}
