package de.trodel.soundboard.gui.tabs.autoclicker.actions;

import static javafx.geometry.Orientation.HORIZONTAL;

import de.trodel.soundboard.gui.LayoutUtils;
import javafx.scene.control.SplitPane;

public class ActionsPane extends SplitPane {

    private final ActionsList actionsList = new ActionsList();
    private final ActionEdit  actionEdit  = new ActionEdit();

    public ActionsPane() {
        setOrientation(HORIZONTAL);

        getItems().addAll(actionsList, actionEdit);
        setDividerPositions(0.5d);
        setDividerPositions(getDividerPositions());

        LayoutUtils.keepHorizontalDividerAtPixel(this, 200);

    }

    public ActionsList getActionsList() {
        return actionsList;
    }

    public ActionEdit getActionEdit() {
        return actionEdit;
    }
}
