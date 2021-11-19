package fr.univlr.debathon.application.viewmodel.mainwindow.debate.question;

import de.saxsys.mvvmfx.Scope;
import javafx.scene.control.ToggleGroup;

public class ResponseScope implements Scope {

    private final ToggleGroup tGroup = new ToggleGroup();

    public ToggleGroup gettGroup() {
        return tGroup;
    }
}
