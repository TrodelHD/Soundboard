package de.trodel.soundboard.hotkey;

import java.util.function.Consumer;
import java.util.function.Supplier;

import de.trodel.soundboard.hotkey.BlockingKeyListenerRecord.KeyAction;
import de.trodel.soundboard.model.ActionModel;

public interface HotkeyProvider {

    public interface RecordActions {
        public void keyAction(KeyAction action, long firstAction);
    }

    public void combination(Consumer<int[]> onFinish);
    public Supplier<ActionModel[]> record(RecordActions onRecordActions);

}
