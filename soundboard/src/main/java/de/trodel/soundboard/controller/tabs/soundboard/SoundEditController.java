package de.trodel.soundboard.controller.tabs.soundboard;

import static de.trodel.soundboard.execution.SoundExecutorJavaFX.getDeaultBands;
import static java.util.stream.Collectors.toList;
import static javafx.scene.control.Alert.AlertType.WARNING;
import static javafx.scene.control.ButtonType.NO;
import static javafx.scene.control.ButtonType.YES;

import java.util.Optional;
import java.util.UUID;

import de.trodel.soundboard.gui.tabs.soundboard.SoundDialog;
import de.trodel.soundboard.gui.tabs.soundboard.SoundsList;
import de.trodel.soundboard.hotkey.HotkeyProvider;
import de.trodel.soundboard.model.EqualizerBandModel;
import de.trodel.soundboard.model.HotkeyModel;
import de.trodel.soundboard.model.SoundModel;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

public class SoundEditController {
    public SoundEditController(SoundsList soundsList, ObservableList<SoundModel> sounds, HotkeyProvider hotkeyProvider) {

        soundsList.getListEditPane().getAdd().setOnAction(action -> {
            SoundModel model = new SoundModel(
                UUID.randomUUID(),
                "",
                "",
                1,
                getDeaultBands().stream()
                    .map(band -> new EqualizerBandModel(band.getCenterFrequency(), band.getBandwidth(), band.getGain()))
                    .collect(toList()),
                new HotkeyModel(new int[0])
            );
            if (new SoundDialog(model, hotkeyProvider).showAndWait().orElse(null) == SoundDialog.BUTTON_OK) {
                sounds.add(model);
            }
        });

        soundsList.getListEditPane().getEdit().setOnAction(action -> {
            Optional.ofNullable(soundsList.getSoundList().getSelectionModel().getSelectedItem())
                .ifPresent(model -> {
                    new SoundDialog(model, hotkeyProvider).showAndWait();
                });
        });

        soundsList.getListEditPane().getRemove().setOnAction(action -> {

            Optional.ofNullable(soundsList.getSoundList().getSelectionModel().getSelectedItem())
                .ifPresent(model -> {
                    Alert alert = new Alert(WARNING, "Do you realy want to delete '" + model.getName() + "'?", YES, NO);
                    if (alert.showAndWait().orElse(null) == YES) {
                        sounds.remove(model);
                    }
                });
        });

    }
}
