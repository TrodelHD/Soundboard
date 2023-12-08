package de.trodel.soundboard.gui.tabs;

import de.trodel.soundboard.gui.AbstractMainTab;
import de.trodel.soundboard.gui.LayoutUtils;
import de.trodel.soundboard.gui.tabs.visualizer.BarSpectrumPane;
import de.trodel.soundboard.gui.tabs.visualizer.FullTrackPane;
import javafx.scene.Node;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class VisualizerTab extends AbstractMainTab {

    private final FullTrackPane   fullTrackPane;
    private final BarSpectrumPane barSpectrumPane;

    public VisualizerTab() {
        super("Visualizer");

        fullTrackPane = new FullTrackPane();
        barSpectrumPane = new BarSpectrumPane();

        setContent(getTabContent());
    }

    protected Node getTabContent() {

        ///BorderPane pane = new BorderPane();
        GridPane pane = new GridPane();
        pane.add(LayoutUtils.anchorFit(fullTrackPane), 0, 0);
        pane.add(LayoutUtils.anchorFit(barSpectrumPane), 0, 1);

        var r1 = new RowConstraints();
        var r2 = new RowConstraints();
        r1.setPercentHeight(50d);
        r2.setPercentHeight(50d);
        pane.getRowConstraints().addAll(r1, r2);

        var c1 = new ColumnConstraints();
        c1.setPercentWidth(100d);
        pane.getColumnConstraints().addAll(c1);

        return LayoutUtils.anchorFit(pane);
    }

    public FullTrackPane getFullTrackPane() {
        return fullTrackPane;
    }

    public BarSpectrumPane getBarSpectrumPane() {
        return barSpectrumPane;
    }
}
