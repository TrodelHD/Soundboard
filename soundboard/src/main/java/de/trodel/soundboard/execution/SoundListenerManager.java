package de.trodel.soundboard.execution;

import java.util.ArrayList;
import java.util.List;

import de.trodel.soundboard.execution.ExecutionUtils.AudioStreamData;
import de.trodel.soundboard.model.SoundModel;

public class SoundListenerManager {

    public interface TrackStartListener {
        public void trackStart(SoundModel sound, AudioStreamData data);
    }

    public interface TrackEndListener {
        public void trackEnd();
    }

    public interface FFTListener {
        public void fft(float[] spectrumLeft, float[] spectrumRight, float bandwidth);
    }

    public interface LiveListener {
        public void live(float[] left, float[] right);
    }

    public interface FullTrackListener {
        public void fullTrack(float[] left, float[] right);
    }

    public interface TrackPlayCallback {
        public boolean live();
        public boolean full();

        public void onLive(float[] left, float[] right);
        public void onFull(float[] left, float[] right);
    }

    private final List<TrackStartListener> trackStarts;
    private final List<TrackEndListener>   trackEnds;

    private final List<FFTListener>       ffts;
    private final List<LiveListener>      lives;
    private final List<FullTrackListener> fullTracks;

    public SoundListenerManager() {
        trackStarts = new ArrayList<>();
        trackEnds = new ArrayList<>();
        ffts = new ArrayList<>();
        lives = new ArrayList<>();
        fullTracks = new ArrayList<>();
    }

    void trackEnded() {
        trackEnds.forEach(TrackEndListener::trackEnd);
    }

    TrackPlayCallback trackStarted(SoundModel soundModel, AudioStreamData data) {
        trackStarts.forEach(l -> l.trackStart(soundModel, data));

        final boolean fft = ffts.size() > 0;
        final boolean live = lives.size() > 0 || fft;
        final boolean full = fullTracks.size() > 0;

        return new TrackPlayCallback() {
            private final FFTransform fftCalc = fft ? new FFTransform(1024, data.getSampleRate()) : null;

            @Override
            public void onLive(float[] left, float[] right) {
                if (!live) {
                    throw new UnsupportedOperationException();
                }

                lives.forEach(l -> l.live(left, right));

                if (fft) {
                    fftCalc.forward(left, right);
                    ffts.forEach(l -> l.fft(fftCalc.getSpectrumLeft(), fftCalc.getSpectrumRight(), fftCalc.getBandWidth()));
                }
            }

            @Override
            public void onFull(float[] left, float[] right) {
                if (!full) {
                    throw new UnsupportedOperationException();
                }
                fullTracks.forEach(l -> l.fullTrack(left, right));
            }

            @Override
            public boolean live() {
                return live;
            }

            @Override
            public boolean full() {
                return full;
            }
        };
    }

    public void addTrackStartListener(TrackStartListener l) {
        if (!trackStarts.contains(l)) {
            trackStarts.add(l);
        }
    }

    public void addTrackEndListener(TrackEndListener l) {
        if (!trackEnds.contains(l)) {
            trackEnds.add(l);
        }
    }

    public void addFullTrackListener(FullTrackListener l) {
        if (!fullTracks.contains(l)) {
            fullTracks.add(l);
        }
    }

    public void addLiveTrackListener(LiveListener l) {
        if (!lives.contains(l)) {
            lives.add(l);
        }
    }

    public void addFFTTrackListener(FFTListener l) {
        if (!ffts.contains(l)) {
            ffts.add(l);
        }
    }

}
