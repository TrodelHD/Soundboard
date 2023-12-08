package de.trodel.soundboard.gui;

import javafx.scene.control.TextInputControl;

public class InputUtils {

    public static void onlyInt(TextInputControl field) {
        field.textProperty().addListener((observable, oldV, newV) -> {
            if (newV.isBlank()) {
                return;
            }

            try {
                Integer.parseInt(newV);
            } catch (NumberFormatException e) {
                field.setText(oldV);
            }
        });
    }

    public static void onlyLong(TextInputControl field) {
        field.textProperty().addListener((observable, oldV, newV) -> {
            if (newV.isBlank()) {
                return;
            }

            try {
                Long.parseLong(newV);
            } catch (NumberFormatException e) {
                field.setText(oldV);
            }
        });
    }

}
