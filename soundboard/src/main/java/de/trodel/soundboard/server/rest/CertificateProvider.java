package de.trodel.soundboard.server.rest;

import static java.nio.file.Files.deleteIfExists;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

public class CertificateProvider {

    static final Path   KEYSTORE_PATH     = Path.of(
        System.getProperty("user.home"), "Documents", "Soundboard",
        "soundboard_key.jks"
    );
    static final String KEYSTORE_PASSWORD = "s0undb0ard_4.o";
    static final String KEYSTORE_ALIAS    = "soundboard-cert";

    public static void createIfNotExists() throws IOException, InterruptedException {
        if (!exists()) {
            createWithKeyTool();
        }
    }

    private static boolean exists() {
        return Files.exists(KEYSTORE_PATH);
    }

    private static void createWithKeyTool() throws IOException, InterruptedException {
        deleteIfExists(KEYSTORE_PATH);
        Process p = new ProcessBuilder(
            getJavaHome() + "\\bin\\keytool.exe",
            "-genkeypair",
            "-alias", KEYSTORE_ALIAS,
            "-keyalg", "RSA",
            "-keysize", "2048",
            "-sigalg", "SHA512withRSA",
            "-keypass", KEYSTORE_PASSWORD,
            "-keystore", KEYSTORE_PATH.toString(),
            "-storepass", KEYSTORE_PASSWORD,
            "-validity", "2000000",
            "-dname", "CN=Trodel, C=DE"
        ).start();
        try (var is = p.getInputStream(); var er = p.getErrorStream(); var os = p.getOutputStream()) {
            p.waitFor(5, TimeUnit.SECONDS);
        } finally {
            p.destroy();
        }
    }

    private static String getJavaHome() throws IOException {
        return System.getProperty("java.home");
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        createIfNotExists();
    }
}
