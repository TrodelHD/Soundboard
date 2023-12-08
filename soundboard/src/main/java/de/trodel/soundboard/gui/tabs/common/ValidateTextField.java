package de.trodel.soundboard.gui.tabs.common;

import java.util.function.Function;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.TextField;

public class ValidateTextField extends TextField {

    private final BooleanProperty         validProperty;
    private final ReadOnlyBooleanProperty validReadProperty;

    public ValidateTextField(String value, Function<String, Boolean> valid) {
        validProperty = new SimpleBooleanProperty(valid.apply(value));
        textProperty().addListener((observer, oldV, newV) -> {
            validProperty.set(valid.apply(newV));

        });
        validReadProperty = BooleanProperty.readOnlyBooleanProperty(validProperty);

        getValidProperty().addListener((observer, oldV, newV) -> {
            setValidColor();
        });
        setValidColor();
    }

    private void setValidColor() {
        if (isValid()) {
            setStyle("-fx-text-fill: green;");
        } else {
            setStyle("-fx-text-fill: red;");
        }
    }

    public ReadOnlyBooleanProperty getValidProperty() {
        return validReadProperty;
    }

    public boolean isValid() {
        return validReadProperty.get();
    }

}
