package fr.univlr.debathon.application.view.taskmanager;

import com.sun.javafx.scene.control.skin.CustomTaskProgressViewSkin;
import de.saxsys.mvvmfx.InjectViewModel;

import fr.univlr.debathon.application.Main;
import fr.univlr.debathon.application.taskmanager.Task_Custom;
import fr.univlr.debathon.application.view.FxmlView_SceneCycle;
import fr.univlr.debathon.application.viewmodel.taskmanager.TaskManagerWindowViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import org.controlsfx.control.TaskProgressView;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * View of the TaskManagerWindow
 * This view contain all task that are proceced by the application.
 *
 * @author Gaetan Brenckle
 */
public class TaskManagerWindowView extends FxmlView_SceneCycle<TaskManagerWindowViewModel> implements Initializable {

    @FXML
    private TaskProgressView<Task_Custom<?>> taskManager;

    @InjectViewModel
    private TaskManagerWindowViewModel taskManagerWindowViewModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.setViewModel(taskManagerWindowViewModel);

        taskManager.setSkin(new CustomTaskProgressViewSkin<>(taskManager));

        taskManager.setGraphicFactory(param -> new ImageView(param.getImage()));
        Main.TASKMANAGER.addTaskManager(taskManager);
    }


    @Override
    public void onViewAdded_Cycle() {
    }

    @Override
    public void onViewRemoved_Cycle() {
        Main.TASKMANAGER.removeTaskManager(taskManager);
    }
}
