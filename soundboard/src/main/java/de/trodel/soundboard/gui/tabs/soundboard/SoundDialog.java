package de.trodel.soundboard.gui.tabs.soundboard;

import static de.trodel.soundboard.gui.LayoutUtils.anchorLeftRight;
import static de.trodel.soundboard.utils.Icons.ICON_ICON_SOUND_FORGE;
import static javafx.scene.input.KeyCode.ESCAPE;

import java.io.File;
import java.util.Optional;

import de.trodel.soundboard.gui.tabs.common.ValidMultiObserver;
import de.trodel.soundboard.gui.tabs.common.ValidObjectObserver;
import de.trodel.soundboard.hotkey.HotkeyProvider;
import de.trodel.soundboard.model.HotkeyModel;
import de.trodel.soundboard.model.SoundModel;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class SoundDialog extends Dialog<ButtonType> {

    public static final ButtonType BUTTON_OK     = ButtonType.OK;
    public static final ButtonType BUTTON_CANCEL = ButtonType.CANCEL;

    private final HotkeyProvider hotkeyProvider;

    private final StringProperty              name;
    private final StringProperty              path;
    private final ObjectProperty<HotkeyModel> hotkey;

    public SoundDialog(SoundModel model, HotkeyProvider hotkeyProvider) {
        super();

        this.hotkeyProvider = hotkeyProvider;

        name = new SimpleStringProperty(model.getName());
        path = new SimpleStringProperty(model.getPath());
        hotkey = new SimpleObjectProperty<>(new HotkeyModel(model.getHotkey()));

        createPane();

        setTitle("Sound edit");

        setResultConverter(dialogButton -> {

            if (dialogButton == BUTTON_OK) {
                model.setName(name.get());
                model.setPath(path.get());
                model.setHotkey(hotkey.get().getHotkey());
            }

            return dialogButton;
        });

        Stage stage = (Stage) getDialogPane().getScene().getWindow();
        stage.getIcons().add(ICON_ICON_SOUND_FORGE);

        //Prevent closing on ESC
        getDialogPane().getScene().addEventHandler(KeyEvent.ANY, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == ESCAPE) {
                    event.consume();
                }
            }
        });

    }

    private void createPane() {
        final DialogPane dialogPane = getDialogPane();
        dialogPane.getButtonTypes().addAll(BUTTON_OK, BUTTON_CANCEL);

        VBox vBox = new VBox(15);

        Button chooseFile = new Button("Choose file");
        Button hotkey = new Button("Click to sethotkey");
        TextField name = new TextField(this.name.getValue());

        /*
         * Valid
         */

        var nameValid = new ValidObjectObserver<>(this.name, s -> s.length() > 0);
        var pathValid = new ValidObjectObserver<>(this.path, s -> s.length() > 0 && new File(s).exists());
        var hotkeyValid = new ValidObjectObserver<>(this.hotkey, __ -> true);

        nameValid.addListener((observer, oldV, newV) -> setColorValid(name, newV));
        pathValid.addListener((observer, oldV, newV) -> setColorValid(chooseFile, newV));
        hotkeyValid.addListener((observer, oldV, newV) -> setColorValid(hotkey, newV));
        setColorValid(name, nameValid.get());
        setColorValid(chooseFile, pathValid.get());
        setColorValid(hotkey, hotkeyValid.get());

        var valid = new ValidMultiObserver(nameValid, pathValid, hotkeyValid);
        valid.addListener((observer, oldV, newV) -> setOkDisable(!newV));
        setOkDisable(!valid.get());

        /*
         * Other
         */

        chooseFile.setOnAction(event -> {
            openFileChooser();
        });
        chooseFile.setPrefHeight(45);

        hotkey.setOnAction(event -> {
            hotkey.setText("Waiting");
            hotkey.setStyle("-fx-text-fill: yellow;");

            hotkeyProvider.combination(value -> {
                Platform.runLater(() -> {
                    this.hotkey.set(new HotkeyModel(value));
                    setColorValid(hotkey, hotkeyValid.get());
                    hotkey.setText("Click to sethotkey");
                });
            });
        });
        hotkey.setPrefHeight(45);

        this.name.bind(name.textProperty());
        name.setPromptText("enter name");
        name.setPrefHeight(30);

        vBox.getChildren().addAll(anchorLeftRight(chooseFile), anchorLeftRight(hotkey), anchorLeftRight(name));
        dialogPane.setContent(vBox);

    }

    private void openFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose sound file");
        fileChooser.getExtensionFilters().addAll(
            new ExtensionFilter("Audio", "*.wav", "*.mp3")
        );
        path.set(Optional.ofNullable(fileChooser.showOpenDialog(getOwner())).map(File::getAbsolutePath).orElse(""));
    }

    private void setColorValid(Node node, boolean valid) {
        if (valid) {
            node.setStyle("-fx-text-fill: green;");
        } else {
            node.setStyle("-fx-text-fill: red;");
        }
    }

    private void setOkDisable(boolean value) {
        getDialogPane().lookupButton(BUTTON_OK).setDisable(value);
    }

}
