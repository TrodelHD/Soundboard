package de.trodel.soundboard.controller.tabs;

import de.trodel.soundboard.controller.tabs.visualizer.BarSpectrumPaneController;
import de.trodel.soundboard.controller.tabs.visualizer.FullTrackPaneController;
import de.trodel.soundboard.execution.SoundListenerManager;
import de.trodel.soundboard.gui.tabs.VisualizerTab;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class VisualizerTabController {
    public VisualizerTabController(VisualizerTab visualizerTab, SoundListenerManager soundListenerManager) {

        TabPane tabPane = visualizerTab.getTabPane();

        tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<>() {
            private boolean hasChanges = false;

            @Override
            public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
                if (newValue == visualizerTab && !hasChanges) {
                    hasChanges = true;
                    register(visualizerTab, soundListenerManager);
                }
            }
        });

    }

    private void register(VisualizerTab visualizerTab, SoundListenerManager soundListenerManager) {
        new FullTrackPaneController(visualizerTab.getFullTrackPane(), soundListenerManager);
        new BarSpectrumPaneController(visualizerTab.getBarSpectrumPane(), soundListenerManager);
    }
}
