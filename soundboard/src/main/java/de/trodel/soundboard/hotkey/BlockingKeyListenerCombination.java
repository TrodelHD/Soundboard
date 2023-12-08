package de.trodel.soundboard.hotkey;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.jnativehook.keyboard.NativeKeyEvent;

public class BlockingKeyListenerCombination extends BlockingKeyListener {

    private final List<Integer>   keys;
    private final Consumer<int[]> result;

    public BlockingKeyListenerCombination(final HotKeyDetector detector, final Consumer<int[]> result) {
        super(detector);
        this.keys = new ArrayList<>();
        this.result = result;
    }

    @Override
    public void nativeKeyPressed(final NativeKeyEvent event) {
        if (keys.contains(event.getKeyCode())) {
            return;
        }
        keys.add(event.getKeyCode());
    }

    @Override
    public void nativeKeyReleased(final NativeKeyEvent event) {
        if (event.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
            result.accept(new int[0]);
        } else {
            result.accept(keys.stream().mapToInt(i -> i).toArray());
        }
        release();
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeEvent) {

    }

}
