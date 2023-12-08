package de.trodel.soundboard.execution;

import static java.lang.Thread.sleep;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang3.StringUtils;

import de.trodel.soundboard.model.ActionModel;
import de.trodel.soundboard.model.AutoclickModel;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.robot.Robot;

public class AutoclickExecutor implements AutoCloseable, Runnable {
    private static final AtomicLong THREAD_CONTER = new AtomicLong(1);

    private final AutoclickModel autoclick;
    private final Robot          robot;
    private final Thread         thread;
    private final Runnable       onClose;

    private boolean[] mouseButtons = new boolean[MouseButton.values().length];
    private boolean[] keyCodes     = new boolean[KeyCode.values().length];

    public AutoclickExecutor(AutoclickModel autoclick, Runnable onClose) {
        this.autoclick = autoclick;
        this.onClose = onClose;
        robot = new Robot();
        thread = new Thread(this, "autoclicker-thread-" + THREAD_CONTER.getAndIncrement());
        thread.start();
    }

    @Override
    public void run() {
        try {
            while (true) {
                for (var action : autoclick.getActions()) {
                    executeAction(action);
                }
            }
        } catch (InterruptedException e) {
            doEnd();
        } catch (Exception e) {
            doEnd();
            e.printStackTrace();
        } finally {
            close();
        }
    }

    private void executeAction(ActionModel action) throws InterruptedException {

        switch (action.getType()) {
            case key:
                executeActionKey(action);
                break;
            case mouse:
                executeActionMouse(action);
                break;
            case mouseMove:
                executeActionMouseMove(action);
                break;
            case cmd:
                executeActionCmd(action);
                break;
            case stop:
                throw new InterruptedException();
            default:
                throw new IllegalArgumentException("Unexpected value: " + action.getType());
        }

        if (action.getSleepAfterAction() > 0) {
            sleep(action.getSleepAfterAction());
        }
    }

    private void executeActionCmd(ActionModel action) throws InterruptedException {
        if (StringUtils.isBlank(action.getCmd())) {
            return;
        }
        Process p = null;
        try {
            p = Runtime.getRuntime().exec("cmd /c " + action.getCmd());
            p.waitFor();
        } catch (InterruptedException e) {
            p.destroy();
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void executeActionMouseMove(ActionModel action) {
        Platform.runLater(() -> {
            double x, y;

            if (action.getSoft()) {
                x = action.getMouseMove().getX() + robot.getMouseX();
                y = action.getMouseMove().getY() + robot.getMouseY();
            } else {
                x = action.getMouseMove().getX();
                y = action.getMouseMove().getY();
            }

            robot.mouseMove(x, y);
        });
    }

    private void executeActionMouse(ActionModel action) {
        if (action.getDown()) {
            Platform.runLater(() -> robot.mouseClick(action.getMouseButton()));
            mouseButtons[action.getMouseButton().ordinal()] = true;
        } else {
            Platform.runLater(() -> robot.mouseRelease(action.getMouseButton()));
            mouseButtons[action.getMouseButton().ordinal()] = false;
        }
    }

    private void executeActionKey(ActionModel action) {
        if (action.getDown()) {
            Platform.runLater(() -> robot.keyPress(action.getKeyCode()));
            keyCodes[action.getKeyCode().ordinal()] = true;
        } else {
            Platform.runLater(() -> robot.keyRelease(action.getKeyCode()));
            keyCodes[action.getKeyCode().ordinal()] = false;
        }
    }

    private void doEnd() {
        if (autoclick.getAutoRelease()) {

            Platform.runLater(() -> {
                for (int i = 0; i < mouseButtons.length; i++) {
                    if (mouseButtons[i]) {
                        robot.mouseRelease(MouseButton.values()[i]);
                    }
                }

                for (int i = 0; i < keyCodes.length; i++) {
                    if (keyCodes[i]) {
                        robot.keyRelease(KeyCode.values()[i]);
                    }
                }
            });
        }
    }

    @Override
    public void close() {
        if (thread.isAlive()) {
            thread.interrupt();
        }
        onClose.run();
    }

}
