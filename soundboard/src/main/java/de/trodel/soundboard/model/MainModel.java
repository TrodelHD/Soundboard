package de.trodel.soundboard.model;

import static java.util.stream.Collectors.toList;
import static javafx.collections.FXCollections.observableArrayList;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.collections.ObservableList;

public class MainModel extends ObservableModel {
    private final ObservableList<SoundModel>     sounds;
    private final ObservableList<AutoclickModel> autoclicker;
    private final ServerModel                    server;
    private final SettingsModel                  settings;

    public MainModel(final ObservableList<SoundModel> sounds, final ObservableList<AutoclickModel> autoclicker, final ServerModel server,
            SettingsModel settings) {
        this.sounds = listenOnModel(sounds);
        this.autoclicker = listenOnModel(autoclicker);
        this.server = (ServerModel) listenOnModel(server);
        this.settings = (SettingsModel) listenOnModel(settings);
    }

    public ObservableList<SoundModel> getSounds() {
        return sounds;
    }

    public ObservableList<AutoclickModel> getAutoclicker() {
        return autoclicker;
    }

    public ServerModel getServer() {
        return server;
    }

    public SettingsModel getSettings() {
        return settings;
    }

    public static JSONObject toJson(MainModel model) {
        JSONObject object = new JSONObject();
        object.put("sounds", new JSONArray(model.sounds.stream().map(SoundModel::toJson).collect(toList())));
        object.put("autoclicker", new JSONArray(model.autoclicker.stream().map(AutoclickModel::toJson).collect(toList())));
        object.put("server", ServerModel.toJson(model.server));
        object.put("settings", SettingsModel.toJson(model.settings));

        return object;
    }

    public static MainModel fromJson(final JSONObject object) {
        return new MainModel(
            observableArrayList(streamArray(object.getJSONArray("sounds")).map(SoundModel::fromJson).collect(toList())),
            observableArrayList(streamArray(object.getJSONArray("autoclicker")).map(AutoclickModel::fromJson).collect(toList())),
            ServerModel.fromJson(object.getJSONObject("server")),
            SettingsModel.fromJson(object.getJSONObject("settings"))
        );
    }

    public static Stream<JSONObject> streamArray(JSONArray arr) {
        return IntStream.range(0, arr.length()).mapToObj(i -> arr.getJSONObject(i));
    }
}
