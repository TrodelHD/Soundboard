package de.trodel.soundboard.gui.tabs.common;

import static java.util.Arrays.stream;

import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.property.ReadOnlyProperty;

public class ValidMultiObserver extends ReadOnlyBooleanPropertyBase {

    private final ReadOnlyProperty<Boolean>[] properties;

    @SafeVarargs
    public ValidMultiObserver(ReadOnlyProperty<Boolean>... properties) {
        this.properties = properties;

        stream(properties).forEach(p -> {
            p.addListener((observer, oldV, newV) -> {
                ValidMultiObserver.this.fireValueChangedEvent();
            });
        });
    }

    @Override
    public Object getBean() {
        return null; // Virtual property, no bean
    }

    @Override
    public String getName() {
        return "ValidObserverProperty";
    }

    @Override
    public boolean get() {
        return stream(properties).allMatch(ReadOnlyProperty::getValue);
    }

}
