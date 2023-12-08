package de.trodel.soundboard.model;

import org.json.JSONObject;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ServerModel extends ObservableModel {

    public enum ServerMode {
        disabled,
        enabled
    }

    private final IntegerProperty            port;
    private final StringProperty             key;
    private final ObjectProperty<ServerMode> mode;

    public ServerModel(final int port, final String key, final ServerMode mode) {
        this.port = new SimpleIntegerProperty(port);
        this.key = new SimpleStringProperty(key);
        this.mode = new SimpleObjectProperty<>(mode);

        listenOn(this.port);
        listenOn(this.key);
        listenOn(this.mode);
    }

    public IntegerProperty getPortProperty() {
        return port;
    }

    public StringProperty getKeyProperty() {
        return key;
    }

    public ObjectProperty<ServerMode> getModeProperty() {
        return mode;
    }

    public int getPort() {
        return port.get();
    }

    public void setPort(int port) {
        this.port.set(port);
    }

    public String getKey() {
        return key.get();
    }

    public void setKey(String key) {
        this.key.set(key);
    }

    public ServerMode getMode() {
        return mode.get();
    }

    public void setMode(ServerMode mode) {
        this.mode.set(mode);
    }

    public static JSONObject toJson(ServerModel model) {
        JSONObject object = new JSONObject();
        object.put("port", model.getPort());
        object.put("key", model.getKey());
        object.put("mode", model.getMode());

        return object;
    }

    public static ServerModel fromJson(JSONObject object) {

        return new ServerModel(
            object.getInt("port"),
            object.getString("key"),
            ServerMode.valueOf(object.getString("mode"))
        );
    }

}
