package de.trodel.soundboard.controller.tabs.visualizer;

import de.trodel.soundboard.execution.SoundListenerManager;
import de.trodel.soundboard.gui.tabs.visualizer.BarSpectrumPane;

public class BarSpectrumPaneController {

    public BarSpectrumPaneController(BarSpectrumPane barSpectrumPane, SoundListenerManager soundListenerManager) {
        soundListenerManager.addTrackStartListener(barSpectrumPane);
        soundListenerManager.addTrackEndListener(barSpectrumPane);
        soundListenerManager.addFFTTrackListener(barSpectrumPane);

    }

}
