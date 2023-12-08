package de.trodel.soundboard.gui;

import javafx.scene.control.Tab;

public abstract class AbstractMainTab extends Tab {
    public AbstractMainTab(String name) {
        super(name);
        setClosable(false);
    }
}
