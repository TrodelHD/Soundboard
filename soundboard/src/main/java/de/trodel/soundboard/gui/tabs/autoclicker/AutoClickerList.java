package de.trodel.soundboard.gui.tabs.autoclicker;

import static javafx.scene.control.SelectionMode.SINGLE;

import de.trodel.soundboard.gui.LayoutUtils;
import de.trodel.soundboard.gui.tabs.common.ListEditPane;
import de.trodel.soundboard.model.AutoclickModel;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;

public class AutoClickerList extends BorderPane {

    private final ListView<AutoclickModel> autoClickList = new ListView<>();
    private final ListEditPane             listEditPane  = new ListEditPane();

    public AutoClickerList() {
        setCenter(LayoutUtils.anchorFit(autoClickList));
        setBottom(listEditPane);

        autoClickList.getSelectionModel().setSelectionMode(SINGLE);
        autoClickList.setCellFactory(param -> new ListCell<>() {
            protected void updateItem(AutoclickModel item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNameProperty().get());
                }
            };
        });
    }

    public ListView<AutoclickModel> getAutoClickList() {
        return autoClickList;
    }

    public ListEditPane getListEditPane() {
        return listEditPane;
    }
}
