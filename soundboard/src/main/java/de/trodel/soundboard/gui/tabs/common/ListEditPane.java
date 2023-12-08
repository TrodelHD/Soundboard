package de.trodel.soundboard.gui.tabs.common;

import static javafx.geometry.Pos.CENTER;

import de.trodel.soundboard.utils.Icons;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class ListEditPane extends HBox {
    private static final Insets INSETS_5 = new Insets(5, 5, 5, 5);

    private final Button add    = new Button();
    private final Button edit   = new Button();
    private final Button remove = new Button();

    public ListEditPane() {

        add.setGraphic(new ImageView(Icons.ICON_PLUS));
        edit.setGraphic(new ImageView(Icons.ICON_EDIT));
        remove.setGraphic(new ImageView(Icons.ICON_MINUS));

        setAlignment(CENTER);

        getChildren().addAll(add, edit, remove);
        HBox.setMargin(add, INSETS_5);
        HBox.setMargin(edit, INSETS_5);
        HBox.setMargin(remove, INSETS_5);

    }

    public Button getAdd() {
        return add;
    }

    public Button getEdit() {
        return edit;
    }

    public Button getRemove() {
        return remove;
    }

}
