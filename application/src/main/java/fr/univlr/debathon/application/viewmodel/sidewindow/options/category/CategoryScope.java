package fr.univlr.debathon.application.viewmodel.sidewindow.options.category;

import de.saxsys.mvvmfx.Scope;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;

/**
 * Scope create for some information of the options window.
 *
 * @author Gaetan Brenckle
 */
public class CategoryScope implements Scope {

    private final ObjectProperty<Node> optionsBaseScene_ = new SimpleObjectProperty<>();

    /**
     * Property of the variable optionsBaseScene_.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link ObjectProperty}{@literal <}{@link Node}{@literal >} - return the property of the variable optionsBaseScene_.
     */
    public ObjectProperty<Node> optionsBaseSceneProperty() {
        return optionsBaseScene_;
    }
}
