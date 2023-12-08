package de.trodel.soundboard.controller.tabs.autoclicker.actions;

import static de.trodel.soundboard.gui.tabs.autoclicker.actions.ActionEdit.InputOption.ACTION_TYPE;
import static de.trodel.soundboard.gui.tabs.autoclicker.actions.ActionEdit.InputOption.CMD;
import static de.trodel.soundboard.gui.tabs.autoclicker.actions.ActionEdit.InputOption.KEY_CODE;
import static de.trodel.soundboard.gui.tabs.autoclicker.actions.ActionEdit.InputOption.MOUSE_CLICK_TYPE;
import static de.trodel.soundboard.gui.tabs.autoclicker.actions.ActionEdit.InputOption.MOUSE_MOVE_TYPE;
import static de.trodel.soundboard.gui.tabs.autoclicker.actions.ActionEdit.InputOption.MOUSE_MOVE_VALUE;
import static de.trodel.soundboard.gui.tabs.autoclicker.actions.ActionEdit.InputOption.NAME;
import static de.trodel.soundboard.gui.tabs.autoclicker.actions.ActionEdit.InputOption.PRESS_TYPE;
import static de.trodel.soundboard.gui.tabs.autoclicker.actions.ActionEdit.InputOption.SLEEP_AFTER_ACTION;
import static javafx.scene.input.MouseButton.MIDDLE;
import static javafx.scene.input.MouseButton.PRIMARY;
import static javafx.scene.input.MouseButton.SECONDARY;

import java.util.Optional;
import java.util.OptionalInt;
import java.util.OptionalLong;

import de.trodel.soundboard.gui.NameWrapper;
import de.trodel.soundboard.gui.tabs.autoclicker.actions.ActionEdit;
import de.trodel.soundboard.gui.tabs.autoclicker.actions.ActionEdit.InputOption;
import de.trodel.soundboard.gui.tabs.autoclicker.actions.ActionsList;
import de.trodel.soundboard.model.ActionModel;
import de.trodel.soundboard.model.ActionModel.ActionType;
import javafx.scene.control.Toggle;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

public class ActionEditController {

    private final ActionEdit  actionEdit;
    private final ActionsList actionsList;

    public ActionEditController(ActionEdit actionEdit, ActionsList actionsList) {
        this.actionEdit = actionEdit;
        this.actionsList = actionsList;
        actionsList.getActionList().getSelectionModel().selectedItemProperty().addListener((observer, oldV, newV) -> {
            setAction(newV);
        });

        actionEdit.getActionType().valueProperty().addListener((observable, oldV, newV) -> {
            getSelected().ifPresent(action -> {
                action.setType(newV);
                setAction(action);
            });

        });

        actionEdit.getKeyCode().valueProperty().addListener((observable, oldV, newV) -> {
            getSelected().ifPresent(action -> action.setKeyCode(newV.getValue()));
        });

        actionEdit.getSleepAfterAction().textProperty().addListener((observable, oldV, newV) -> {
            getSelected().ifPresent(action -> getLong(newV).ifPresent(value -> action.setSleepAfterAction(value)));
        });

        actionEdit.getName().textProperty().addListener((observable, oldV, newV) -> {
            getSelected().ifPresent(action -> action.setName(newV));
        });

        actionEdit.getHorizontal().textProperty().addListener((observable, oldV, newV) -> {
            getSelected().ifPresent(action -> getInt(newV).ifPresent(value -> action.setMouseMoveX(value)));
        });

        actionEdit.getVertical().textProperty().addListener((observable, oldV, newV) -> {
            getSelected().ifPresent(action -> getInt(newV).ifPresent(value -> action.setMouseMoveY(value)));
        });

        actionEdit.getCmd().textProperty().addListener((observable, oldV, newV) -> {
            getSelected().ifPresent(action -> action.setCmd(newV));
        });

        actionEdit.getMouseClickType().selectedToggleProperty().addListener((observable, oldV, newV) -> {
            getSelected().ifPresent(action -> mouseButton().ifPresent(button -> action.setMouseButton(button)));
        });

        actionEdit.getMouseMoveType().selectedToggleProperty().addListener((observable, oldV, newV) -> {
            getSelected().ifPresent(action -> isSoft().ifPresent(soft -> action.setSoft(soft)));
        });

        actionEdit.getPressType().selectedToggleProperty().addListener((observable, oldV, newV) -> {
            getSelected().ifPresent(action -> isDown().ifPresent(down -> action.setDown(down)));
        });

        setAction(getSelected().orElse(null));
    }

