package de.trodel.soundboard.controller.tabs.autoclicker.actions;

import static de.trodel.soundboard.model.ActionModel.ActionType.key;

import java.awt.Point;
import java.util.Optional;
import java.util.UUID;

import de.trodel.soundboard.controller.ListContentChangeObserver;
import de.trodel.soundboard.gui.tabs.autoclicker.actions.ActionsList;
import de.trodel.soundboard.gui.tabs.autoclicker.actions.RecordDialog;
import de.trodel.soundboard.hotkey.HotkeyProvider;
import de.trodel.soundboard.model.ActionModel;
import javafx.collections.ObservableList;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

public class ActionsListController {
    private final ActionsList                              actionsList;
    private ListContentChangeObserver<ActionModel, String> contentChangeObserver;

    public ActionsListController(ActionsList actionsList, HotkeyProvider hotkeyProvider) {
        this.actionsList = actionsList;

        this.actionsList.getActionList().itemsProperty().addListener((observer, oldV, newV) -> {
            observeList(newV);
        });
        observeList(actionsList.getActionList().getItems());

        actionsList.getAdd().setOnAction(e -> {
            ActionModel model = new ActionModel(
                UUID.randomUUID(), "new", key, KeyCode.DIGIT0, MouseButton.PRIMARY, 1000, true, true, new Point(0, 0), ""
            );
            getSelected().ifPresentOrElse(action -> {
                var list = actionsList.getActionList().getItems();
                list.add(list.indexOf(action) + 1, model);
            }, () -> {
                actionsList.getActionList().getItems().add(model);
            });
            actionsList.getActionList().getSelectionModel().select(model);
        });

        actionsList.getRemove().setOnAction(e -> {
            getSelected().ifPresent(action -> actionsList.getActionList().getItems().remove(action));
        });

        actionsList.getUp().setOnAction(e -> {
            getSelected().ifPresent(action -> {
                var list = actionsList.getActionList().getItems();
                int i = list.indexOf(action);
                if (i > 0) {
                    list.remove(i);
                    list.add(i - 1, action);
                    actionsList.getActionList().getSelectionModel().select(action);
                }
            });
        });

        actionsList.getDown().setOnAction(e -> {
            getSelected().ifPresent(action -> {
                var list = actionsList.getActionList().getItems();
                int i = list.indexOf(action);
                if (i < list.size() - 1) {
                    list.remove(i);
                    list.add(i + 1, action);
                    actionsList.getActionList().getSelectionModel().select(action);
                }
            });
        });

        actionsList.getRecord().setOnAction(e -> {
            RecordDialog dialog = new RecordDialog();
            var valueSupplier = hotkeyProvider.record(dialog.getRecordActions());

            if (dialog.showAndWait().orElse(null) == RecordDialog.BUTTON_OK) {
                actionsList.getActionList().getItems().addAll(valueSupplier.get());
            } else {
                valueSupplier.get();
            }
        });

    }

    private Optional<ActionModel> getSelected() {
        return Optional.ofNullable(actionsList.getActionList().getSelectionModel().getSelectedItem());
    }

    private void observeList(ObservableList<ActionModel> list) {
        if (contentChangeObserver != null) {
            contentChangeObserver.dispose();
        }
        contentChangeObserver = new ListContentChangeObserver<>(
            list,
            ActionModel::getNameProperty,
            (action) -> {
                return (observValue, oldV, newV) -> {
                    actionsList.getActionList().refresh();
                };
            }
        );

    }

    public void setAutoclick(ObservableList<ActionModel> list) {
        actionsList.getActionList().setItems(list);
    }
}
