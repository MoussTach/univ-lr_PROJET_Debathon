package fr.univlr.debathon.server.viewmodel;

import de.saxsys.mvvmfx.SceneLifecycle;
import de.saxsys.mvvmfx.ViewModel;

import java.util.function.Consumer;

/**
 * A SceneCycle modified to permit to extend the SceneCycle on view.
 *
 * @author Gaetan Brenckle
 */
public abstract class ViewModel_SceneCycle implements ViewModel, SceneLifecycle {

    private Consumer<Void> addedView = null;
    private Consumer<Void> removedView = null;

    /**
     * method called when the viewModel is added.
     *
     * @author Gaetan Brenckle
     */
    public abstract void onViewAdded_Cycle();

    /**
     * method called when the viewModel is removed.
     *
     * @author Gaetan Brenckle
     */
    public abstract void onViewRemoved_Cycle();


    /**
     * Called when you want to bind a function to execute when the view is added.
     *
     * @author Gaetan Brenckle
     *
     * @param addedView - {@link Consumer} - a function
     */
    public void setAddedView(Consumer<Void> addedView) {
        this.addedView = addedView;
    }

    /**
     * Called when you want to bind a function to execute when the view is removed.
     *
     * @author Gaetan Brenckle
     *
     * @param removedView - {@link Consumer} - a function
     */
    public void setRemovedView(Consumer<Void> removedView) {
        this.removedView = removedView;
    }


    /**
     * Classic added method of the SceneCycle.
     * Modified to also call the added method of the view before.
     *
     * Use {@link ViewModel_SceneCycle#onViewAdded_Cycle()} instead of this method.
     *
     * @author Gaetan Brenckle
     */
    @Override
    public void onViewAdded() {
        if (this.addedView != null) {
            this.addedView.accept(null);
            this.addedView = null;
        }

        onViewAdded_Cycle();
    }

    /**
     * Classic removed method of the SceneCycle.
     * Modified to also call the removed method of the view before.
     *
     * Use {@link ViewModel_SceneCycle#onViewRemoved_Cycle()} instead of this method.
     *
     * @author Gaetan Brenckle
     */
    @Override
    public void onViewRemoved() {
        if (this.removedView != null) {
            this.removedView.accept(null);
            this.removedView = null;
        }

        onViewRemoved_Cycle();
    }
}
