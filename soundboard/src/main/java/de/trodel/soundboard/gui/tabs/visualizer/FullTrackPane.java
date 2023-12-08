package de.trodel.soundboard.gui.tabs.visualizer;

import static de.trodel.soundboard.gui.LayoutUtils.anchorFit;
import static javafx.scene.paint.CycleMethod.NO_CYCLE;

import de.trodel.soundboard.execution.ExecutionUtils.AudioStreamData;
import de.trodel.soundboard.execution.SoundListenerManager.FullTrackListener;
import de.trodel.soundboard.execution.SoundListenerManager.LiveListener;
import de.trodel.soundboard.execution.SoundListenerManager.TrackEndListener;
import de.trodel.soundboard.execution.SoundListenerManager.TrackStartListener;
import de.trodel.soundboard.model.SoundModel;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

public class FullTrackPane extends StackPane implements FullTrackListener, TrackStartListener, TrackEndListener, LiveListener {

    private class AnchorCanvas extends Canvas {
        @Override
        public boolean isResizable() {
            return true;
        }

        @Override
        public void resize(double width, double height) {
            setWidth(width);
            setHeight(height);
        }
    }

    private final FullTrackBuffer buffer;
    private final Canvas          background, forground;
    private AudioStreamData       audioData;

    public FullTrackPane() {
        background = new AnchorCanvas();
        forground = new AnchorCanvas();
        buffer = new FullTrackBuffer(2048);

        getChildren().addAll(anchorFit(background), anchorFit(forground));
    }

    private void drawBackground() {
        GraphicsContext gc = background.getGraphicsContext2D();
        double width = background.getWidth();
        double height = background.getHeight();

        float[] leftRaw = buffer.getLeft();
        float[] rightRaw = buffer.getLeft();

        double halfHight = height / 2;

        gc.clearRect(0, 0, width, height);
        gc.setFill(
            new LinearGradient(
                0, 0, 0, height, false, NO_CYCLE,
                new Stop(0, Color.RED),
                new Stop(0.3, Color.GREEN),
                new Stop(0.5, Color.GREEN.brighter()),
                new Stop(0.7, Color.GREEN),
                new Stop(1, Color.RED)
            )
        );
        //Full poly
        int points = leftRaw.length + rightRaw.length;
        double[] px = new double[points];
        double[] py = new double[points];

        for (int i = 0; i < leftRaw.length; i++) {
            px[i] = width * (i / (double) leftRaw.length);
            py[i] = halfHight * (1 - leftRaw[i]);
        }

        for (int i = rightRaw.length - 1, j = leftRaw.length; i >= 0; i--, j++) {
            px[j] = width * (i / (double) rightRaw.length);
            //py[j] = halfHight;
            py[j] = halfHight * rightRaw[i] + halfHight;
        }

        gc.fillPolygon(px, py, points);

    }

    private void drawForground() {
        GraphicsContext gc = forground.getGraphicsContext2D();
        double width = forground.getWidth();
        double height = forground.getHeight();

        gc.setFill(Color.BLACK);
        gc.clearRect(0, 0, width, height);

        if (audioData == null) {
            return;
        }

        final double relativ = (double) audioData.getMillisecondPosition() / (double) audioData.getMillisecondLength();
        gc.fillRect(width * relativ, 0, 1, height);
    }

    @Override
    public void live(float[] left, float[] right) {
        Platform.runLater(() -> {
            drawForground();
        });
    }

    @Override
    public void trackStart(SoundModel sound, AudioStreamData data) {
        this.audioData = data;
        buffer.trackStart(sound, data);
        Platform.runLater(() -> {
            drawBackground();
            drawForground();
        });
    }

    @Override
    public void fullTrack(float[] left, float[] right) {
        buffer.fullTrack(left, right);
        Platform.runLater(() -> drawBackground());
    }

    @Override
    public void trackEnd() {

    }

    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public void resize(double width, double height) {
        setWidth(width);
        setHeight(height);
        background.resize(width, height);
        forground.resize(width, height);
        drawBackground();
        drawForground();
    }

}
