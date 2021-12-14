package fr.univlr.debathon.server.view;

import de.saxsys.mvvmfx.FxmlView;
import fr.univlr.debathon.server.viewmodel.ViewModel_SceneCycle;

/**
 * Abstract class to implement a SceneCycle usable on view.
 *
 * @author Gaetan Brenckle
 *
 * @param <T> - extends {@link ViewModel_SceneCycle} - to link the sceneCycle of the view with the viewModel
 */
public abstract class FxmlView_SceneCycle<T extends ViewModel_SceneCycle> implements FxmlView<T> {

    /**
     * method called when the view is added.
     * Need to initialize before with {@link FxmlView_SceneCycle#setViewModel(ViewModel_SceneCycle)}.
     *
     * @author Gaetan Brenckle
     */
    public abstract void onViewAdded_Cycle();

    /**
     * method called when the view is removed.
     * Need to initialize before with {@link FxmlView_SceneCycle#setViewModel(ViewModel_SceneCycle)}.
     *
     * @author Gaetan Brenckle
     */
    public abstract void onViewRemoved_Cycle();


    /**
     * Method needed to be called on the {@link javafx.fxml.Initializable} method, because the viewModel inject need to be
     * created and this method need to be here to create the link between the view and the viewModel.
     *
     * @author Gaetan Brenckle
     *
     * @param viewModel - {@link T} - the view model that will invoke addedCycle and RemovedCycle when needed.
     */
    public void setViewModel(T viewModel) {
        viewModel.setAddedView(aVoid -> onViewAdded_Cycle());
        viewModel.setRemovedView(aVoid -> onViewRemoved_Cycle());
    }
}
