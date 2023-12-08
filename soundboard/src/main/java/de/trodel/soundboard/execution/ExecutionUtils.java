package de.trodel.soundboard.execution;

import ddf.minim.AudioMetaData;
import ddf.minim.spi.AudioRecordingStream;

public class ExecutionUtils {

    public interface AudioStreamData {

        public float getSampleRate();

        public int getSampleSizeInBits();

        public int getChannels();

        public int getFrameSize();

        public float getFrameRate();

        public boolean isBigEndian();

        public long getSampleFrameLength();

        public int getMillisecondPosition();

        public int getMillisecondLength();

        public AudioMetaData getMetaData();

    }

    public static AudioStreamData audioStreamData(AudioRecordingStream ars) {
        return new AudioStreamData() {
            @Override
            public float getSampleRate() {
                return ars.getFormat().getSampleRate();
            }

            @Override
            public int getSampleSizeInBits() {
                return ars.getFormat().getSampleSizeInBits();
            }

            @Override
            public int getChannels() {
                return ars.getFormat().getChannels();
            }

            @Override
            public int getFrameSize() {
                return ars.getFormat().getFrameSize();
            }

            @Override
            public float getFrameRate() {
                return ars.getFormat().getFrameRate();
            }

            @Override
            public boolean isBigEndian() {
                return ars.getFormat().isBigEndian();
            }

            @Override
            public long getSampleFrameLength() {
                return ars.getSampleFrameLength();
            }

            @Override
            public int getMillisecondPosition() {
                return ars.getMillisecondPosition();
            }

            @Override
            public int getMillisecondLength() {
                return ars.getMillisecondLength();
            }

            @Override
            public AudioMetaData getMetaData() {
                return ars.getMetaData();
            }

        };
    }

}