    private void setAction(ActionModel model) {
        if (model == null) {
            actionEdit.setOptions();
            actionEdit.getActionType().getSelectionModel().select(null);
            actionEdit.getKeyCode().getSelectionModel().select(null);
            actionEdit.getSleepAfterAction().setText("");
            actionEdit.getName().setText("");
            actionEdit.getHorizontal().setText("");
            actionEdit.getVertical().setText("");
            actionEdit.getCmd().setText("");
            actionEdit.getMouseClickType().selectToggle(null);
            actionEdit.getMouseMoveType().selectToggle(null);
            actionEdit.getPressType().selectToggle(null);
            actionEdit.setDisable(true);
            return;
        }
        actionEdit.setOptions(getOptions(model.getType()));
        actionEdit.getActionType().getSelectionModel().select(model.getType());
        actionEdit.getKeyCode().getSelectionModel().select(new NameWrapper<>(model.getKeyCode(), KeyCode::name));
        actionEdit.getSleepAfterAction().setText(Long.toString(model.getSleepAfterAction()));
        actionEdit.getName().setText(model.getName());
        actionEdit.getHorizontal().setText(Integer.toString(model.getMouseMove().x));
        actionEdit.getVertical().setText(Integer.toString(model.getMouseMove().y));
        actionEdit.getCmd().setText(model.getCmd());
        actionEdit.getMouseClickType().selectToggle(toMouseButton(model.getMouseButton()));
        actionEdit.getMouseMoveType().selectToggle(toSoft(model.getSoft()));
        actionEdit.getPressType().selectToggle(toDown(model.getDown()));
        actionEdit.setDisable(false);
    }

    private InputOption[] getOptions(ActionType type) {
        switch (type) {
            case key:
                return new InputOption[] { ACTION_TYPE, KEY_CODE, SLEEP_AFTER_ACTION, NAME, PRESS_TYPE };
            case mouse:
                return new InputOption[] { ACTION_TYPE, MOUSE_CLICK_TYPE, SLEEP_AFTER_ACTION, NAME, PRESS_TYPE };
            case mouseMove:
                return new InputOption[] { ACTION_TYPE, MOUSE_MOVE_VALUE, SLEEP_AFTER_ACTION, NAME, MOUSE_MOVE_TYPE };
            case stop:
                return new InputOption[] { ACTION_TYPE, NAME };
            case cmd:
                return new InputOption[] { ACTION_TYPE, CMD, SLEEP_AFTER_ACTION, NAME };

        }
        throw new IllegalArgumentException("Unexspected value: " + type);
    }

    private Toggle toSoft(boolean soft) {
        return soft ? actionEdit.getMouseMoveSoft() : actionEdit.getMouseMoveHard();
    }

    private Toggle toDown(boolean down) {
        return down ? actionEdit.getPressDown() : actionEdit.getPressUp();
    }

    private Toggle toMouseButton(MouseButton button) {
        switch (button) {
            case PRIMARY:
                return actionEdit.getMouseClickLeft();
            case MIDDLE:
                return actionEdit.getMouseClickMiddle();
            case SECONDARY:
                return actionEdit.getMouseClickRight();
            default:
                throw new IllegalArgumentException("Unexspected value: " + button);
        }
    }

    private Optional<Boolean> isSoft() {
        if (actionEdit.getMouseMoveSoft().equals(actionEdit.getMouseMoveType().getSelectedToggle())) {
            return Optional.of(true);
        } else if (actionEdit.getMouseMoveHard().equals(actionEdit.getMouseMoveType().getSelectedToggle())) {
            return Optional.of(false);
        }
        return Optional.empty();
    }

    private Optional<Boolean> isDown() {
        if (actionEdit.getPressDown().equals(actionEdit.getPressType().getSelectedToggle())) {
            return Optional.of(true);
        } else if (actionEdit.getPressUp().equals(actionEdit.getPressType().getSelectedToggle())) {
            return Optional.of(false);
        }
        return Optional.empty();
    }

    private Optional<MouseButton> mouseButton() {
        if (actionEdit.getMouseClickLeft().equals(actionEdit.getMouseClickType().getSelectedToggle())) {
            return Optional.of(PRIMARY);
        } else if (actionEdit.getMouseClickMiddle().equals(actionEdit.getMouseClickType().getSelectedToggle())) {
            return Optional.of(MIDDLE);
        } else if (actionEdit.getMouseClickRight().equals(actionEdit.getMouseClickType().getSelectedToggle())) {
            return Optional.of(SECONDARY);
        }
        return Optional.empty();
    }

    private OptionalLong getLong(String string) {
        try {
            return OptionalLong.of(Long.parseLong(string));
        } catch (NumberFormatException e) {
            return OptionalLong.empty();
        }
    }

    private OptionalInt getInt(String string) {
        try {
            return OptionalInt.of(Integer.parseInt(string));
        } catch (NumberFormatException e) {
            return OptionalInt.empty();
        }
    }

    private Optional<ActionModel> getSelected() {
        return Optional.ofNullable(actionsList.getActionList().getSelectionModel().getSelectedItem());
    }

}
