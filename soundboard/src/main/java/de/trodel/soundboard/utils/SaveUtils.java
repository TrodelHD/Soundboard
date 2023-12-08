package de.trodel.soundboard.utils;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.newInputStream;
import static java.nio.file.Files.newOutputStream;
import static java.nio.file.Files.notExists;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.READ;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.nio.file.StandardOpenOption.WRITE;
import static java.util.Optional.empty;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

public class SaveUtils {
    private static final Path path = Path.of(
        System.getProperty("user.home"), "Documents", "Soundboard",
        "Sounds-Settings.json"
    );

    public static Optional<String> load() throws IOException {

        if (notExists(path)) {
            return empty();
        }

        try (var is = newInputStream(path, READ)) {
            return Optional.of(new String(is.readAllBytes(), UTF_8));
        }
    }

    public static void save(final String string) throws IOException {
        createDirectories(path.getParent());
        try (var os = newOutputStream(path, CREATE, WRITE, TRUNCATE_EXISTING)) {
            os.write(string.getBytes(UTF_8));
        }
    }
}
