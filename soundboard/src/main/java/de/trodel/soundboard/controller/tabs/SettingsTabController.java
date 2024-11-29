package de.trodel.soundboard.controller.tabs;

import java.util.Arrays;
import java.util.stream.Collectors;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;

import de.trodel.soundboard.gui.NameWrapper;
import de.trodel.soundboard.gui.tabs.SettingsTab;
import de.trodel.soundboard.hotkey.HotkeyProvider;
import de.trodel.soundboard.model.SettingsModel;
import javafx.application.Platform;

public class SettingsTabController {

    public SettingsTabController(SettingsTab settings, SettingsModel model, HotkeyProvider hotkeyProvider) {

        settings.getOutputDevice().getSelectionModel().select(new NameWrapper<>(model.getOutputDevice(), null));

        settings.getOutputDevice().valueProperty().addListener((observable, oldV, newV) -> {
            model.setOutputDevice(newV.getValue());
        });

        settings.getSoundStopHotKey().setText(getSoundStopHotkeyText(model.getSoundStopHotkey()));
        settings.getSoundStopHotKey().setOnAction(event -> {
            settings.getSoundStopHotKey().setText("Waiting...");
            hotkeyProvider.combination(arr -> {
                Platform.runLater(() -> {
                    model.setSoundStopHotkey(arr);
                    settings.getSoundStopHotKey().setText(getSoundStopHotkeyText(model.getSoundStopHotkey()));
                });
            });
        });
    }

    private String getSoundStopHotkeyText(int[] soundStopHotkey) {
        String keys = Arrays.stream(soundStopHotkey)
            .mapToObj(key -> NativeKeyEvent.getKeyText(key))
            .collect(Collectors.joining(", "));

        return keys.length() == 0 ? "<NONE>" : keys;
    }

}
