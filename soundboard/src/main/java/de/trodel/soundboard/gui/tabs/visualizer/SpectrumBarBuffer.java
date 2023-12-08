package de.trodel.soundboard.gui.tabs.visualizer;

import static java.lang.System.currentTimeMillis;

import java.util.Arrays;
import java.util.stream.IntStream;

public class SpectrumBarBuffer {
    private final float[][] bars;
    private final short[]   barFrequencies;
    private final long[]    lastUpadte;
    private final float     maxDropPerMilliSecond;

    public SpectrumBarBuffer(int channels, float maxDropPerSecond, short[] barFrequencies) {
        bars = new float[channels][barFrequencies.length];
        this.barFrequencies = Arrays.copyOf(barFrequencies, barFrequencies.length);
        lastUpadte = new long[channels];
        this.maxDropPerMilliSecond = maxDropPerSecond / 1000f;
    }

    public void update(float[] spectrum, float bandWidth, int channel) {
        float[] bar = this.bars[channel];
        long currentTime = currentTimeMillis();

        float maxDrop = (currentTime - lastUpadte[channel]) * maxDropPerMilliSecond;
        lastUpadte[channel] = currentTime;

        int lastSpectrumIndex = 0;
        for (int i = 0; i < bar.length; i++) {
            short barFrequency = barFrequencies[i];
            int spectrumIndex = Math.min((int) (barFrequency / bandWidth), spectrum.length);
            float value = getMaxValue(spectrum, lastSpectrumIndex, spectrumIndex);
            lastSpectrumIndex = spectrumIndex;

            float targ = bar[i];

            if (targ <= value) {
                bar[i] = value;
            } else {
                if (targ - value <= maxDrop) {
                    bar[i] = value;
                } else {
                    bar[i] = targ - maxDrop;
                }
            }
        }
    }

    private float getMaxValue(float[] arr, int from, int to) {
        float max = arr[from];
        for (int i = from + 1; i < to; i++) {
            if (max < arr[i]) {
                max = arr[i];
            }
        }
        return max;
    }

    public float[] getBands(int channel) {
        return bars[channel];
    }

    public void clearAll() {
        IntStream.range(0, bars.length).forEach(this::clear);
    }

    public void clear(int channel) {
        Arrays.fill(bars[channel], 0);
        lastUpadte[channel] = 0;
    }

}
