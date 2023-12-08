package de.trodel.soundboard.gui.tabs.autoclicker.actions;

import static de.trodel.soundboard.utils.Icons.ICON_ARROW_DOWN;
import static de.trodel.soundboard.utils.Icons.ICON_ARROW_UP;
import static de.trodel.soundboard.utils.Icons.ICON_MINUS;
import static de.trodel.soundboard.utils.Icons.ICON_PLUS;
import static de.trodel.soundboard.utils.Icons.ICON_REC;
import static javafx.geometry.Pos.CENTER;
import static javafx.scene.control.SelectionMode.SINGLE;

import de.trodel.soundboard.model.ActionModel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ActionsList extends VBox {
    private static final Insets INSETS_5 = new Insets(5, 5, 5, 5);

    private final ListView<ActionModel> actionList = new ListView<>();
    private final Button                add        = new Button();
    private final Button                remove     = new Button();
    private final Button                up         = new Button();
    private final Button                down       = new Button();
    private final Button                record     = new Button();

    public ActionsList() {
        setAlignment(Pos.TOP_CENTER);

        setupButton(add, ICON_PLUS);
        setupButton(remove, ICON_MINUS);
        setupButton(up, ICON_ARROW_UP);
        setupButton(down, ICON_ARROW_DOWN);
        setupButton(record, ICON_REC);

        HBox buttons = new HBox(add, remove, up, down, record);
        buttons.setAlignment(CENTER);

        Label label = new Label("Actions");

        actionList.getSelectionModel().setSelectionMode(SINGLE);
        actionList.setCellFactory(param -> new ListCell<>() {
            protected void updateItem(ActionModel item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNameProperty().get());
                }
            };
        });

        getChildren().addAll(label, actionList, buttons);
        setVgrow(actionList, Priority.ALWAYS);
        setVgrow(label, Priority.NEVER);
        setVgrow(buttons, Priority.NEVER);

    }

    private static void setupButton(Button button, Image icon) {
        button.setGraphic(new ImageView(icon));
        HBox.setMargin(button, INSETS_5);
    }

    public ListView<ActionModel> getActionList() {
        return actionList;
    }

    public Button getAdd() {
        return add;
    }

    public Button getRemove() {
        return remove;
    }

    public Button getUp() {
        return up;
    }

    public Button getDown() {
        return down;
    }

    public Button getRecord() {
        return record;
    }

}
