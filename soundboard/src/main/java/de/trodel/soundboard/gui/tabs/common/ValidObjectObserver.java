package de.trodel.soundboard.gui.tabs.common;

import java.util.function.Function;

import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ObservableObjectValue;

public class ValidObjectObserver<T> extends ReadOnlyBooleanPropertyBase {

    private final ObservableObjectValue<T> value;
    private final Function<T, Boolean>     function;

    public ValidObjectObserver(ObservableObjectValue<T> value, Function<T, Boolean> function) {
        this.value = value;
        this.function = function;

        this.value.addListener((observer, oldV, newV) -> {
            fireValueChangedEvent();
        });
    }

    @Override
    public Object getBean() {
        return null; // Virtual property, no bean
    }

    @Override
    public String getName() {
        return "ValidObjectProperty";
    }

    @Override
    public boolean get() {
        return function.apply(value.get());
    }

}
