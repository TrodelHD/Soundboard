package de.trodel.soundboard.gui;

import java.util.Objects;
import java.util.function.Function;

public class NameWrapper<E> {

    private final E                   e;
    private final Function<E, String> nameFunction;

    public NameWrapper(E e, Function<E, String> nameFunction) {
        this.e = e;
        this.nameFunction = nameFunction;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof NameWrapper<?> other) {
            return Objects.equals(other.e, e);
        }

        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(e);
    }

    @Override
    public String toString() {
        return nameFunction.apply(e);
    }

    public E getE() {
        return e;
    }

    public E getValue() {
        return e;
    }

}
