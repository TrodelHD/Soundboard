package de.trodel.soundboard.execution;

import ddf.minim.AudioListener;
import ddf.minim.AudioPlayer;
import de.trodel.soundboard.execution.filter.VolumeEffect;
import de.trodel.soundboard.model.SoundModel;

public class MinimSoundExecutor implements AutoCloseable, AudioListener {

    private final Runnable    onClose;
    private final AudioPlayer player;

    @SuppressWarnings("deprecation")
    public MinimSoundExecutor(SoundModel sound, AudioPlayer player, Runnable onClose) {
        this.onClose = onClose;
        this.player = player;

        player.addListener(this);
        player.play();

        var volume = new VolumeEffect();

        volume.setVolume(sound.getVolume());
        sound.getVolumeProperty().addListener((observable, oldV, newV) -> {
            volume.setVolume(newV.floatValue());
        });

        player.addEffect(volume);

    }

    @Override
    public void close() {
        player.close();
        onClose.run();
    }

    @Override
    public void samples(float[] samp) {
        checkClose();
    }

    @Override
    public void samples(float[] sampL, float[] sampR) {
        checkClose();

    }

    private void checkClose() {
        if (!player.isPlaying()) {
            close();
        }
    }

}
