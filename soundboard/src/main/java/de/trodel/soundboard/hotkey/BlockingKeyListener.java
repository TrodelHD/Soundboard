package de.trodel.soundboard.hotkey;

import java.util.Objects;

import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

public abstract class BlockingKeyListener implements NativeKeyListener {
    private final HotKeyDetector detector;

    public BlockingKeyListener(final HotKeyDetector detector) {
        this.detector = detector;
        inject();
    }

    private void inject() {
        if (Objects.equals(detector.blockingKeyListener, this)) {
            return;
        }
        if (detector.blockingKeyListener != null) {
            throw new IllegalStateException("Can not override an existing BlockingKeyListener!");
        }
        detector.blockingKeyListener = this;
    }

    public void release() {
        if (Objects.equals(detector.blockingKeyListener, this)) {
            detector.blockingKeyListener = null;
        }
    }
}
