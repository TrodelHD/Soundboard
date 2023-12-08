package de.trodel.soundboard.model;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Mixer.Info;

import org.json.JSONObject;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class SettingsModel extends ObservableModel {

    public record MixerInfo(String name, String vendor, String description, String version) {
        public Optional<Mixer> getMixer() {
            return Arrays.stream(AudioSystem.getMixerInfo())
                .filter(i -> Objects.equals(i.getName(), name()))
                .filter(i -> Objects.equals(i.getVendor(), vendor()))
                .filter(i -> Objects.equals(i.getDescription(), description()))
                .filter(i -> Objects.equals(i.getVersion(), version()))
                .findFirst()
                .map(AudioSystem::getMixer);
        }

        private static MixerInfo of(Info info) {
            return new MixerInfo(info.getName(), info.getVendor(), info.getDescription(), info.getVersion());
        }
    };

    private final ObjectProperty<MixerInfo>   outputDevice;
    private final ObjectProperty<HotkeyModel> soundStopHotkey;

    public SettingsModel(MixerInfo outputDevice, HotkeyModel soundStopHotkey) {

        this.outputDevice = new SimpleObjectProperty<>(outputDevice);
        this.soundStopHotkey = new SimpleObjectProperty<>(soundStopHotkey);

        listenOn(this.outputDevice);
        listenOn(this.soundStopHotkey);
    }

    public ObjectProperty<MixerInfo> getOutputDeviceProperty() {
        return outputDevice;
    }

    public void setOutputDevice(MixerInfo outputDevice) {
        this.outputDevice.set(outputDevice);
    }

    public MixerInfo getOutputDevice() {
        return outputDevice.get();
    }

    public ObjectProperty<HotkeyModel> getSoundStopHotkeyProperty() {
        return soundStopHotkey;
    }

    public void setSoundStopHotkey(int[] soundStopHotkey) {
        this.soundStopHotkey.set(new HotkeyModel(soundStopHotkey));
    }

    public int[] getSoundStopHotkey() {
        return soundStopHotkey.get().getHotkey();
    }

    public static JSONObject toJson(SettingsModel model) {
        JSONObject object = new JSONObject();
        object.put("soundStopHotkey", HotkeyModel.toJson(model.getSoundStopHotkeyProperty().get()));
        object.put("outputDevice", infoToJson(model.getOutputDevice()));
        return object;
    }

    public static SettingsModel fromJson(final JSONObject object) {
        return new SettingsModel(
            infoFromJson(object.getJSONObject("outputDevice")),
            HotkeyModel.fromJson(object.getJSONArray("soundStopHotkey"))
        );
    }

    private static MixerInfo infoFromJson(JSONObject object) {
        return new MixerInfo(object.getString("name"), object.getString("vendor"), object.getString("description"), object.getString("version"));
    }

    private static JSONObject infoToJson(MixerInfo info) {
        JSONObject object = new JSONObject();
        object.put("name", info.name());
        object.put("vendor", info.vendor());
        object.put("description", info.description());
        object.put("version", info.version());
        return object;
    }

    public static MixerInfo[] availableOutputDevices() {

        return Arrays.stream(AudioSystem.getMixerInfo())
            .map(MixerInfo::of)
            .toArray(MixerInfo[]::new);
    }

}
