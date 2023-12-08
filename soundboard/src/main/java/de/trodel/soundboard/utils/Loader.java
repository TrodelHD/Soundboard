package de.trodel.soundboard.utils;

import java.io.IOException;
import java.net.URL;

import javafx.scene.image.Image;

public class Loader {

    public static String cssUrl(String css) throws IOException {
        return getResource("css/" + css + ".css").toExternalForm();
    }

    public static Image loadIcon(String icon) {
        return new Image(getResource("icons/" + icon + ".png").toExternalForm());
    }

    private static URL getResource(String resource) {
        return Loader.class.getResource("/de/trodel/soundboard/" + resource);
        //return Loader.class.getResource("/resources/" + resource);
    }
}
