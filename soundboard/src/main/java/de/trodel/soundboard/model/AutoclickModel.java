package de.trodel.soundboard.model;

import static de.trodel.soundboard.model.MainModel.streamArray;
import static java.util.stream.Collectors.toList;
import static javafx.collections.FXCollections.observableArrayList;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

public class AutoclickModel extends ObservableModel {
    private final UUID                        id;
    private final StringProperty              name;
    private final ObservableList<ActionModel> actions;
    private final BooleanProperty             autoRelease;
    private final ObjectProperty<HotkeyModel> hotkey;

    public AutoclickModel(final UUID id, final String name, final List<ActionModel> initialActions,
            final boolean autoRelease, final HotkeyModel hotkey) {
        this.id = Objects.requireNonNull(id);
        this.name = new SimpleStringProperty(name);
        this.actions = observableArrayList(initialActions);
        this.autoRelease = new SimpleBooleanProperty(autoRelease);
        this.hotkey = new SimpleObjectProperty<>(hotkey);

        listenOn(this.name);
        listenOnModel(this.actions);
        listenOn(this.autoRelease);
        listenOn(this.hotkey);

    }

    public UUID getId() {
        return id;
    }

    public StringProperty getNameProperty() {
        return name;
    }

    public ObservableList<ActionModel> getActions() {
        return actions;
    }

    public BooleanProperty getAutoReleaseProperty() {
        return autoRelease;
    }

    public ObjectProperty<HotkeyModel> getHotkeyProperty() {
        return hotkey;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public boolean getAutoRelease() {
        return autoRelease.get();
    }

    public void setAutoRelease(boolean autoRelease) {
        this.autoRelease.set(autoRelease);
    }

    public int[] getHotkey() {
        return hotkey.get().getHotkey();
    }

    public void setHotkey(int[] hotkey) {
        this.hotkey.setValue(new HotkeyModel(hotkey));
    }

    public static JSONObject toJson(AutoclickModel model) {
        JSONObject object = new JSONObject();
        object.put("id", model.getId());
        object.put("name", model.getName());
        object.put("actions", new JSONArray(model.getActions().stream().map(ActionModel::toJson).collect(toList())));
        object.put("autoRelease", model.getAutoRelease());
        object.put("hotkey", HotkeyModel.toJson(model.hotkey.get()));
        return object;
    }

    public static AutoclickModel fromJson(JSONObject object) {

        return new AutoclickModel(
            UUID.fromString(object.getString("id")),
            object.getString("name"),
            observableArrayList(streamArray(object.getJSONArray("actions")).map(ActionModel::fromJson).collect(toList())),
            object.getBoolean("autoRelease"),
            HotkeyModel.fromJson(object.getJSONArray("hotkey"))
        );
    }

}
