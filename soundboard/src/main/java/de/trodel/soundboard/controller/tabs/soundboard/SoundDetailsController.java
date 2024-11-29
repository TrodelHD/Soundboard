package de.trodel.soundboard.controller.tabs.soundboard;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;

import de.trodel.soundboard.execution.SoundboardExecutionProvider;
import de.trodel.soundboard.gui.tabs.soundboard.SoundDetails;
import de.trodel.soundboard.gui.tabs.soundboard.SoundsList;
import de.trodel.soundboard.model.HotkeyModel;
import de.trodel.soundboard.model.SoundModel;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.Slider;

public class SoundDetailsController {
    private final SoundDetails soundDetails;
    private final SoundsList   soundsList;
    private Optional<Runnable> onChange = Optional.empty();

    public SoundDetailsController(SoundDetails soundDetails, SoundsList soundsList, SoundboardExecutionProvider soundboardExecutionProvider) {
        this.soundDetails = soundDetails;
        this.soundsList = soundsList;
        soundsList.getSoundList().getSelectionModel().selectedItemProperty().addListener((observer, oldV, newV) -> {
            select(newV);
        });

        soundDetails.getVolume().valueProperty().addListener((observer, oldV, newV) -> {
            getSelected().ifPresent(sm -> sm.setVolume(newV.floatValue() / 100f));
        });

        soundDetails.getPlay().setOnAction(action -> {
            getSelected().ifPresent(sm -> soundboardExecutionProvider.executeSound(sm));
        });

        soundDetails.getStop().setOnAction(action -> {
            getSelected().ifPresent(sm -> soundboardExecutionProvider.stopSound());
        });

        select(getSelected().orElse(null));

    }

    private Optional<SoundModel> getSelected() {
        return Optional.ofNullable(soundsList.getSoundList().getSelectionModel().getSelectedItem());
    }

    private void select(SoundModel model) {
        onChange.ifPresent(Runnable::run);
        onChange = Optional.empty();

        if (model == null) {
            soundDetails.setDisable(true);
            soundDetails.getInfo().setText("Name:\nHotkey:");
            soundDetails.getGain().clear();
            soundDetails.getVolume().setValue(100);
            return;
        }

        soundDetails.setDisable(false);
        soundDetails.getInfo().setText(getInfoText(model));
        soundDetails.getGain().clear();
        soundDetails.getVolume().setValue(model.getVolume() * 100);

        ChangeListener<String> nameList = (observer, oldV, newV) -> {
            soundDetails.getInfo().setText(getInfoText(model));
        };
        model.getNameProperty().addListener(nameList);

        ChangeListener<HotkeyModel> hotkeyList = (observer, oldV, newV) -> {
            soundDetails.getInfo().setText(getInfoText(model));
        };
        model.getHotkeyProperty().addListener(hotkeyList);

        model.getBands().forEach(band -> {
            Slider slider = soundDetails.getGain().createGainSlider(band.getCenterFrequency(), band.getBandwidth());
            slider.setValue(band.getGain());
            slider.valueProperty().addListener((observable, oldV, newV) -> {
                band.setGain(newV.doubleValue());
            });
        });

        onChange = Optional.of(() -> {
            model.getNameProperty().removeListener(nameList);
            model.getHotkeyProperty().removeListener(hotkeyList);
        });
    }

    private String getInfoText(SoundModel soundModel) {

        String keys = Arrays.stream(soundModel.getHotkeyProperty().get().getHotkey())
            .mapToObj(key -> NativeKeyEvent.getKeyText(key))
            .collect(Collectors.joining(", "));

        return "Name: " + soundModel.getName() + "\nHotkey: " + keys;
    }
}
