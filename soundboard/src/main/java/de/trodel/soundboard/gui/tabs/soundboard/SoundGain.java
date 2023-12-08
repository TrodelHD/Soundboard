package de.trodel.soundboard.gui.tabs.soundboard;

import static javafx.geometry.Orientation.VERTICAL;
import static javafx.geometry.Pos.CENTER;

import java.text.DecimalFormat;

import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.EqualizerBand;

public class SoundGain extends HBox {

    public SoundGain() {
        setAlignment(CENTER);
    }

    public void clear() {
        getChildren().clear();
    }

    public Slider createGainSlider(double centerFrequency, double bandwidth) {
        Slider slider = generateGainSlider();
        slider.setPrefHeight(160);

        getChildren().add(labelBox(slider, centerFrequency, bandwidth));

        return slider;
    }

    private static final DecimalFormat df = new DecimalFormat("0");

    private Node labelBox(Slider slider, double centerFrequency, double bandwidth) {
        VBox box = new VBox(5);
        Label center = new Label("Freq:\n" + df.format(centerFrequency));
        center.setContentDisplay(ContentDisplay.CENTER);
        Label band = new Label("Band:\n" + df.format(bandwidth));
        band.setContentDisplay(ContentDisplay.CENTER);
        box.getChildren().addAll(slider, center, band);

        return box;
    }

    private Slider generateGainSlider() {
        Slider slider = new Slider(EqualizerBand.MIN_GAIN, EqualizerBand.MAX_GAIN, 0);
        slider.setOrientation(VERTICAL);
        slider.setMajorTickUnit(6);
        slider.setMinorTickCount(5);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);

        slider.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            if (event.getButton() == MouseButton.MIDDLE && event.getClickCount() == 2) {
                slider.setValue(0);
            }
        });

        return slider;
    }

}
