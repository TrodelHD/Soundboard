package de.trodel.soundboard.controller.tabs.autoclicker;

import static java.util.Optional.ofNullable;
import static javafx.collections.FXCollections.observableArrayList;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import org.jnativehook.keyboard.NativeKeyEvent;

import de.trodel.soundboard.controller.tabs.autoclicker.actions.ActionPaneController;
import de.trodel.soundboard.execution.SoundboardExecutionProvider;
import de.trodel.soundboard.gui.tabs.autoclicker.AutoClickerDetails;
import de.trodel.soundboard.gui.tabs.autoclicker.AutoClickerList;
import de.trodel.soundboard.hotkey.HotkeyProvider;
import de.trodel.soundboard.model.AutoclickModel;
import de.trodel.soundboard.model.HotkeyModel;
import javafx.beans.value.ChangeListener;

public class AutoClickerDetailsController {
    private final ActionPaneController actionPaneController;
    private final AutoClickerDetails   autoClickerDetails;
    private final AutoClickerList      autoClickerList;
    private Optional<Runnable>         onChange = Optional.empty();

    public AutoClickerDetailsController(AutoClickerDetails autoClickerDetails, AutoClickerList autoClickerList, HotkeyProvider hotkeyProvider,
            SoundboardExecutionProvider soundboardExecutionProvider) {
        actionPaneController = new ActionPaneController(autoClickerDetails.getActionsPane(), hotkeyProvider);

        this.autoClickerDetails = autoClickerDetails;
        this.autoClickerList = autoClickerList;

        autoClickerDetails.getAutorealease().selectedProperty().addListener((observer, oldV, newV) -> {
            getSelected().ifPresent(am -> am.setAutoRelease(newV));
        });

        autoClickerList.getAutoClickList().getSelectionModel().selectedItemProperty().addListener((observer, oldV, newV) -> {
            select(newV);
        });

        autoClickerDetails.getPlay().setOnAction(event -> {
            getSelected().ifPresent(ac -> soundboardExecutionProvider.executeAutoclick(ac));
        });

        autoClickerDetails.getStop().setOnAction(event -> {
            getSelected().ifPresent(ac -> soundboardExecutionProvider.stopAutoclick(ac.getId()));
        });

        select(getSelected().orElse(null));

    }

    private Optional<AutoclickModel> getSelected() {
        return Optional.ofNullable(autoClickerList.getAutoClickList().getSelectionModel().getSelectedItem());
    }

    private void select(AutoclickModel model) {
        onChange.ifPresent(Runnable::run);
        onChange = Optional.empty();
        actionPaneController.getActionsListController()
            .setAutoclick(ofNullable(model).map(AutoclickModel::getActions).orElseGet(() -> observableArrayList()));

        if (model == null) {
            autoClickerDetails.getAutorealease().setSelected(false);
            autoClickerDetails.setDisable(true);
            autoClickerDetails.getInfo().setText("Name:\nHotkey:");
            return;
        }

        autoClickerDetails.getAutorealease().setSelected(model.getAutoRelease());
        autoClickerDetails.setDisable(false);
        autoClickerDetails.getInfo().setText(getInfoText(model));

        ChangeListener<String> nameList = (observer, oldV, newV) -> {
            autoClickerDetails.getInfo().setText(getInfoText(model));
        };
        model.getNameProperty().addListener(nameList);

        ChangeListener<HotkeyModel> hotkeyList = (observer, oldV, newV) -> {
            autoClickerDetails.getInfo().setText(getInfoText(model));
        };
        model.getHotkeyProperty().addListener(hotkeyList);

        onChange = Optional.of(() -> {
            model.getNameProperty().removeListener(nameList);
            model.getHotkeyProperty().removeListener(hotkeyList);
        });
    }

    private String getInfoText(AutoclickModel model) {

        String keys = Arrays.stream(model.getHotkeyProperty().get().getHotkey())
            .mapToObj(key -> NativeKeyEvent.getKeyText(key))
            .collect(Collectors.joining(", "));

        return "Name: " + model.getName() + "\nHotkey: " + keys;
    }
}
