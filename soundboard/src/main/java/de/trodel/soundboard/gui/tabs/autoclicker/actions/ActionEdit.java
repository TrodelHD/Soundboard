package de.trodel.soundboard.gui.tabs.autoclicker.actions;

import static de.trodel.soundboard.gui.tabs.autoclicker.actions.ActionEdit.InputOption.ACTION_TYPE;
import static de.trodel.soundboard.gui.tabs.autoclicker.actions.ActionEdit.InputOption.CMD;
import static de.trodel.soundboard.gui.tabs.autoclicker.actions.ActionEdit.InputOption.KEY_CODE;
import static de.trodel.soundboard.gui.tabs.autoclicker.actions.ActionEdit.InputOption.MOUSE_CLICK_TYPE;
import static de.trodel.soundboard.gui.tabs.autoclicker.actions.ActionEdit.InputOption.MOUSE_MOVE_TYPE;
import static de.trodel.soundboard.gui.tabs.autoclicker.actions.ActionEdit.InputOption.MOUSE_MOVE_VALUE;
import static de.trodel.soundboard.gui.tabs.autoclicker.actions.ActionEdit.InputOption.NAME;
import static de.trodel.soundboard.gui.tabs.autoclicker.actions.ActionEdit.InputOption.PRESS_TYPE;
import static de.trodel.soundboard.gui.tabs.autoclicker.actions.ActionEdit.InputOption.SLEEP_AFTER_ACTION;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static javafx.collections.FXCollections.observableArrayList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.trodel.soundboard.gui.InputUtils;
import de.trodel.soundboard.gui.NameWrapper;
import de.trodel.soundboard.model.ActionModel.ActionType;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class ActionEdit extends VBox {

    private static final Insets INSETS = new Insets(0, 5, 0, 5);

    public static enum InputOption {
        ACTION_TYPE,
        KEY_CODE,
        SLEEP_AFTER_ACTION,
        NAME,
        MOUSE_MOVE_VALUE,
        CMD,
        MOUSE_CLICK_TYPE,
        MOUSE_MOVE_TYPE,
        PRESS_TYPE
    }

    private final ComboBox<ActionType>           actionType       = new ComboBox<>();
    private final ComboBox<NameWrapper<KeyCode>> keyCode          = new ComboBox<>();
    private final TextField                      sleepAfterAction = new TextField();
    private final TextField                      name             = new TextField();
    private final TextField                      horizontal       = new TextField();
    private final TextField                      vertical         = new TextField();
    private final TextField                      cmd              = new TextField();

    private final ToggleGroup mouseClickType   = new ToggleGroup();
    private final RadioButton mouseClickLeft   = new RadioButton();
    private final RadioButton mouseClickMiddle = new RadioButton();
    private final RadioButton mouseClickRight  = new RadioButton();

    private final ToggleGroup mouseMoveType = new ToggleGroup();
    private final RadioButton mouseMoveSoft = new RadioButton();
    private final RadioButton mouseMoveHard = new RadioButton();

    private final ToggleGroup pressType = new ToggleGroup();
    private final RadioButton pressDown = new RadioButton();
    private final RadioButton pressUp   = new RadioButton();

    private final Map<InputOption, List<Node>> options;

    public ActionEdit() {
        super(10);
        options = new HashMap<>();
        //Setup

        actionType.setItems(observableArrayList(stream(ActionType.values()).collect(toList())));
        keyCode.setItems(
            observableArrayList(
                stream(KeyCode.values())
                    .sorted((o1, o2) -> o1.getName().compareTo(o2.getName()))
                    .map(e -> new NameWrapper<>(e, KeyCode::name))
                    .collect(toList())
            )
        );

        mouseClickLeft.setToggleGroup(mouseClickType);
        mouseClickMiddle.setToggleGroup(mouseClickType);
        mouseClickRight.setToggleGroup(mouseClickType);

        mouseMoveSoft.setToggleGroup(mouseMoveType);
        mouseMoveHard.setToggleGroup(mouseMoveType);

        pressDown.setToggleGroup(pressType);
        pressUp.setToggleGroup(pressType);

        mouseClickLeft.setText("Mouse left");
        mouseClickMiddle.setText("Mouse middle");
        mouseClickRight.setText("Mouse right");

        mouseMoveSoft.setText("Soft move");
        mouseMoveHard.setText("Hard move");

        pressDown.setText("Press button");
        pressUp.setText("Release button");

        //Limit input

        InputUtils.onlyInt(horizontal);
        InputUtils.onlyInt(vertical);
        InputUtils.onlyLong(sleepAfterAction);

        //"Place"

        add(withHeader("Actiontype:", actionType), ACTION_TYPE);
        add(withHeader("Choose key to press:", keyCode), KEY_CODE);
        add(mouseClickLeft, MOUSE_CLICK_TYPE);
        add(mouseClickMiddle, MOUSE_CLICK_TYPE);
        add(mouseClickRight, MOUSE_CLICK_TYPE);
        add(withHeader("Horizontal:", horizontal), MOUSE_MOVE_VALUE);
        add(withHeader("Vertical:", vertical), MOUSE_MOVE_VALUE);
        add(withHeader("Command:", cmd), CMD);
        add(withHeader("Sleep after action (ms):", sleepAfterAction), SLEEP_AFTER_ACTION);
        add(withHeader("Name:", name), NAME);
        add(pressDown, PRESS_TYPE);
        add(pressUp, PRESS_TYPE);
        add(mouseMoveSoft, MOUSE_MOVE_TYPE);
        add(mouseMoveHard, MOUSE_MOVE_TYPE);
    }

    private void add(Region region, InputOption option) {
        options.computeIfAbsent(option, __ -> new ArrayList<>()).add(region);

        VBox.setMargin(region, INSETS);
    }

    private VBox withHeader(String header, Region region) {
        VBox vBox = new VBox(1);
        Label label = new Label(header);
        vBox.getChildren().addAll(label, region);
        region.prefWidthProperty().bind(vBox.widthProperty());
        return vBox;
    }

    public void setOptions(InputOption... inputOptions) {
        getChildren().clear();
        stream(inputOptions).forEach(io -> getChildren().addAll(options.get(io)));

    }

    public ComboBox<ActionType> getActionType() {
        return actionType;
    }

    public ComboBox<NameWrapper<KeyCode>> getKeyCode() {
        return keyCode;
    }

    public TextField getSleepAfterAction() {
        return sleepAfterAction;
    }

    public TextField getName() {
        return name;
    }

    public TextField getHorizontal() {
        return horizontal;
    }

    public TextField getVertical() {
        return vertical;
    }

    public TextField getCmd() {
        return cmd;
    }

    public ToggleGroup getMouseClickType() {
        return mouseClickType;
    }

    public RadioButton getMouseClickLeft() {
        return mouseClickLeft;
    }

    public RadioButton getMouseClickMiddle() {
        return mouseClickMiddle;
    }

    public RadioButton getMouseClickRight() {
        return mouseClickRight;
    }

    public ToggleGroup getMouseMoveType() {
        return mouseMoveType;
    }

    public RadioButton getMouseMoveHard() {
        return mouseMoveHard;
    }

    public RadioButton getMouseMoveSoft() {
        return mouseMoveSoft;
    }

    public ToggleGroup getPressType() {
        return pressType;
    }

    public RadioButton getPressDown() {
        return pressDown;
    }

    public RadioButton getPressUp() {
        return pressUp;
    }

}
