package de.trodel.soundboard.gui.tabs.visualizer;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.util.Arrays.fill;

import de.trodel.soundboard.execution.ExecutionUtils.AudioStreamData;
import de.trodel.soundboard.execution.SoundListenerManager.FullTrackListener;
import de.trodel.soundboard.execution.SoundListenerManager.TrackStartListener;
import de.trodel.soundboard.model.SoundModel;

public class FullTrackBuffer implements FullTrackListener, TrackStartListener {

    private float[] left;
    private float[] right;
    private double  indexPerSample;
    private int     sample;

    public FullTrackBuffer(int bufferSize) {
        left = new float[bufferSize];
        right = new float[bufferSize];
    }

    @Override
    public void trackStart(SoundModel sound, AudioStreamData data) {

        long leng = data.getSampleFrameLength();
        if (leng == -1) {
            leng = (long) ((data.getMillisecondLength() / 1000d) * data.getFrameRate());
        }
        indexPerSample = left.length / (double) leng;
        fill(left, 0f);
        fill(right, 0f);
        sample = 0;
    }

    @Override
    public void fullTrack(float[] left, float[] right) {
        if (left.length != right.length) {
            throw new IllegalArgumentException("Both sides must be the same lenght");
        }

        for (int i = 0; i < left.length; i++) {
            int bI = getBufferIndex();
            if (this.left[bI] < left[i]) {
                this.left[bI] = left[i];
            }

            if (this.right[bI] < right[i]) {
                this.right[bI] = right[i];
            }

            sample++;
        }
    }

    public float[] getLeft() {
        return left;
    }

    public float[] getRight() {
        return right;
    }

    public int getBufferIndex() {
        return min(max((int) (indexPerSample * sample), 0), left.length - 1);
    }
}
