package de.trodel.soundboard.gui;

import de.trodel.soundboard.gui.tabs.AutoclickerTab;
import de.trodel.soundboard.gui.tabs.ServerTab;
import de.trodel.soundboard.gui.tabs.SettingsTab;
import de.trodel.soundboard.gui.tabs.SoundboardTab;
import de.trodel.soundboard.gui.tabs.VisualizerTab;
import javafx.scene.control.TabPane;

public class MainTabPane extends TabPane {

    private final SoundboardTab  soundboardTab  = new SoundboardTab();
    private final AutoclickerTab autoclickerTab = new AutoclickerTab();
    private final ServerTab      serverTab      = new ServerTab();
    private final VisualizerTab  visualizerTab  = new VisualizerTab();
    private final SettingsTab    settingsTab    = new SettingsTab();

    public MainTabPane() {
        getTabs().add(soundboardTab);
        getTabs().add(autoclickerTab);
        getTabs().add(serverTab);
        getTabs().add(visualizerTab);
        getTabs().add(settingsTab);

    }

    public SoundboardTab getSoundboardTab() {
        return soundboardTab;
    }

    public AutoclickerTab getAutoclickerTab() {
        return autoclickerTab;
    }

    public ServerTab getServerTab() {
        return serverTab;
    }

    public VisualizerTab getVisualizerTab() {
        return visualizerTab;
    }

    public SettingsTab getSettingsTab() {
        return settingsTab;
    }

}
