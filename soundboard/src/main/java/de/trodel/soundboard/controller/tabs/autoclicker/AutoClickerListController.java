package de.trodel.soundboard.controller.tabs.autoclicker;

import de.trodel.soundboard.controller.ListContentChangeObserver;
import de.trodel.soundboard.gui.tabs.autoclicker.AutoClickerList;
import de.trodel.soundboard.hotkey.HotkeyProvider;
import de.trodel.soundboard.model.AutoclickModel;
import javafx.collections.ObservableList;

public class AutoClickerListController {

    public AutoClickerListController(AutoClickerList autoClickerList, ObservableList<AutoclickModel> autoclicks, HotkeyProvider hotkeyProvider) {
        new AutoClickerEditController(autoClickerList, autoclicks, hotkeyProvider);

        var listView = autoClickerList.getAutoClickList();
        listView.setItems(autoclicks);

        new ListContentChangeObserver<>(autoclicks, AutoclickModel::getNameProperty, (sound) -> {
            return (observValue, oldV, newV) -> {
                listView.refresh();
            };
        });

    }

}
