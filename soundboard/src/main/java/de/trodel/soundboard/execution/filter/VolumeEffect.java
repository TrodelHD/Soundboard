package de.trodel.soundboard.execution.filter;

import ddf.minim.AudioEffect;

public class VolumeEffect implements AudioEffect {

    private float volume = 1;

    @Override
    public void process(float[] signal) {
        if (volume == 1) {
            return;
        }
        for (int i = 0; i < signal.length; i++) {
            signal[i] = signal[i] * volume;
        }
    }

    @Override
    public void process(float[] sigLeft, float[] sigRight) {
        process(sigLeft);
        process(sigRight);
    }

    public void setVolume(float volume) {
        //this.volume = Math.min(1, Math.max((float) (Math.exp(6.908 * volume) / 1000), 0));
        this.volume = volume * volume;
    }
}
