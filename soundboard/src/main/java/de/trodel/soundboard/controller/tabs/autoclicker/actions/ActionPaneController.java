package de.trodel.soundboard.controller.tabs.autoclicker.actions;

import de.trodel.soundboard.gui.tabs.autoclicker.actions.ActionsPane;
import de.trodel.soundboard.hotkey.HotkeyProvider;

public class ActionPaneController {

    private final ActionsListController actionsListController;

    public ActionPaneController(ActionsPane actionsPane, HotkeyProvider hotkeyProvider) {
        actionsListController = new ActionsListController(actionsPane.getActionsList(), hotkeyProvider);
        new ActionEditController(actionsPane.getActionEdit(), actionsPane.getActionsList());
    }

    public ActionsListController getActionsListController() {
        return actionsListController;
    }

}
