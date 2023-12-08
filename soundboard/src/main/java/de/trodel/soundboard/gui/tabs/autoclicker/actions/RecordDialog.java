package de.trodel.soundboard.gui.tabs.autoclicker.actions;

import static javafx.scene.control.SelectionMode.SINGLE;
import static javafx.scene.input.KeyCode.ESCAPE;

import de.trodel.soundboard.hotkey.BlockingKeyListenerRecord.KeyAction;
import de.trodel.soundboard.hotkey.HotkeyProvider.RecordActions;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyEvent;

public class RecordDialog extends Dialog<ButtonType> {

    public static final ButtonType BUTTON_OK     = ButtonType.OK;
    public static final ButtonType BUTTON_CANCEL = ButtonType.CANCEL;

    private final RecordActions       recordActions;
    private final ListView<KeyAction> actions = new ListView<>();

    public RecordDialog() {

        getDialogPane().getButtonTypes().addAll(BUTTON_OK, BUTTON_CANCEL);

        getDialogPane().setContent(actions);
        actions.setPrefSize(640, 420);

        actions.getSelectionModel().setSelectionMode(SINGLE);
        actions.setCellFactory(param -> new ListCell<>() {
            protected void updateItem(KeyAction item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append(item.isDown() ? "Down\t" : "Up\t\t");
                    sb.append(item.getKeyCode().getName());
                    sb.append("\t".repeat(Math.max(0, 3 - item.getKeyCode().getName().length() / 6)) + " ");
                    sb.append((item.getSystemTime() - actions.getItems().get(0).getSystemTime()) + "ms");
                    setText(sb.toString());
                }
            };
        });

        recordActions = (action, firstAction) -> {
            Platform.runLater(() -> {
                actions.getItems().add(action);
                actions.scrollTo(actions.getItems().size() - 1);
            });
        };

        //Prevent closing on ESC
        getDialogPane().getScene().addEventHandler(KeyEvent.ANY, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == ESCAPE) {
                    event.consume();
                }
            }
        });

        setOnShown(event -> {
            Platform.runLater(() -> {
                actions.requestFocus();
            });
        });
    }

    public RecordActions getRecordActions() {
        return recordActions;
    }
}
