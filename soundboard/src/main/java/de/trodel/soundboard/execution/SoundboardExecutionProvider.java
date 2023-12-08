package de.trodel.soundboard.execution;

import java.util.UUID;

import de.trodel.soundboard.model.AutoclickModel;
import de.trodel.soundboard.model.SoundModel;

public interface SoundboardExecutionProvider {

    public void executeSound(SoundModel sound);
    public void executeAutoclick(AutoclickModel autoclick);

    public void stopSound();
    public void stopAutoclick(UUID id);

    public SoundListenerManager getSoundListenerManager();
}
