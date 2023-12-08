package de.trodel.soundboard.model;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SoundModel extends ObservableModel {

    private final UUID                               id;
    private final StringProperty                     name;
    private final StringProperty                     path;
    private final FloatProperty                      volume;
    private final ObservableList<EqualizerBandModel> bands;
    private final ObjectProperty<HotkeyModel>        hotkey;

    public SoundModel(final UUID id, final String name, final String path, final float volume,
            List<EqualizerBandModel> bands,
            final HotkeyModel hotkey) {
        this.id = Objects.requireNonNull(id);
        this.name = new SimpleStringProperty(name);
        this.path = new SimpleStringProperty(path);
        this.volume = new SimpleFloatProperty(volume);
        this.bands = FXCollections.observableList(bands);
        this.hotkey = new SimpleObjectProperty<>(hotkey);

        listenOn(this.name);
        listenOn(this.path);
        listenOn(this.volume);
        listenOnModel(this.bands);
        listenOn(this.hotkey);

    }

    public UUID getId() {
        return id;
    }

    public StringProperty getNameProperty() {
        return name;
    }

    public StringProperty getPathProperty() {
        return path;
    }

    public FloatProperty getVolumeProperty() {
        return volume;
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

    public String getPath() {
        return path.get();
    }

    public void setPath(String path) {
        this.path.set(path);
    }

    public float getVolume() {
        return volume.get();
    }

    public void setVolume(float volume) {
        this.volume.set(volume);
    }

    public int[] getHotkey() {
        return hotkey.get().getHotkey();
    }

    public ObservableList<EqualizerBandModel> getBands() {
        return bands;
    }

    public void setHotkey(int[] hotkey) {
        this.hotkey.set(new HotkeyModel(hotkey));
    }

    public static JSONObject toJson(SoundModel model) {
        JSONObject object = new JSONObject();
        object.put("id", model.getId());
        object.put("name", model.getName());
        object.put("path", model.getPath());
        object.put("volume", model.getVolume());
        object.put("gains", new JSONArray(model.getBands().stream().map(EqualizerBandModel::toJson).collect(toList())));
        object.put("hotkey", HotkeyModel.toJson(model.hotkey.get()));
        return object;
    }

    public static SoundModel fromJson(JSONObject object) {

        return new SoundModel(
            UUID.fromString(object.getString("id")),
            object.getString("name"),
            object.getString("path"),
            object.getFloat("volume"),
            fromJsonArray(object.getJSONArray("gains")),
            HotkeyModel.fromJson(object.getJSONArray("hotkey"))
        );
    }

    private static List<EqualizerBandModel> fromJsonArray(JSONArray arr) {
        List<EqualizerBandModel> result = new ArrayList<>();
        for (int i = 0; i < arr.length(); i++) {
            result.add(EqualizerBandModel.fromJson(arr.getJSONObject(i)));
        }
        return result;
    }

}
