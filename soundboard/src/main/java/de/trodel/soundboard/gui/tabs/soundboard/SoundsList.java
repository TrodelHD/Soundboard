package de.trodel.soundboard.gui.tabs.soundboard;

import static javafx.scene.control.SelectionMode.SINGLE;

import de.trodel.soundboard.gui.LayoutUtils;
import de.trodel.soundboard.gui.tabs.common.ListEditPane;
import de.trodel.soundboard.model.SoundModel;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;

public class SoundsList extends BorderPane {

    private final ListView<SoundModel> soundList    = new ListView<>();
    private final ListEditPane         listEditPane = new ListEditPane();

    public SoundsList() {
        setCenter(LayoutUtils.anchorFit(soundList));
        setBottom(listEditPane);

        soundList.getSelectionModel().setSelectionMode(SINGLE);
        soundList.setCellFactory(param -> new ListCell<>() {
            protected void updateItem(SoundModel item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNameProperty().get());
                }
            };
        });
    }

    public ListView<SoundModel> getSoundList() {
        return soundList;
    }

    public ListEditPane getListEditPane() {
        return listEditPane;
    }
}
