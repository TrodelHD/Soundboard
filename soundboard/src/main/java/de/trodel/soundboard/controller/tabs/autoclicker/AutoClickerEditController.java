package de.trodel.soundboard.controller.tabs.autoclicker;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static javafx.scene.control.Alert.AlertType.WARNING;
import static javafx.scene.control.ButtonType.NO;
import static javafx.scene.control.ButtonType.YES;

import java.awt.Point;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import de.trodel.soundboard.gui.tabs.autoclicker.AutoClickerDialog;
import de.trodel.soundboard.gui.tabs.autoclicker.AutoClickerList;
import de.trodel.soundboard.gui.tabs.soundboard.SoundDialog;
import de.trodel.soundboard.hotkey.HotkeyProvider;
import de.trodel.soundboard.model.ActionModel;
import de.trodel.soundboard.model.AutoclickModel;
import de.trodel.soundboard.model.HotkeyModel;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

public class AutoClickerEditController {
    public AutoClickerEditController(AutoClickerList autoClickerList, ObservableList<AutoclickModel> autoclickers, HotkeyProvider hotkeyProvider) {

        autoClickerList.getListEditPane().getAdd().setOnAction(action -> {
            AutoclickModel model = new AutoclickModel(UUID.randomUUID(), "", emptyList(), true, new HotkeyModel(new int[0]));
            if (new AutoClickerDialog(model, false, hotkeyProvider).showAndWait().orElse(null) == SoundDialog.BUTTON_OK) {
                autoclickers.add(model);
            }
        });

        autoClickerList.getListEditPane().getEdit().setOnAction(action -> {
            Optional.ofNullable(autoClickerList.getAutoClickList().getSelectionModel().getSelectedItem())
                .ifPresent(model -> {
                    if (new AutoClickerDialog(model, true, hotkeyProvider).showAndWait().orElse(null) == AutoClickerDialog.BUTTON_CLONE) {
                        autoclickers.add(clone(model));
                    }
                });
        });

        autoClickerList.getListEditPane().getRemove().setOnAction(action -> {

            Optional.ofNullable(autoClickerList.getAutoClickList().getSelectionModel().getSelectedItem())
                .ifPresent(model -> {
                    Alert alert = new Alert(WARNING, "Do you realy want to delete '" + model.getName() + "'?", YES, NO);
                    if (alert.showAndWait().orElse(null) == YES) {
                        autoclickers.remove(model);
                    }
                });
        });

    }

    private static AutoclickModel clone(AutoclickModel model) {
        return new AutoclickModel(
            UUID.randomUUID(),
            model.getName(),
            model.getActions().stream().map(AutoClickerEditController::clone).collect(toList()),
            model.getAutoRelease(),
            new HotkeyModel(Arrays.copyOf(model.getHotkey(), model.getHotkey().length))
        );
    }

    private static ActionModel clone(ActionModel model) {
        return new ActionModel(
            UUID.randomUUID(),
            model.getName(),
            model.getType(),
            model.getKeyCode(),
            model.getMouseButton(),
            model.getSleepAfterAction(),
            model.getDown(),
            model.getSoft(),
            new Point(model.getMouseMove().x, model.getMouseMove().y),
            model.getCmd()
        );
    }
}
