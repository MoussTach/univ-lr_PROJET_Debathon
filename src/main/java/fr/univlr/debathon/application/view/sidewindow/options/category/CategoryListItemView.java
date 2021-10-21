package fr.univlr.debathon.application.view.sidewindow.options.category;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import fr.univlr.debathon.application.view.FxmlView_SceneCycle;
import fr.univlr.debathon.application.viewmodel.sidewindow.options.category.CategoryListItemViewModel;
import fr.univlr.debathon.application.viewmodel.sidewindow.options.category.CategoryValidator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * View of a item on a {@link javafx.scene.control.ListView}.
 * This item is used to reference a category for the options window.
 *
 * @author Gaetan Brenckle
 */
@SuppressWarnings("rawtypes")
public class CategoryListItemView extends FxmlView_SceneCycle<CategoryListItemViewModel> implements Initializable {

    @FXML
    private ImageView categoryImage;
    @FXML
    private Text categoryTextName;

    @InjectViewModel
    private CategoryListItemViewModel<FxmlView<CategoryValidator>, CategoryValidator> categoryListItemViewModel;

    public void initialize(URL location, ResourceBundle resources) {
        this.setViewModel(categoryListItemViewModel);

        categoryImage.imageProperty().bind(categoryListItemViewModel.img_Property());
        categoryTextName.textProperty().bind(categoryListItemViewModel.categoryNameProperty());
    }


    @Override
    public void onViewAdded_Cycle() {
        // default implementation
    }

    @Override
    public void onViewRemoved_Cycle() {
        categoryImage.imageProperty().unbind();
        categoryTextName.textProperty().unbind();
    }
}
