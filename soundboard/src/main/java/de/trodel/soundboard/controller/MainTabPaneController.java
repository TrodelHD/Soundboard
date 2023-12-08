package de.trodel.soundboard.controller;

import de.trodel.soundboard.controller.tabs.AutoClickerTabController;
import de.trodel.soundboard.controller.tabs.ServerTabController;
import de.trodel.soundboard.controller.tabs.SettingsTabController;
import de.trodel.soundboard.controller.tabs.SoundboardTabController;
import de.trodel.soundboard.controller.tabs.VisualizerTabController;
import de.trodel.soundboard.execution.SoundboardExecutionProvider;
import de.trodel.soundboard.gui.MainTabPane;
import de.trodel.soundboard.hotkey.HotkeyProvider;
import de.trodel.soundboard.model.MainModel;
import de.trodel.soundboard.server.ServerManager;

public class MainTabPaneController {

    public MainTabPaneController(MainTabPane mainTabPane, MainModel mainModel,
            HotkeyProvider hotkeyProvider,
            SoundboardExecutionProvider soundboardExecutionProvider,
            ServerManager serverManager) {
        new SoundboardTabController(mainTabPane.getSoundboardTab(), mainModel.getSounds(), hotkeyProvider, soundboardExecutionProvider);
        new AutoClickerTabController(mainTabPane.getAutoclickerTab(), mainModel.getAutoclicker(), hotkeyProvider, soundboardExecutionProvider);
        new SettingsTabController(mainTabPane.getSettingsTab(), mainModel.getSettings(), hotkeyProvider);
        new ServerTabController(mainTabPane.getServerTab(), mainModel.getServer(), serverManager);
        new VisualizerTabController(mainTabPane.getVisualizerTab(), soundboardExecutionProvider.getSoundListenerManager());
    }

}
