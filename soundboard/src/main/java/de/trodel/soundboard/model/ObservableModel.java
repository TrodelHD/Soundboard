package de.trodel.soundboard.model;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableNumberValue;
import javafx.beans.value.ObservableObjectValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class ObservableModel {

    public interface ObservableModelListener {
        public void modelChanged();
    }

    private final List<ObservableModelListener> listeners;
    private final ObservableModelListener       internalListener;

    public ObservableModel() {
        listeners = new ArrayList<>();
        internalListener = () -> listeners.forEach(l -> l.modelChanged());
    }

    protected ObservableModel listenOnModel(ObservableModel model) {
        model.addModelListener(internalListener);
        return model;
    }

    protected <E extends ObservableModel> ObservableList<E> listenOnModel(ObservableList<E> list) {
        list.addListener(new ListChangeListener<E>() {
            @Override
            public void onChanged(Change<? extends E> c) {
                while (c.next()) {
                    c.getRemoved().forEach(model -> {
                        if (!c.getList().contains(model)) {
                            model.removeModelListener(internalListener);
                        }
                    });
                    c.getAddedSubList().forEach(model -> model.addModelListener(internalListener));
                }
                internalListener.modelChanged();
            }
        });
        list.forEach(model -> model.addModelListener(internalListener));
        return list;
    }

    protected <E> ObservableObjectValue<E> listenOn(ObservableObjectValue<E> obj) {
        obj.addListener((observable, oldValue, newValue) -> {
            internalListener.modelChanged();
        });
        return obj;
    }

    protected ObservableNumberValue listenOn(ObservableNumberValue number) {
        number.addListener((observable, oldValue, newValue) -> {
            internalListener.modelChanged();
        });
        return number;

    }

    protected ObservableBooleanValue listenOn(ObservableBooleanValue bool) {
        bool.addListener((observable, oldValue, newValue) -> {
            internalListener.modelChanged();
        });
        return bool;
    }

    public void addModelListener(ObservableModelListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeModelListener(ObservableModelListener listener) {
        listeners.remove(listener);
    }
}
