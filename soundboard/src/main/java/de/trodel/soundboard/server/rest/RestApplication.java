package de.trodel.soundboard.server.rest;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.TimeUnit;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.ssl.SSLContextConfigurator;
import org.glassfish.grizzly.ssl.SSLEngineConfigurator;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import de.trodel.soundboard.execution.SoundboardExecutionProvider;
import de.trodel.soundboard.model.MainModel;
import de.trodel.soundboard.server.rest.RestAuthService.TokenData;
import de.trodel.soundboard.server.rest.requests.ActionRequest;
import de.trodel.soundboard.server.rest.requests.AuthorizationRequest;
import de.trodel.soundboard.server.rest.requests.DataRequest;
import de.trodel.soundboard.server.rest.requests.EchoRequest;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import javafx.collections.ObservableMap;

public class RestApplication implements AutoCloseable, ExceptionMapper<Exception> {
    public static final String BASE_URI = "http://localhost:8080/";

    private final HttpServer      server;
    private final RestAuthService authService;

    public RestApplication(MainModel mainModel, SoundboardExecutionProvider executionProvider) {
        this.authService = new RestAuthService(mainModel.getServer().getKeyProperty());

        final ResourceConfig rc = new ResourceConfig().packages();

        rc.register(JacksonFeature.class);

        rc.register(new AuthorizationRequest(authService));
        rc.register(new DataRequest(mainModel, authService));
        rc.register(new ActionRequest(mainModel, executionProvider, authService));
        rc.register(new EchoRequest());
        rc.register(this);

        try {
            CertificateProvider.createIfNotExists();
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }

        SSLContextConfigurator sslCon = new SSLContextConfigurator();

        sslCon.setKeyStoreFile(CertificateProvider.KEYSTORE_PATH.toString());
        sslCon.setKeyStorePass(CertificateProvider.KEYSTORE_PASSWORD);

        server = GrizzlyHttpServerFactory.createHttpServer(
            URI.create("http://0.0.0.0:" + mainModel.getServer().getPort() + "/"),
            rc,
            true,
            new SSLEngineConfigurator(sslCon, false, false, false)
        );

        //, true, new SSLEngineConfigurator(sslCon, false, false, false)
    }

    @Override
    public Response toResponse(Exception exception) {
        exception.printStackTrace();

        return Response.status(500).build();
    }

    @Override
    public void close() {
        try {
            server.shutdown().get(5, TimeUnit.SECONDS);
        } catch (Exception e) {
            server.shutdownNow();
        }
    }

    public ObservableMap<String, TokenData> getTokenData() {
        return authService.getTokens();
    }

}
