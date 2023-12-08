package de.trodel.soundboard.controller;

import static java.util.Optional.ofNullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class ListContentChangeObserver<E, T> {

    private final ObservableList<E>               observableList;
    private final ListChangeListener<E>           listChangeListener;
    private final Function<E, ObservableValue<T>> content;
    private final Map<E, ChangeListener<T>>       listeners = new HashMap<>();

    public ListContentChangeObserver(
            ObservableList<E> observableList,
            Function<E, ObservableValue<T>> content,
            Function<E, ChangeListener<T>> listener) {
        this.content = content;
        this.observableList = observableList;
        observableList.addListener(listChangeListener = new ListChangeListener<E>() {
            @Override
            public void onChanged(Change<? extends E> c) {
                while (c.next()) {
                    c.getRemoved().forEach(removed -> {
                        listeners.computeIfPresent(removed, (__, list) -> {
                            content.apply(removed).removeListener(list);
                            return null;
                        });
                    });

                    c.getAddedSubList().forEach(added -> {
                        ChangeListener<T> list = listener.apply(added);
                        ofNullable(listeners.put(added, list)).ifPresent(old -> content.apply(added).removeListener(old));
                        content.apply(added).addListener(list);
                    });
                }

            }
        });

        observableList.forEach(added -> {
            ChangeListener<T> list = listener.apply(added);
            ofNullable(listeners.put(added, list)).ifPresent(old -> content.apply(added).removeListener(old));
            content.apply(added).addListener(list);
        });

    }

    public void dispose() {
        this.observableList.removeListener(listChangeListener);
        listeners.forEach((e, list) -> content.apply(e).removeListener(list));
    }

}
