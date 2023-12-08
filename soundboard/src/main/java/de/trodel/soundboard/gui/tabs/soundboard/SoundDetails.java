package de.trodel.soundboard.gui.tabs.soundboard;

import static de.trodel.soundboard.utils.Icons.ICON_PLAY;
import static de.trodel.soundboard.utils.Icons.ICON_STOP;
import static javafx.geometry.Pos.CENTER;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SoundDetails extends VBox {

    private static final Insets INSETS_8 = new Insets(8, 8, 8, 8);

    private final HBox      header = new HBox();
    private final TextArea  info   = new TextArea();
    private final Button    play   = new Button();
    private final Button    stop   = new Button();
    private final Slider    volume = new Slider(0, 100, 100);
    private final SoundGain gain   = new SoundGain();

    public SoundDetails() {
        header.setSpacing(10);

        play.setGraphic(new ImageView(ICON_PLAY));
        stop.setGraphic(new ImageView(ICON_STOP));

        info.setEditable(false);

        info.setPrefSize(250, 50);
        play.setPrefSize(30, 30);
        stop.setPrefSize(30, 30);
        header.getChildren().addAll(info, play, stop);
        header.setAlignment(Pos.CENTER_LEFT);

        var box1 = withHeader("Volume", volume);
        var box2 = withHeader("Gain", gain);

        setupSlider(volume, 100, 10, 10);

        getChildren().addAll(header, box1);
        VBox.setMargin(header, INSETS_8);
        VBox.setMargin(box1, INSETS_8);
        VBox.setMargin(box2, INSETS_8);

    }

    private void setupSlider(Slider slider, double defaultValue, double majorTick, int minorTickCount) {
        slider.addEventHandler(MouseEvent.MOUSE_CLICKED, (event) -> {
            if (event.getButton() == MouseButton.MIDDLE && event.getClickCount() == 2) {
                slider.setValue(defaultValue);
            }
        });
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(majorTick);
        slider.setMinorTickCount(minorTickCount);
        slider.setShowTickLabels(true);
    }

    private VBox withHeader(String header, Node node) {
        VBox vBox = new VBox(5);
        Label label = new Label(header);
        vBox.setAlignment(CENTER);
        vBox.getChildren().addAll(label, node);
        return vBox;
    }

    public TextArea getInfo() {
        return info;
    }

    public Button getPlay() {
        return play;
    }

    public Button getStop() {
        return stop;
    }

    public Slider getVolume() {
        return volume;
    }

    public SoundGain getGain() {
        return gain;
    }

}
