package de.trodel.soundboard.gui.tabs.visualizer;

import static de.trodel.soundboard.gui.LayoutUtils.anchorFit;
import static javafx.scene.paint.CycleMethod.NO_CYCLE;

import org.apache.commons.lang3.Validate;

import de.trodel.soundboard.execution.ExecutionUtils.AudioStreamData;
import de.trodel.soundboard.execution.SoundListenerManager.FFTListener;
import de.trodel.soundboard.execution.SoundListenerManager.TrackEndListener;
import de.trodel.soundboard.execution.SoundListenerManager.TrackStartListener;
import de.trodel.soundboard.model.SoundModel;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

public class BarSpectrumPane extends BorderPane implements TrackStartListener, TrackEndListener, FFTListener {

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

    public static final short[] barFrequencys = new short[] {
        50, 100, 150, 300, 600, 900, 1200, 1500, 1800, 2100, 2400, 2700, 3000, 3600, 4200, 4800, 5400, 8000, 10000, 12000, 16000, 20000
    }; //Has to be low to high

    private final SpectrumBarBuffer spectrumBuffer;
    private final Canvas            canvas;

    private static final double SIDE_SPACER   = 0.15d;
    private static final double MIDDLE_SPACER = 0.08d;
    private static final double BAR_SPACE     = (1d - 2d * SIDE_SPACER - MIDDLE_SPACER) / 2d;

    public BarSpectrumPane() {
        this.spectrumBuffer = new SpectrumBarBuffer(2, 0.7f, barFrequencys);
        this.canvas = new AnchorCanvas();
        setCenter(anchorFit(canvas));
        drawBars();
    }

    private void drawBars() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        double width = getWidth();
        double height = getHeight();

        gc.clearRect(0, 0, width, height);
        gc.setFill(
            new LinearGradient(
                0, 0, 0, height, false, NO_CYCLE,
                new Stop(0, Color.RED),
                new Stop(0.6, Color.GREEN),
                new Stop(1.0, Color.GREEN.brighter())
            )
        );

        float[] leftSpec = spectrumBuffer.getBands(0);
        float[] rightSpec = spectrumBuffer.getBands(1);

        if (leftSpec == null || rightSpec == null) {
            return;
        }

        Validate.isTrue(leftSpec.length == rightSpec.length);

        double spacePerBar = width / leftSpec.length;
        double sideSpace = SIDE_SPACER * spacePerBar;
        double middleSpace = MIDDLE_SPACER * spacePerBar;
        double barSpace = BAR_SPACE * spacePerBar;

        for (int i = 0; i < leftSpec.length; i++) {
            double left = leftSpec[i] * height;
            double right = rightSpec[i] * height;

            double x0 = spacePerBar * i;

            gc.fillRect(x0 + sideSpace, height - left, barSpace, left);
            gc.fillRect(x0 + sideSpace + barSpace + middleSpace, height - right, barSpace, right);
        }
    }

    @Override
    public void fft(float[] spectrumLeft, float[] spectrumRight, float bandWidth) {
        spectrumBuffer.update(spectrumLeft, bandWidth, 0);
        spectrumBuffer.update(spectrumRight, bandWidth, 1);

        Platform.runLater(() -> {
            drawBars();
        });
    }

    @Override
    public void trackStart(SoundModel sound, AudioStreamData data) {
        spectrumBuffer.clearAll();
    }

    @Override
    public void trackEnd() {
        spectrumBuffer.clearAll();
        Platform.runLater(() -> {
            drawBars();
        });
    }
}
