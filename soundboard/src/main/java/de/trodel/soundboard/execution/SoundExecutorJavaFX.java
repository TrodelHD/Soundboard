package de.trodel.soundboard.execution;

import static java.util.stream.Collectors.toList;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.trodel.soundboard.model.EqualizerBandModel;
import de.trodel.soundboard.model.SoundModel;
import javafx.scene.media.EqualizerBand;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundExecutorJavaFX implements AutoCloseable {

    public static List<EqualizerBand> getDeaultBands() {
        return Collections.unmodifiableList(
            List.of(
                new EqualizerBand(32, 19, 0),
                new EqualizerBand(64, 39, 0),
                new EqualizerBand(125, 78, 0),
                new EqualizerBand(250, 156, 0),
                new EqualizerBand(500, 312, 0),
                new EqualizerBand(1000, 625, 0),
                new EqualizerBand(2000, 1250, 0),
                new EqualizerBand(4000, 2500, 0),
                new EqualizerBand(8000, 5000, 0),
                new EqualizerBand(16000, 10000, 0)
            )
        );
    }

    private List<Runnable>    unbindActions;
    private final MediaPlayer player;
    private final Runnable    onClose;

    public SoundExecutorJavaFX(SoundModel sound, Runnable onClose) {
        this.onClose = onClose;
        this.unbindActions = new ArrayList<>();

        Media media = new Media(new File(sound.getPath()).toURI().toString());
        player = new MediaPlayer(media);

        player.volumeProperty().bind(sound.getVolumeProperty());

        player.getAudioEqualizer().getBands().clear();
        player.getAudioEqualizer().getBands().addAll(sound.getBands().stream().map(m -> toBand(m)).collect(toList()));

        player.getAudioEqualizer().getBands().forEach(band -> {
            band.gainProperty().addListener((observable, oldV, newV) -> {
                player.getAudioEqualizer().setEnabled(player.getAudioEqualizer().getBands().stream().anyMatch(b -> b.getGain() != 0));
            });
            unbindActions.add(() -> band.gainProperty());
        });
        player.getAudioEqualizer().setEnabled(player.getAudioEqualizer().getBands().stream().anyMatch(b -> b.getGain() != 0));

        player.setOnEndOfMedia(() -> close());
        player.play();
    }

    @Override
    public void close() {
        unbindActions.forEach(Runnable::run);
        player.dispose();
        onClose.run();
    }

    private EqualizerBand toBand(EqualizerBandModel model) {
        var band = new EqualizerBand(model.getCenterFrequency(), model.getBandwidth(), model.getGain());
        band.centerFrequencyProperty().bind(model.getCenterFrequencyProperty());
        band.bandwidthProperty().bind(model.getBandwidthProperty());
        band.gainProperty().bind(model.getGainProperty());

        unbindActions.add(() -> band.centerFrequencyProperty().unbind());
        unbindActions.add(() -> band.bandwidthProperty().unbind());
        unbindActions.add(() -> band.gainProperty().unbind());

        return band;
    }

}
