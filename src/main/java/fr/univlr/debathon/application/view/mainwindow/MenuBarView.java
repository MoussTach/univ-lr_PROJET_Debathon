package fr.univlr.debathon.application.view.mainwindow;

import de.saxsys.mvvmfx.InjectViewModel;
import fr.univlr.debathon.application.view.FxmlView_SceneCycle;
import fr.univlr.debathon.application.viewmodel.mainwindow.MenuBarViewModel;
import fr.univlr.debathon.log.generate.CustomLogger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToggleGroup;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * View of the menubar.
 *
 * @author Gaetan Brenckle
 */
public class MenuBarView extends FxmlView_SceneCycle<MenuBarViewModel> implements Initializable {

    private static final CustomLogger LOGGER = CustomLogger.create(MenuBarView.class.getName());

    @FXML
    public Menu menuBarFile;
    @FXML
    public MenuItem menuBarFile_menuItemOption;

    @FXML
    private Menu menuBarHelp;
    @FXML
    private MenuItem menuBarHelp_menuItemAbout;

    private final ToggleGroup tGroupBtnMenu_tag = new ToggleGroup();

    @InjectViewModel
    private MenuBarViewModel menuBarViewModel;


    /**
     * action on menu validation of the menu menuBarFile.
     *
     * @author Gaetan Brenckle
     */
    @FXML
    private void actValidation_menu_file() {
        LOGGER.input(String.format("Press the menuItem %s", menuBarFile.getId()));
    }

    /**
     * action of the menuItem menuBarFile_menuItemOption.
     *
     * @author Gaetan Brenckle
     */
    @FXML
    private void act_menuItem_option() {
        LOGGER.input(String.format("Press the menuItem %s", menuBarFile_menuItemOption.getId()));
        menuBarViewModel.act_Open_OptionWindow();
    }

    /**
     * action on menu validation of the menu menuBarHelp.
     *
     * @author Gaetan Brenckle
     */
    @FXML
    private void actValidation_menu_help() {
        LOGGER.input(String.format("Press the menuItem %s", menuBarHelp.getId()));
    }

    /**
     * action of the menuItem menuBarHelp_menuItemAbout.
     *
     * @author Gaetan Brenckle
     */
    @FXML
    private void act_menuItem_about() {
        LOGGER.input(String.format("Press the menuItem %s", menuBarHelp_menuItemAbout.getId()));
    }


    public void initialize(URL location, ResourceBundle resources) {
        this.setViewModel(menuBarViewModel);

        menuBarFile.textProperty().bind(menuBarViewModel.menuBarFile_label_Property());
        menuBarFile_menuItemOption.textProperty().bind(menuBarViewModel.menuBarFile_menuItemOption_labelProperty());

        menuBarHelp.textProperty().bind(menuBarViewModel.menuBarHelp_label_Property());
        menuBarHelp_menuItemAbout.textProperty().bind(menuBarViewModel.menuBarHelp_menuItemAbout_label_Property());

        tGroupBtnMenu_tag.selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> {
            if (newVal == null)
                oldVal.setSelected(true);
        });
    }


    @Override
    public void onViewAdded_Cycle() {
        // default implementation
    }

    @Override
    public void onViewRemoved_Cycle() {
        menuBarFile.textProperty().unbind();
        menuBarFile_menuItemOption.textProperty().unbind();

        menuBarHelp.textProperty().unbind();
        menuBarHelp_menuItemAbout.textProperty().unbind();
    }
}
