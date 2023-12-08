package de.trodel.soundboard;

import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;

import de.trodel.soundboard.model.ObservableModel;

public class SaveManager implements AutoCloseable {

    private static final Duration MIN_DELAY = Duration.ofSeconds(2);

    private Timer          timer;
    private final Runnable runSave;

    private boolean timerIsRunning;
    private long    request;

    public SaveManager(Runnable runSave) {
        this.runSave = runSave;
        timerIsRunning = false;
        request = 0;
    }

    public void saveOnChange(ObservableModel model) {
        model.addModelListener(() -> saveRequest());
    }

    private void saveRequest() {
        request++;
        if (timerIsRunning) {
            return;
        }
        timerIsRunning = true;

        getTimer().schedule(new TimerTask() {
            private final long thisRequest = request;

            @Override
            public void run() {
                timerIsRunning = false;

                if (thisRequest == request) {
                    runSave.run();
                } else {
                    saveRequest();
                }
            }
        }, MIN_DELAY.toMillis());
    }

    private Timer getTimer() {
        if (timer == null) {
            timer = new Timer();
        }
        return timer;
    }

    @Override
    public void close() {
        if (timer != null) {
            timer.cancel();
        }
    }

}
