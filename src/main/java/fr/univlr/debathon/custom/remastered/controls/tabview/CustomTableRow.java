package fr.univlr.debathon.custom.remastered.controls.tabview;

import de.saxsys.mvvmfx.*;
import javafx.application.Platform;
import javafx.scene.control.TableRow;
import javafx.scene.Node;

public abstract class CustomTableRow<T extends FxmlView<? extends V>, V extends RowDataMVVM & ViewModel, I> extends TableRow<I> implements CustomTableRowCondition {

    private final Node sceneClassNode_;

    protected CustomTableRow(Class<T> sceneClassView) {

        final ViewTuple<T, V> dynamicTuple = FluentViewLoader.fxmlView(sceneClassView)
                .load();
        V sceneClassViewModel_ = dynamicTuple.getViewModel();
        sceneClassViewModel_.bindItem(itemProperty());

        this.sceneClassNode_ = dynamicTuple.getView();

        selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
            if (conditionVisibility() && sceneClassNode_ != null) {
                Platform.runLater(() -> {
                    if (Boolean.TRUE.equals(isNowSelected)) {
                        if (!getChildren().contains(sceneClassNode_)) {
                            getChildren().add(sceneClassNode_);
                        }
                    } else {
                        getChildren().remove(sceneClassNode_);
                    }
                    this.requestLayout();
                });
            }
        });
    }

    @Override
    public boolean conditionVisibility() {
        return true;
    }

    @Override
    protected double computePrefHeight(double width) {
        if (isSelected()) {
            return super.computePrefHeight(width)+sceneClassNode_.prefHeight(getWidth());
        } else {
            return super.computePrefHeight(width);
        }
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        if (isSelected()) {
            double width = getWidth();
            double paneHeight = sceneClassNode_.prefHeight(width);
            sceneClassNode_.resizeRelocate(0, getHeight()-paneHeight, width, paneHeight);
        }
    }
}
