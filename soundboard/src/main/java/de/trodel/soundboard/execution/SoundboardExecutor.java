package de.trodel.soundboard.execution;

import static java.util.Optional.ofNullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import de.trodel.soundboard.model.AutoclickModel;
import de.trodel.soundboard.model.SettingsModel;
import de.trodel.soundboard.model.SoundModel;

public class SoundboardExecutor implements SoundboardExecutionProvider {

    private final MinimSoundExecutorProvider   minimSoundExecutorProvider;
    private final SoundListenerManager         soundListenerManager;
    private Optional<MinimSoundExecutor>       soundExecutor      = Optional.empty();
    private final Map<UUID, AutoclickExecutor> autoclickExecutors = new HashMap<>();

    public SoundboardExecutor(SettingsModel settingsModel) {
        this.soundListenerManager = new SoundListenerManager();
        this.minimSoundExecutorProvider = new MinimSoundExecutorProvider(settingsModel);
    }

    @Override
    public void executeSound(SoundModel sound) {
        soundExecutor.ifPresent(MinimSoundExecutor::close);
        soundExecutor = Optional.of(minimSoundExecutorProvider.getExecutor(sound, soundListenerManager, () -> {
            soundExecutor = Optional.empty();
            soundListenerManager.trackEnded();
        }));
    }

    @Override
    public void executeAutoclick(AutoclickModel autoclick) {
        ofNullable(autoclickExecutors.get(autoclick.getId()))
            .ifPresentOrElse(
                AutoclickExecutor::close,
                () -> autoclickExecutors.put(autoclick.getId(), new AutoclickExecutor(autoclick, () -> autoclickExecutors.remove(autoclick.getId())))
            );
    }

    @Override
    public void stopSound() {
        soundExecutor.ifPresent(MinimSoundExecutor::close);
    }

    @Override
    public void stopAutoclick(UUID id) {
        ofNullable(autoclickExecutors.get(id)).ifPresent(AutoclickExecutor::close);
    }

    @Override
    public SoundListenerManager getSoundListenerManager() {
        return soundListenerManager;
    }

}
