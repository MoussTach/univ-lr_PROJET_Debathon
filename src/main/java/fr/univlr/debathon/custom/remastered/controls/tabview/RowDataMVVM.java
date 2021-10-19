package fr.univlr.debathon.custom.remastered.controls.tabview;

import fr.univlr.debathon.application.viewmodel.ViewModel_SceneCycle;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public abstract class RowDataMVVM<I> extends ViewModel_SceneCycle {

    protected final ObjectProperty<I> rowData = new SimpleObjectProperty<>();

    protected void bindItem(ObjectProperty<I> property) {
        this.rowData.bind(property);
        activate();
    }

    protected abstract void activate();
}
