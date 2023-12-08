package de.trodel.soundboard.execution;

import static de.trodel.soundboard.execution.ExecutionUtils.audioStreamData;
import static java.nio.file.Files.newInputStream;
import static java.nio.file.StandardOpenOption.READ;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import ddf.minim.AudioListener;
import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import ddf.minim.javasound.JSMinim;
import ddf.minim.spi.AudioRecordingStream;
import de.trodel.soundboard.App;
import de.trodel.soundboard.model.SettingsModel;
import de.trodel.soundboard.model.SoundModel;

public class MinimSoundExecutorProvider {
    private final JSMinim jsMinim;
    private final Minim   minim;

    public MinimSoundExecutorProvider(SettingsModel settings) {
        jsMinim = new JSMinim(this);
        minim = new Minim(jsMinim);
        settings.getOutputDeviceProperty().addListener((observable, oldV, newV) -> {
            settings.getOutputDevice().getMixer().ifPresent(jsMinim::setOutputMixer);
        });

        settings.getOutputDevice().getMixer().ifPresent(jsMinim::setOutputMixer);
    }

    public MinimSoundExecutor getExecutor(SoundModel sound, SoundListenerManager soundListenerManager, Runnable onClose) {
        AudioPlayer player = minim.loadFile(sound.getPath(), 1024);

        var callback = soundListenerManager.trackStarted(sound, audioStreamData(extractARS(player)));

        List<Runnable> close = new ArrayList<>();

        if (callback.live()) {
            player.addListener(new AudioListener() {
                @Override
                public void samples(float[] sampL, float[] sampR) {
                    callback.onLive(sampL, sampR);
                }

                @Override
                public void samples(float[] samp) {
                    samples(samp, samp);
                }
            });
        }

        if (callback.full()) {
            int bufferSize = 1024 * 8 * 8;
            FullTrackReader ftr = new FullTrackReader(minim.loadFileStream(sound.getPath(), bufferSize, false), bufferSize) {
                @Override
                public void full(float[] left, float[] right) {
                    callback.onFull(left, right);
                }
            };
            close.add(() -> ftr.close());
        }

        close.add(onClose);

        return new MinimSoundExecutor(sound, player, () -> close.forEach(Runnable::run));
    }

    private AudioRecordingStream extractARS(AudioPlayer player) {
        try {
            Field field = player.getClass().getDeclaredField("recording");
            field.setAccessible(true);
            return (AudioRecordingStream) field.get(player);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public final String sketchPath(String string) {
        throw new UnsupportedOperationException("Tried sketchPath(" + string + ")");
    }

    public final InputStream createInput(String string) {

        try {
            final String lower = string.toLowerCase();
            if (lower.endsWith(".gz") || lower.endsWith(".svgz")) {
                return new BufferedInputStream(new GZIPInputStream(newInputStream(Path.of(new File(string).toURI()), READ)));
            }
            return new BufferedInputStream(newInputStream(Path.of(new File(string).toURI()), READ));
        } catch (Exception e) {
            App.throwable(e);
            return null;
        }

    }

    public static void main(String[] args) {

    }

}
