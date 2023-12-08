package de.trodel.soundboard.gui;

import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;

public class LayoutUtils {
    public static AnchorPane anchorFit(Node node) {
        return anchorFit(node, 0, 0, 0, 0);
    }

    public static AnchorPane anchorFit(Node node, double top, double left, double right, double bottom) {
        AnchorPane anchorPane = new AnchorPane(node);

        AnchorPane.setTopAnchor(node, top);
        AnchorPane.setLeftAnchor(node, left);
        AnchorPane.setRightAnchor(node, right);
        AnchorPane.setBottomAnchor(node, bottom);

        return anchorPane;
    }

    public static AnchorPane anchorLeftRight(Node node) {
        return anchorLeftRight(node, 0, 0);
    }

    public static AnchorPane anchorLeftRight(Node node, double left, double right) {
        AnchorPane anchorPane = new AnchorPane(node);

        AnchorPane.setLeftAnchor(node, left);
        AnchorPane.setRightAnchor(node, right);

        return anchorPane;
    }

    public static AnchorPane anchorTopBottom(Node node) {
        return anchorTopBottom(node, 0, 0);
    }

    public static AnchorPane anchorTopBottom(Node node, double top, double bottom) {
        AnchorPane anchorPane = new AnchorPane(node);

        AnchorPane.setTopAnchor(node, top);
        AnchorPane.setBottomAnchor(node, bottom);

        return anchorPane;
    }

    private static class AtomicDouble {
        public double value;
    }

    public static void keepHorizontalDividerAtPixel(SplitPane splitPane, double firstValue) {
        AtomicDouble dividerPos = new AtomicDouble();
        dividerPos.value = firstValue;

        splitPane.widthProperty().addListener((observable, oldV, newV) -> {
            splitPane.setDividerPosition(0, dividerPos.value / newV.doubleValue());
        });

        splitPane.getDividers().get(0).positionProperty().addListener((observable, oldV, newV) -> {
            dividerPos.value = newV.doubleValue() * splitPane.getWidth();
        });

    }
}
