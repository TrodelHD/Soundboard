package de.trodel.soundboard;

import static de.trodel.soundboard.model.ServerModel.ServerMode.disabled;
import static de.trodel.soundboard.model.SettingsModel.availableOutputDevices;
import static javafx.collections.FXCollections.observableArrayList;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.jnativehook.NativeHookException;
import org.json.JSONObject;

import de.trodel.soundboard.controller.MainTabPaneController;
import de.trodel.soundboard.execution.SoundboardExecutionProvider;
import de.trodel.soundboard.execution.SoundboardExecutor;
import de.trodel.soundboard.gui.MainTabPane;
import de.trodel.soundboard.hotkey.BlockingKeyListenerCombination;
import de.trodel.soundboard.hotkey.BlockingKeyListenerRecord;
import de.trodel.soundboard.hotkey.HotKeyManager;
import de.trodel.soundboard.hotkey.HotkeyProvider;
import de.trodel.soundboard.model.ActionModel;
import de.trodel.soundboard.model.AutoclickModel;
import de.trodel.soundboard.model.HotkeyModel;
import de.trodel.soundboard.model.MainModel;
import de.trodel.soundboard.model.ServerModel;
import de.trodel.soundboard.model.SettingsModel;
import de.trodel.soundboard.model.SoundModel;
import de.trodel.soundboard.server.ServerManager;
import de.trodel.soundboard.utils.Icons;
import de.trodel.soundboard.utils.SaveUtils;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    private MainModel                   mainModel;
    private MainTabPane                 mainTabPane;
    private MainTabPaneController       mainTabPaneController;
    private HotKeyManager               hotKeyManager;
    private SoundboardExecutionProvider soundboardExecutor;
    private ServerManager               serverManager;

    @Override
    public void start(Stage stage) throws IOException, NativeHookException {
        mainModel = SaveUtils.load()
            .map(JSONObject::new)
            .map(MainModel::fromJson)
            .orElseGet(
                () -> new MainModel(
                    observableArrayList(),
                    observableArrayList(),
                    new ServerModel(9450, "", disabled),
                    new SettingsModel(availableOutputDevices()[0], new HotkeyModel(new int[0]))
                )
            );

        soundboardExecutor = new SoundboardExecutor(mainModel.getSettings());
        serverManager = new ServerManager(mainModel, soundboardExecutor);
        mainTabPane = new MainTabPane();
        mainTabPaneController = new MainTabPaneController(mainTabPane, mainModel, getHotkeyProvider(), getSoundboardExecutor(), serverManager);

        scene = new Scene(mainTabPane, 640, 880);

        stage.getIcons().add(Icons.ICON_ICON_SOUND_FORGE);
        stage.setScene(scene);
        stage.show();

        SaveManager saveManager = new SaveManager(() -> {
            try {
                SaveUtils.save(MainModel.toJson(mainModel).toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        saveManager.saveOnChange(mainModel);

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                try {
                    serverManager.close();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                try {
                    SaveUtils.save(MainModel.toJson(mainModel).toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                saveManager.close();
                Platform.exit();
                System.exit(0);
            }
        });

        hotKeyManager = new HotKeyManager(mainModel) {
            @Override
            public void onSoundHit(SoundModel model) {
                getSoundboardExecutor().executeSound(model);
            }

            @Override
            public void onAutoClickHit(AutoclickModel model) {
                getSoundboardExecutor().executeAutoclick(model);
            }

            @Override
            public void onSoundStop() {
                getSoundboardExecutor().stopSound();
            }
        };

    }

    private HotkeyProvider getHotkeyProvider() {
        return new HotkeyProvider() {

            @Override
            public Supplier<ActionModel[]> record(RecordActions onRecordActions) {
                var record = new BlockingKeyListenerRecord(hotKeyManager.getDetector()) {
                    @Override
                    public void keyAction(KeyAction action, long firstAction) {
                        onRecordActions.keyAction(action, firstAction);
                    }
                };

                return () -> {
                    record.close();
                    return record.toActions();
                };
            }

            @Override
            public void combination(Consumer<int[]> onFinish) {
                new BlockingKeyListenerCombination(hotKeyManager.getDetector(), onFinish);
            }
        };
    }

    public static void throwable(final Throwable throwable) {
        Platform.runLater(() -> {
            throwable.printStackTrace();

        });
    }

    public SoundboardExecutionProvider getSoundboardExecutor() {
        return soundboardExecutor;
    }

    public static void main(String[] args) {
        launch();
    }

}