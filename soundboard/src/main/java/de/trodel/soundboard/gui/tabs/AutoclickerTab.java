package de.trodel.soundboard.gui.tabs;

import de.trodel.soundboard.gui.AbstractMainTab;
import de.trodel.soundboard.gui.LayoutUtils;
import de.trodel.soundboard.gui.tabs.autoclicker.AutoClickerDetails;
import de.trodel.soundboard.gui.tabs.autoclicker.AutoClickerList;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;

public class AutoclickerTab extends AbstractMainTab {

    private final SplitPane          splitPane          = new SplitPane();
    private final AutoClickerList    autoClickerList    = new AutoClickerList();
    private final AutoClickerDetails autoClickerDetails = new AutoClickerDetails();

    public AutoclickerTab() {
        super("Autoclicker");

        splitPane.setOrientation(Orientation.HORIZONTAL);
        splitPane.getItems().addAll(autoClickerList, autoClickerDetails);

        splitPane.setDividerPositions(0.25d);

        setContent(getTabContent());

        LayoutUtils.keepHorizontalDividerAtPixel(splitPane, 200);

    }

    protected Node getTabContent() {
        return splitPane;
    }

    public AutoClickerList getAutoClickerList() {
        return autoClickerList;
    }

    public AutoClickerDetails getAutoClickerDetails() {
        return autoClickerDetails;
    }

}
