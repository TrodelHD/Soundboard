package de.trodel.soundboard.gui.tabs.autoclicker;

import static de.trodel.soundboard.utils.Icons.ICON_PLAY;
import static de.trodel.soundboard.utils.Icons.ICON_STOP;
import static javafx.geometry.Pos.CENTER;
import static javafx.scene.layout.Priority.ALWAYS;

import de.trodel.soundboard.gui.tabs.autoclicker.actions.ActionsPane;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class AutoClickerDetails extends VBox {

    private static final Insets INSETS_8 = new Insets(8, 8, 8, 8);

    private final HBox        header       = new HBox();
    private final TextArea    info         = new TextArea();
    private final Button      play         = new Button();
    private final Button      stop         = new Button();
    private final CheckBox    autorealease = new CheckBox();
    private final ActionsPane actionsPane  = new ActionsPane();

    public AutoClickerDetails() {
        header.setSpacing(10);

        VBox subHeader1 = new VBox(10);
        HBox subHeader2 = new HBox(10);

        play.setGraphic(new ImageView(ICON_PLAY));
        stop.setGraphic(new ImageView(ICON_STOP));

        subHeader2.setAlignment(CENTER);
        subHeader2.getChildren().addAll(play, stop);
        subHeader1.setAlignment(CENTER);
        subHeader1.getChildren().addAll(subHeader2, autorealease);

        autorealease.setText("Auto-Release");

        info.setEditable(false);

        info.setPrefSize(250, 50);
        play.setPrefSize(30, 30);
        stop.setPrefSize(30, 30);
        header.getChildren().addAll(info, subHeader1);
        header.setAlignment(Pos.CENTER_LEFT);

        getChildren().addAll(header, actionsPane);
        VBox.setMargin(header, INSETS_8);
        VBox.setVgrow(actionsPane, ALWAYS);
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

    public CheckBox getAutorealease() {
        return autorealease;
    }

    public ActionsPane getActionsPane() {
        return actionsPane;
    }

}
