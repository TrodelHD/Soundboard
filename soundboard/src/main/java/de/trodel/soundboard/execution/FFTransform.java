package de.trodel.soundboard.execution;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.pow;

import ddf.minim.analysis.FFT;

public class FFTransform {

    private final FFT     fft;
    private final float[] spectrumLeft;
    private final float[] spectrumRight;

    public FFTransform(int timeSize, float sampleRate) {
        this.fft = new FFT(timeSize, sampleRate);
        this.spectrumLeft = new float[fft.specSize()];
        this.spectrumRight = new float[fft.specSize()];
    }

    public void forward(float[] left, float[] right) {
        writeSpectrum(spectrumLeft, left);
        writeSpectrum(spectrumRight, right);
    }

    private static final float POW = 1 / 1.7f;

    private void writeSpectrum(float[] spectrum, float[] samples) {
        fft.forward(samples);
        for (int i = 0; i < spectrum.length; i++) {
            float bandwidht = fft.getBand(i) / (350);//426 real value 350 looks better
            spectrum[i] = min(1, max(0, (float) pow(bandwidht, POW)));
        }
    }

    public float getBandWidth() {
        return fft.getBandWidth();
    }

    public float[] getSpectrumLeft() {
        return spectrumLeft;
    }

    public float[] getSpectrumRight() {
        return spectrumRight;
    }
}
