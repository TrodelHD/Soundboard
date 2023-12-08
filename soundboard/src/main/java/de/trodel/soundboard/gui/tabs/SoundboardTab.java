package de.trodel.soundboard.gui.tabs;

import de.trodel.soundboard.gui.AbstractMainTab;
import de.trodel.soundboard.gui.LayoutUtils;
import de.trodel.soundboard.gui.tabs.soundboard.SoundDetails;
import de.trodel.soundboard.gui.tabs.soundboard.SoundsList;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;

public class SoundboardTab extends AbstractMainTab {

    private final SplitPane    splitPane    = new SplitPane();
    private final SoundsList   soundsList   = new SoundsList();
    private final SoundDetails soundDetails = new SoundDetails();

    public SoundboardTab() {
        super("Soundboard");

        splitPane.setOrientation(Orientation.HORIZONTAL);
        splitPane.getItems().addAll(soundsList, soundDetails);

        splitPane.setDividerPositions(0.25d);

        setContent(getTabContent());

        LayoutUtils.keepHorizontalDividerAtPixel(splitPane, 200);
    }

    protected Node getTabContent() {
        return splitPane;
    }

    public SoundsList getSoundsList() {
        return soundsList;
    }

    public SoundDetails getSoundDetails() {
        return soundDetails;
    }
}
