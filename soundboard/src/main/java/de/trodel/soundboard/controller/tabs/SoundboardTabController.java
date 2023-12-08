package de.trodel.soundboard.controller.tabs;

import de.trodel.soundboard.controller.tabs.soundboard.SoundDetailsController;
import de.trodel.soundboard.controller.tabs.soundboard.SoundsListController;
import de.trodel.soundboard.execution.SoundboardExecutionProvider;
import de.trodel.soundboard.gui.tabs.SoundboardTab;
import de.trodel.soundboard.hotkey.HotkeyProvider;
import de.trodel.soundboard.model.SoundModel;
import javafx.collections.ObservableList;

public class SoundboardTabController {
    public SoundboardTabController(SoundboardTab soundboardTab, ObservableList<SoundModel> sounds, HotkeyProvider hotkeyProvider,
            SoundboardExecutionProvider soundboardExecutionProvider) {
        new SoundsListController(soundboardTab.getSoundsList(), sounds, hotkeyProvider);
        new SoundDetailsController(soundboardTab.getSoundDetails(), soundboardTab.getSoundsList(), soundboardExecutionProvider);
    }
}
