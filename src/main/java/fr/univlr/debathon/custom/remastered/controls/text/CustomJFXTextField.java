package fr.univlr.debathon.custom.remastered.controls.text;

import com.jfoenix.controls.JFXTextField;
import de.saxsys.mvvmfx.utils.validation.ValidationStatus;
import fr.univlr.debathon.custom.fixer.TooltipDefaultFixer;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Point2D;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Text;
import javafx.stage.Window;

/**
 * Custom javafx textfield control.
 * This control implement a property when the text has changed, when the focus is lost. Useful when you want to log.
 * Also, you can set a {@link ValidationStatus} to have a tooltip that display the current {@link de.saxsys.mvvmfx.utils.validation.ValidationMessage}.
 *
 * @author Gaetan Brenckle
 */
public class CustomJFXTextField extends JFXTextField {

    private final Text txtMessage = new Text("text");
    private final Tooltip tool = new Tooltip();

    private final StringProperty onChangeWhenRelease = new SimpleStringProperty();

    private ValidationStatus currentStatus = null;
    private String beforeEditing = "";
    private String afterEditing = "";

    /**
     * Default constructor.
     * Use a listener of the focusProperty to update the onChangeWhenReleaseProperty when the field has changed, on focus lost.
     * Also, display if needed, and a {@link ValidationStatus} is set, a tooltip.
     *
     * @author Gaetan Brenckle
     */
    public CustomJFXTextField() {
        super();

        TooltipDefaultFixer.setTooltipTimers(100, 99999999, 200);
        focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                beforeEditing = getText();

                toolTipShow();
            } else {
                tool.hide();

                afterEditing = getText();

                if (beforeEditing != null && !beforeEditing.equals(afterEditing)) {
                    onChangeWhenRelease.set(afterEditing);

                    //I dont know why, but the property need to be getted to fire the change event
                    onChangeWhenRelease.get();
                }
            }
        });
    }

    /**
     * Constructor to directly set a text.
     * After that, is the same as the default contructor
     *
     * @param text - {@link String} - the string displayed
     *
     * @see CustomJFXTextField#CustomJFXTextField() ()
     */
    public CustomJFXTextField(String text) {
        this();
        super.setText(text);
    }

    /**
     * Show the tooltip if a {@link ValidationStatus} is set.
     *
     * @author Gaetan Brenckle
     */
    private void toolTipShow() {
        Point2D p = this.localToScene(0.0, 0.0);

        if (currentStatus != null && !currentStatus.isValid()) {
            tool.show(this,
                    p.getX() + this.getScene().getX() + this.getScene().getWindow().getX(),
                    p.getY() + this.getScene().getY() + this.getScene().getWindow().getY() + this.getHeight());
        }
    }

    /**
     * Setter of the variable currentStatus.
     * This function also create some listener to be enable to update the text and the tooltip when the status change.
     *
     * @author Gaetan Brenckle
     *
     * @param status - {@link ValidationStatus} - status of the rules for the validation of the control.
     */
    public void setValidationStatus(ValidationStatus status) {
        if (status == null) {
            return ;
        }
        tool.textProperty().bind(txtMessage.textProperty());

        //Validation Message text update
        if (status.getHighestMessage().isPresent()) {
            txtMessage.setText(status.getHighestMessage().get().getMessage());
        }

        //Validation Message text update
        setOnKeyReleased(event -> {
            if (status.getHighestMessage().isPresent()) {
                txtMessage.setText(status.getHighestMessage().get().getMessage());
            }
        });

        //show the tooltip when the validation is false only
        status.validProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                tool.hide();
            } else {
                toolTipShow();
            }
        });

        final Window window = this.getScene().getWindow();
        window.xProperty().addListener(observable -> {
            if (tool.isShowing()) {
                toolTipShow();
            }
        });
        window.yProperty().addListener(observable -> {
            if (tool.isShowing()) {
                toolTipShow();
            }
        });

        this.currentStatus = status;
    }

    /**
     * Getter for the variable beforeEditing.
     *
     * @author Gaetan Brenckle
     *
     * @return - {@link String} - return the variable beforeEditing.
     */
    public String getTextBeforeEditing() {
        return beforeEditing;
    }

    /**
     * Getter for the variable afterEditing.
     *
     * @author Gaetan Brenckle
     *
     * @return - {@link String} - return the variable afterEditing.
     */
    public String getTextAfterEditing() {
        return afterEditing;
    }

    /**
     * Getter for the property onChangeWhenRelease.
     *
     * @author Gaetan Brenckle
     *
     * @return - {@link String} - return the property onChangeWhenRelease.
     */
    public StringProperty onChangeWhenReleaseProperty() {
        return onChangeWhenRelease;
    }
}
