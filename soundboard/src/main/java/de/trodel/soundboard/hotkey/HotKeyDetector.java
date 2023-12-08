package de.trodel.soundboard.hotkey;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public abstract class HotKeyDetector implements NativeKeyListener, AutoCloseable {

    private final boolean[] keys;
    BlockingKeyListener     blockingKeyListener;

    public abstract void keyReleased(boolean[] keys);

    public HotKeyDetector() throws NativeHookException {

        keys = new boolean[65536];

        for (int i = 0; i < keys.length; i++) {
            keys[i] = false;
        }

        // Get the logger for "com.github.kwhat.jnativehook" and set the level to warning.
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.WARNING);

        // Don't forget to disable the parent handlers.
        logger.setUseParentHandlers(false);

        GlobalScreen.registerNativeHook();
        GlobalScreen.addNativeKeyListener(this);
    }

    @Override
    public void nativeKeyPressed(final NativeKeyEvent event) {
        keys[event.getKeyCode()] = true;

        if (blockingKeyListener != null) {
            blockingKeyListener.nativeKeyPressed(event);
            return;
        }
    }

    @Override
    public void nativeKeyReleased(final NativeKeyEvent event) {
        if (!keys[event.getKeyCode()]) {
            return;
        }

        if (blockingKeyListener != null) {
            keys[event.getKeyCode()] = false;
            blockingKeyListener.nativeKeyReleased(event);
            return;
        }

        keyReleased(keys);
        keys[event.getKeyCode()] = false;
    }

    @Override
    public void nativeKeyTyped(final NativeKeyEvent event) {
        if (blockingKeyListener != null) {
            blockingKeyListener.nativeKeyTyped(event);
            return;
        }
    }

    public void releaseBlockingKeyListener() {
        blockingKeyListener = null;
    }

    @Override
    public void close() throws NativeHookException {
        GlobalScreen.unregisterNativeHook();
    }
}
