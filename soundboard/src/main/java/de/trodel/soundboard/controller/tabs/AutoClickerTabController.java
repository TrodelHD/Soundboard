package de.trodel.soundboard.controller.tabs;

import de.trodel.soundboard.controller.tabs.autoclicker.AutoClickerDetailsController;
import de.trodel.soundboard.controller.tabs.autoclicker.AutoClickerListController;
import de.trodel.soundboard.execution.SoundboardExecutionProvider;
import de.trodel.soundboard.gui.tabs.AutoclickerTab;
import de.trodel.soundboard.hotkey.HotkeyProvider;
import de.trodel.soundboard.model.AutoclickModel;
import javafx.collections.ObservableList;

public class AutoClickerTabController {
    public AutoClickerTabController(AutoclickerTab autoclickerTab, ObservableList<AutoclickModel> autoclicks, HotkeyProvider hotkeyProvider,
            SoundboardExecutionProvider soundboardExecutionProvider) {
        new AutoClickerListController(autoclickerTab.getAutoClickerList(), autoclicks, hotkeyProvider);
        new AutoClickerDetailsController(
            autoclickerTab.getAutoClickerDetails(), autoclickerTab.getAutoClickerList(), hotkeyProvider, soundboardExecutionProvider
        );
    }
}
