package de.trodel.soundboard.execution;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ddf.minim.MultiChannelBuffer;
import ddf.minim.spi.AudioRecordingStream;

public abstract class FullTrackReader implements AutoCloseable {

    private final ExecutorService      reader    = Executors.newSingleThreadExecutor();;
    private final AudioRecordingStream ars;
    private boolean                    arsClosed = false;
    private boolean                    stoped    = false;

    public abstract void full(float[] left, float[] right);

    public FullTrackReader(AudioRecordingStream ars, int bufferSize) {
        if (!ars.isPlaying()) {
            ars.play();
        }
        this.ars = ars;
        reader.submit(() -> {
            var mcb = new MultiChannelBuffer(bufferSize, ars.getFormat().getChannels());

            int lIndex = 0;
            int rIndex = mcb.getChannelCount() > 1 ? 1 : 0;

            int read;
            while ((read = ars.read(mcb)) > 0) {
                if (stoped) {
                    break;
                }

                if (read != mcb.getBufferSize()) {
                    float[] left = new float[read];
                    float[] right = new float[read];

                    System.arraycopy(mcb.getChannel(lIndex), 0, left, 0, left.length);
                    System.arraycopy(mcb.getChannel(rIndex), 0, right, 0, right.length);

                    full(left, right);
                } else {
                    full(mcb.getChannel(lIndex), mcb.getChannel(rIndex));
                }
            }
            ars.close();
            arsClosed = true;
            reader.shutdown();
            return null;
        });

    }

    @Override
    public void close() {
        stoped = true;
        if (!reader.isTerminated()) {
            reader.shutdownNow();
        }
        if (!arsClosed) {
            ars.close();
        }
    }

}
