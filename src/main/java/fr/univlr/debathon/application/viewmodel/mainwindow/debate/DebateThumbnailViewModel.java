package fr.univlr.debathon.application.viewmodel.mainwindow.debate;

import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.InjectScope;
import de.saxsys.mvvmfx.ViewTuple;
import fr.univlr.debathon.application.view.mainwindow.debate.DebateThumbnailView;
import fr.univlr.debathon.application.view.mainwindow.debate.DebateView;
import fr.univlr.debathon.application.view.mainwindow.debate.items.TagView;
import fr.univlr.debathon.application.viewmodel.ViewModel_SceneCycle;
import fr.univlr.debathon.application.viewmodel.mainwindow.MainViewScope;
import fr.univlr.debathon.application.viewmodel.mainwindow.debate.items.TagViewModel;
import fr.univlr.debathon.log.generate.CustomLogger;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.scene.layout.BorderPane;

public class DebateThumbnailViewModel extends ViewModel_SceneCycle {

    private static final CustomLogger LOGGER = CustomLogger.create(DebateThumbnailViewModel.class.getName());

    /*TODO Model
    private final Debate;*/

    //Value
    private final StringProperty lblTitle_value = new SimpleStringProperty("/");
    private final ListProperty<ViewTuple<TagView, TagViewModel>> listTag_selected_value = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final StringProperty lblNbPeople_value = new SimpleStringProperty("/");

    @InjectScope
    private MainViewScope mainViewScope;


    /**
     * Default constructor
     *
     * @author Gaetan Brenckle
     */
    public DebateThumbnailViewModel() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][constructor] Creation of the DebateThumbnailViewModel() object.");
        }

        //TODO link Model with data
    }

    public void actvm_btnOpenDebate() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[public][method] Usage of the DebateThumbnailViewModel.actvm_btnOpenDebate()");
        }

        final ViewTuple<DebateView, DebateViewModel> debateViewTuple = FluentViewLoader.fxmlView(DebateView.class)
                .providedScopes(mainViewScope)
                .load();

        BorderPane mainBorder = this.mainViewScope.basePaneProperty().get();
        mainBorder.setCenter(debateViewTuple.getView());
    }


    //Value
    /**
     * Property of the variable lblTitle_value.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link StringProperty} - return the property of the variable lblTitle_value.
     */
    public StringProperty lblTitle_valueProperty() {
        return lblTitle_value;
    }

    /**
     * Property of the variable listTag_selected_value.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link StringProperty} - return the property of the variable listTag_selected_value.
     */
    public ListProperty<ViewTuple<TagView, TagViewModel>> listTag_selected_valueProperty() {
        return listTag_selected_value;
    }

    /**
     * Property of the variable lblNbPeople_value.
     *
     * @author Gaetan Brenckle
     *
     * @return {@link StringProperty} - return the property of the variable lblNbPeople_value.
     */
    public StringProperty lblNbPeople_valueProperty() {
        return lblNbPeople_value;
    }


    @Override
    public void onViewAdded_Cycle() {
    }

    @Override
    public void onViewRemoved_Cycle() {
    }
}
