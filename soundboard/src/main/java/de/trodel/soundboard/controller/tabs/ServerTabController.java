package de.trodel.soundboard.controller.tabs;

import static de.trodel.soundboard.model.ServerModel.ServerMode.disabled;
import static de.trodel.soundboard.model.ServerModel.ServerMode.enabled;

import de.trodel.soundboard.gui.tabs.ServerTab;
import de.trodel.soundboard.model.ServerModel;
import de.trodel.soundboard.server.ServerManager;

public class ServerTabController {

    public ServerTabController(ServerTab serverTab, ServerModel serverModel, ServerManager serverManager) {

        serverTab.getAuthorizations().setItems(serverManager.getTokens());

        serverTab.getKey().setText(serverModel.getKey());
        serverModel.getKeyProperty().bind(serverTab.getKey().textProperty());

        serverTab.getPort().setText(Integer.toString(serverModel.getPort()));
        serverTab.getPort().textProperty().addListener((observable, oldV, newV) -> {
            try {
                serverTab.getEnabled().setSelected(false);
                serverModel.setPort(Integer.parseInt(newV));
            } catch (NumberFormatException e) {
                //Nothing todo here
            }
        });

        serverTab.getEnabled().setSelected(serverModel.getMode() == enabled);
        serverTab.getEnabled().selectedProperty().addListener((observable, oldV, newV) -> {
            serverModel.setMode(newV ? enabled : disabled);
        });

    }

}
