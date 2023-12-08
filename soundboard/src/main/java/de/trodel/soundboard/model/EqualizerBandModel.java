package de.trodel.soundboard.model;

import org.json.JSONObject;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class EqualizerBandModel extends ObservableModel {

    private final DoubleProperty centerFrequency;
    private final DoubleProperty bandwidth;
    private final DoubleProperty gain;

    public EqualizerBandModel(double centerFrequency, double bandwidth, double gain) {
        this.centerFrequency = new SimpleDoubleProperty(centerFrequency);
        this.bandwidth = new SimpleDoubleProperty(bandwidth);
        this.gain = new SimpleDoubleProperty(gain);

        listenOn(this.centerFrequency);
        listenOn(this.bandwidth);
        listenOn(this.gain);
    }

    public DoubleProperty getCenterFrequencyProperty() {
        return centerFrequency;
    }

    public double getCenterFrequency() {
        return centerFrequency.get();
    }

    public void setCenterFrequency(double centerFrequency) {
        this.centerFrequency.set(centerFrequency);
    }

    public DoubleProperty getBandwidthProperty() {
        return bandwidth;
    }

    public double getBandwidth() {
        return bandwidth.get();
    }

    public void setBandwidth(double bandwidth) {
        this.bandwidth.set(bandwidth);
    }

    public DoubleProperty getGainProperty() {
        return gain;
    }

    public double getGain() {
        return gain.get();
    }

    public void setGain(double gain) {
        this.gain.set(gain);
    }

    public static JSONObject toJson(EqualizerBandModel model) {
        JSONObject object = new JSONObject();
        object.put("centerFrequency", model.getCenterFrequency());
        object.put("bandwidth", model.getBandwidth());
        object.put("gain", model.getGain());
        return object;
    }

    public static EqualizerBandModel fromJson(JSONObject object) {
        return new EqualizerBandModel(
            object.getDouble("centerFrequency"),
            object.getDouble("bandwidth"),
            object.getDouble("gain")
        );
    }

}
