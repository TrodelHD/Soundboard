package de.trodel.soundboard.gui.tabs;

import static de.trodel.soundboard.model.SettingsModel.availableOutputDevices;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static javafx.collections.FXCollections.observableArrayList;

import de.trodel.soundboard.gui.AbstractMainTab;
import de.trodel.soundboard.gui.NameWrapper;
import de.trodel.soundboard.model.SettingsModel.MixerInfo;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class SettingsTab extends AbstractMainTab {
    private static final Insets                    INSETS_10       = new Insets(10, 10, 10, 10);
    private final ComboBox<NameWrapper<MixerInfo>> outputDevice    = new ComboBox<>();
    private final Button                           soundStopHotKey = new Button();

    public SettingsTab() {
        super("Settings");

        VBox settings = new VBox(0);

        outputDevice.setItems(
            observableArrayList(
                stream(availableOutputDevices())
                    .map(info -> new NameWrapper<>(info, MixerInfo::name))
                    .collect(toList())
            )
        );

        soundStopHotKey.setMinWidth(200);

        settings.getChildren().add(withHeader("Output Device:", outputDevice));
        settings.getChildren().add(withHeader("Sound stop hotkey:", soundStopHotKey));

        setContent(settings);
    }

    private VBox withHeader(String header, Node node) {
        VBox vBox = new VBox(5);
        Label label = new Label(header);
        vBox.setAlignment(Pos.TOP_LEFT);
        vBox.getChildren().addAll(label, node);

        VBox.setMargin(vBox, INSETS_10);

        return vBox;
    }

    public ComboBox<NameWrapper<MixerInfo>> getOutputDevice() {
        return outputDevice;
    }

    public Button getSoundStopHotKey() {
        return soundStopHotKey;
    }

}
