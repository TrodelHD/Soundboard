package de.trodel.soundboard.hotkey;

import static java.time.Duration.between;
import static java.time.Instant.now;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.jnativehook.NativeHookException;

import de.trodel.soundboard.model.AutoclickModel;
import de.trodel.soundboard.model.MainModel;
import de.trodel.soundboard.model.SoundModel;

public abstract class HotKeyManager {

    public abstract void onSoundStop();

    public abstract void onSoundHit(SoundModel model);

    public abstract void onAutoClickHit(AutoclickModel model);

    private final HotKeyDetector     detector;
    private final Map<UUID, Instant> recentHit;

    public HotKeyManager(final MainModel mainModel) throws NativeHookException {
        this.recentHit = new HashMap<>();
        this.detector = new HotKeyDetector() {
            @Override
            public void keyReleased(final boolean[] keys) {
                mainModel.getSounds().forEach(sound -> {
                    if (match(keys, sound.getHotkey()) && isNotRecent(sound.getId())) {
                        onSoundHit(sound);
                    }
                });

                mainModel.getAutoclicker().forEach(autoclick -> {
                    if (match(keys, autoclick.getHotkey()) && isNotRecent(autoclick.getId())) {
                        onAutoClickHit(autoclick);
                    }
                });

                if (match(keys, mainModel.getSettings().getSoundStopHotkey())) {
                    onSoundStop();
                }
            }
        };
    }

    private boolean isNotRecent(final UUID id) {

        final Instant last = recentHit.get(id);

        if (last == null) {
            recentHit.put(id, now());
            return true;
        }

        if (between(last, now()).toMillis() <= 300L) {
            return false;
        }

        recentHit.put(id, now());
        return true;
    }

    private boolean match(final boolean[] keys, final int[] hotkeys) {
        if (hotkeys.length == 0) {
            return false;
        }

        for (final int i : hotkeys) {
            if (!keys[i]) {
                return false;
            }
        }

        return true;
    }

    public HotKeyDetector getDetector() {
        return detector;
    }

}
