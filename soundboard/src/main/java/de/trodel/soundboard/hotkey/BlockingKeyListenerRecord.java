package de.trodel.soundboard.hotkey;

import static java.util.Arrays.stream;
import static javafx.scene.input.KeyCode.UNDEFINED;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.SwingKeyAdapter;

import de.trodel.soundboard.model.ActionModel;
import de.trodel.soundboard.model.ActionModel.ActionType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

public abstract class BlockingKeyListenerRecord extends BlockingKeyListener implements AutoCloseable {

    public static class KeyAction {
        private final KeyCode keyCode;
        private final long    systemTime;
        private final boolean down;

        public KeyAction(final KeyCode keyCode, final long systemTime, final boolean down) {
            this.keyCode = keyCode;
            this.systemTime = systemTime;
            this.down = down;
        }

        public KeyCode getKeyCode() {
            return keyCode;
        }

        public long getSystemTime() {
            return systemTime;
        }

        public boolean isDown() {
            return down;
        }
    }

    public abstract void keyAction(KeyAction action, long firstAction);

    private List<KeyAction>       actions = new ArrayList<>();
    private final SwingKeyAdapter swingAdapter;

    public BlockingKeyListenerRecord(final HotKeyDetector detector) {
        super(detector);
        swingAdapter = new SwingKeyAdapter() {
            private static final long serialVersionUID = 859960214771659744L;

            @Override
            public void keyPressed(final KeyEvent event) {
                final KeyAction action = new KeyAction(getKeyCode(event.getKeyCode()), System.currentTimeMillis(), true);
                actions.add(action);
                keyAction(action, actions.get(0).getSystemTime());
            }

            @Override
            public void keyReleased(final KeyEvent event) {
                final KeyAction action = new KeyAction(getKeyCode(event.getKeyCode()), System.currentTimeMillis(), false);
                actions.add(action);
                keyAction(action, actions.get(0).getSystemTime());
            }

            private KeyCode getKeyCode(int keyCode) {
                return stream(KeyCode.values())
                    .filter(e -> e.getCode() == keyCode)
                    .findFirst()
                    .orElse(UNDEFINED);
            }
        };
    }

    public ActionModel[] toActions() {
        final ActionModel[] arr = new ActionModel[actions.size()];
        for (int i = 0; i < arr.length; i++) {
            final KeyAction action = actions.get(i);

            arr[i] = new ActionModel(
                UUID.randomUUID(),
                action.getKeyCode().getName() + (action.isDown() ? "_Down" : "_Up"),
                ActionType.key,
                action.getKeyCode(),
                MouseButton.PRIMARY,
                i >= actions.size() - 1 ? 1000 : actions.get(i + 1).getSystemTime() - action.getSystemTime(),
                action.isDown(),
                true,
                new Point(0, 0),
                ""
            );
        }

        return arr;
    }

    @Override
    public void nativeKeyPressed(final NativeKeyEvent event) {
        swingAdapter.nativeKeyPressed(event);
    }

    @Override
    public void nativeKeyReleased(final NativeKeyEvent event) {
        swingAdapter.nativeKeyReleased(event);

    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeEvent) {

    }

    @Override
    public void close() {
        release();
    }

}
