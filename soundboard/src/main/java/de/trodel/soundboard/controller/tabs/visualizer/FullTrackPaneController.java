package de.trodel.soundboard.controller.tabs.visualizer;

import de.trodel.soundboard.execution.SoundListenerManager;
import de.trodel.soundboard.gui.tabs.visualizer.FullTrackPane;

public class FullTrackPaneController {

    public FullTrackPaneController(FullTrackPane fullTrackPane, SoundListenerManager soundListenerManager) {
        soundListenerManager.addFullTrackListener(fullTrackPane);
        soundListenerManager.addTrackStartListener(fullTrackPane);
        soundListenerManager.addTrackEndListener(fullTrackPane);
        soundListenerManager.addLiveTrackListener(fullTrackPane);

    }

}
