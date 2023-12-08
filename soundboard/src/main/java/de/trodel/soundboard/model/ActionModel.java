package de.trodel.soundboard.model;

import java.awt.Point;
import java.util.Objects;
import java.util.UUID;

import org.json.JSONObject;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

public class ActionModel extends ObservableModel {

    public enum ActionType {
        key("Key Press"),
        mouse("Mouse click"),
        mouseMove("Mouse move"),
        stop("Stop"),
        cmd("CMD");

        public final String name;

        private ActionType(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private final UUID                        id;
    private final StringProperty              name;
    private final ObjectProperty<ActionType>  type;
    private final ObjectProperty<KeyCode>     keyCode;
    private final ObjectProperty<MouseButton> mouseButton;
    private final LongProperty                sleepAfterAction;
    private final BooleanProperty             down;
    private final BooleanProperty             soft;
    private final ObjectProperty<Point>       mouseMove;
    private final StringProperty              cmd;

    public ActionModel(final UUID id, final String name, final ActionType type, final KeyCode keyCode, MouseButton mouseButton,
            final long sleepAfterAction, final boolean down, final boolean soft, final Point mouseMove, final String cmd) {
        this.id = Objects.requireNonNull(id);
        this.name = new SimpleStringProperty(name);
        this.type = new SimpleObjectProperty<>(type);
        this.keyCode = new SimpleObjectProperty<>(keyCode);
        this.mouseButton = new SimpleObjectProperty<>(mouseButton);
        this.sleepAfterAction = new SimpleLongProperty(sleepAfterAction);
        this.down = new SimpleBooleanProperty(down);
        this.soft = new SimpleBooleanProperty(soft);
        this.mouseMove = new SimpleObjectProperty<>(mouseMove);
        this.cmd = new SimpleStringProperty(cmd);

        listenOn(this.name);
        listenOn(this.type);
        listenOn(this.keyCode);
        listenOn(this.mouseButton);
        listenOn(this.sleepAfterAction);
        listenOn(this.down);
        listenOn(this.soft);
        listenOn(this.mouseMove);
        listenOn(this.cmd);
    }

    public UUID getId() {
        return id;
    }

    public StringProperty getNameProperty() {
        return name;
    }

    public ObjectProperty<ActionType> getTypeProperty() {
        return type;
    }

    public ObjectProperty<KeyCode> getKeyCodeProperty() {
        return keyCode;
    }

    public LongProperty getSleepAfterActionProperty() {
        return sleepAfterAction;
    }

    public BooleanProperty getDownProperty() {
        return down;
    }

    public ObjectProperty<Point> getMouseMoveProperty() {
        return mouseMove;
    }

    public StringProperty getCmdProperty() {
        return cmd;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public ActionType getType() {
        return type.get();
    }

    public void setType(ActionType type) {
        this.type.set(type);
    }

    public KeyCode getKeyCode() {
        return keyCode.get();
    }

    public void setKeyCode(KeyCode keyCode) {
        this.keyCode.set(keyCode);
    }

    public long getSleepAfterAction() {
        return sleepAfterAction.get();
    }

    public void setSleepAfterAction(long sleepAfterAction) {
        this.sleepAfterAction.set(sleepAfterAction);
    }

    public boolean getDown() {
        return down.get();
    }

    public void setDown(boolean down) {
        this.down.set(down);
    }

    public Point getMouseMove() {
        return mouseMove.get();
    }

    public void setMouseMove(Point cmd) {
        this.mouseMove.set(cmd);
    }

    public void setMouseMoveX(int x) {
        this.mouseMove.set(new Point(x, getMouseMove().y));
    }

    public void setMouseMoveY(int y) {
        this.mouseMove.set(new Point(getMouseMove().x, y));
    }

    public String getCmd() {
        return cmd.get();
    }

    public void setCmd(String cmd) {
        this.cmd.set(cmd);
    }

    public ObjectProperty<MouseButton> getMouseButtonProperty() {
        return mouseButton;
    }

    public MouseButton getMouseButton() {
        return mouseButton.get();
    }

    public void setMouseButton(MouseButton mouseButton) {
        this.mouseButton.set(mouseButton);
    }

    public BooleanProperty getSoftProperty() {
        return soft;
    }

    public boolean getSoft() {
        return soft.get();
    }

    public void setSoft(boolean soft) {
        this.soft.set(soft);
    }

    public static JSONObject toJson(ActionModel model) {
        JSONObject object = new JSONObject();
        object.put("id", model.getId());
        object.put("name", model.getName());
        object.put("type", model.getType());
        object.put("keycode", model.getKeyCode());
        object.put("mousebutton", model.getMouseButton());
        object.put("sleepAfterAction", model.getSleepAfterAction());
        object.put("down", model.getDown());
        object.put("soft", model.getSoft());
        object.put("mouseMoveX", model.getMouseMove().x);
        object.put("mouseMoveY", model.getMouseMove().y);
        object.put("cmd", model.getCmd());

        return object;
    }

    public static ActionModel fromJson(JSONObject object) {
        return new ActionModel(
            UUID.fromString(object.getString("id")),
            object.getString("name"),
            ActionType.valueOf(object.getString("type")),
            KeyCode.valueOf(object.getString("keycode")),
            MouseButton.valueOf(object.getString("mousebutton")),
            object.getLong("sleepAfterAction"),
            object.getBoolean("down"),
            object.getBoolean("soft"),
            new Point(object.getInt("mouseMoveX"), object.getInt("mouseMoveY")),
            object.getString("cmd")
        );
    }

}
