package fr.univlr.debathon.application.viewmodel.sidewindow.options.category;

import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.Scope;
import de.saxsys.mvvmfx.ViewTuple;
import fr.univlr.debathon.application.viewmodel.ViewModel_SceneCycle;
import fr.univlr.debathon.language.LanguageBundle;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.image.Image;

import java.util.ResourceBundle;

/**
 * ViewModel of the view {@link fr.univlr.debathon.application.view.sidewindow.options.category.CategoryListItemView}.
 * This ViewModel is created dynamically and to be available to return a view on a other pane when a item is sectioned.
 *
 * @author Gaetan Brenckle
 *
 * @param <T> extends {@link ObjectProperty}{@literal <? extends}{@link V}{@literal >}
 * @param <V> extends {@link CategoryValidator}
 */
public class CategoryListItemViewModel<T extends FxmlView<? extends V>, V extends CategoryValidator> extends ViewModel_SceneCycle {

    private final ObjectProperty<ResourceBundle> resBundle_ = LanguageBundle.getInstance().bindResourceBundle("properties.language.sidewindow.options.lg_options");

    private final ObjectProperty<Image> categoryImage_ = new SimpleObjectProperty<>(new Image(this.getClass().getResourceAsStream("/img/imageNotFound.png")));
    private final StringProperty categoryName_ = new SimpleStringProperty();

    private final String categoryNamePassed_;

    private final V sceneClassViewModel_;
    private final Node sceneClassNode_;

    private ChangeListener<ResourceBundle> listener_ChangedValue_bundleLanguage_ = null;

    /**
     * Default constructor.
     * Create a ResourceBundle listener.
     * Load the node of the View given on parameter.
     *
     * @author Gaetan Brenckle
     *
     * @param categoryName - {@link String} - text field that indicate the category.
     * @param image - {@link Image} - Image associated with the category.
     * @param sceneClassView - {@link Class}{@literal <}{@link T}{@literal >} - class that can be loaded on a parent pane when this item is selected.
     * @param scope - {@link Scope} - Scope that can be associated with the sceneClassView parameter.
     */
    public CategoryListItemViewModel(String categoryName, Image image, Class<T> sceneClassView, Scope scope) {
        this.categoryNamePassed_ = categoryName;
        this.categoryName_.set(this.resBundle_.get().getString(categoryName));
        this.setCategoryImage(image);

        //ResourceBundle listener
        if (this.listener_ChangedValue_bundleLanguage_ == null) {
            this.listener_ChangedValue_bundleLanguage_ = this::listener_bundleLanguage;
            this.resBundle_.addListener(this.listener_ChangedValue_bundleLanguage_);
        }

        final ViewTuple<T, V> dynamicTuple = FluentViewLoader.fxmlView(sceneClassView)
                .providedScopes(scope)
                .load();
        this.sceneClassViewModel_ = dynamicTuple.getViewModel();
        this.sceneClassNode_ = dynamicTuple.getView();
    }

    /**
     * Getter for the sceneNode associated with the item.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link Node} - return the Node associated with the Item.
     */
    public Node getSceneNode() {
        return this.sceneClassNode_;
    }

    /**
     * Getter for the ViewModel of the associated SceneNode.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link V} - the ViewModel of the SceneNode.
     */
    public V getSceneClassViewModel_() {
        return this.sceneClassViewModel_;
    }

    /**
     * Getter for the category name passed on the constructor without the modification of the resource bundle.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link String} - the category name passed on the constructor.
     */
    public String getCategoryNamePassed_() {
        return categoryNamePassed_;
    }

    /**
     * Setter for the image.
     * Use this Method instead of {@link ObjectProperty#set(Object)} when you want to update a image because this
     * method has a default Image if the parameter is null.
     *
     * @author Gaetan Brenckle
     *
     * @param img_ - {@link Image} - set the new Image.
     */
    private void setCategoryImage(Image img_) {
        if (img_ == null) {
            img_ = new Image(this.getClass().getResourceAsStream("/img/imageNotFound.png"));
        }
        this.categoryImage_.set(img_);
    }

    /**
     * Property of the variable categoryName_.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link StringProperty} - return the property of the variable categoryName_.
     */
    public StringProperty categoryNameProperty() {
        return this.categoryName_;
    }

    /**
     * Property of the variable categoryImage_.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link ObjectProperty}{@literal <}{@link Image}{@literal >} - return the property of the variable categoryImage_.
     */
    public ObjectProperty<Image> img_Property() {
        return this.categoryImage_;
    }


    /**
     * Listener for the resource bundle.
     *
     * @param observable - {@link ObservableValue} - the value observed
     * @param oldValue - {@link ResourceBundle} - the old value that are replaced
     * @param newValue - {@link ResourceBundle} - the new value
     */
    private void listener_bundleLanguage(ObservableValue<? extends ResourceBundle> observable, ResourceBundle oldValue, ResourceBundle newValue) {
        this.categoryName_.set(this.resBundle_.get().getString(this.categoryNamePassed_));
    }


    @Override
    public void onViewAdded_Cycle() {
        // default implementation
    }

    @Override
    public void onViewRemoved_Cycle() {
        if (this.listener_ChangedValue_bundleLanguage_ != null) {
            this.resBundle_.removeListener(this.listener_ChangedValue_bundleLanguage_);
            this.listener_ChangedValue_bundleLanguage_ = null;
        }
        LanguageBundle.getInstance().unbindResourceBundle(this.resBundle_);
    }
}