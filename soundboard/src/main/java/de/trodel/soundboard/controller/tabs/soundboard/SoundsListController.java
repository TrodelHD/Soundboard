package de.trodel.soundboard.controller.tabs.soundboard;

import de.trodel.soundboard.controller.ListContentChangeObserver;
import de.trodel.soundboard.gui.tabs.soundboard.SoundsList;
import de.trodel.soundboard.hotkey.HotkeyProvider;
import de.trodel.soundboard.model.SoundModel;
import javafx.collections.ObservableList;

public class SoundsListController {

    public SoundsListController(SoundsList soundsList, ObservableList<SoundModel> sounds, HotkeyProvider hotkeyProvider) {
        new SoundEditController(soundsList, sounds, hotkeyProvider);

        var listView = soundsList.getSoundList();
        listView.setItems(sounds);

        new ListContentChangeObserver<>(sounds, SoundModel::getNameProperty, (sound) -> {
            return (observValue, oldV, newV) -> {
                listView.refresh();
            };
        });

    }

}
