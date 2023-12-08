package de.trodel.soundboard.server.rest.requests;

import java.util.UUID;

import de.trodel.soundboard.execution.SoundboardExecutionProvider;
import de.trodel.soundboard.model.MainModel;
import de.trodel.soundboard.restbase.c2s.RestActionRequest;
import de.trodel.soundboard.restbase.c2s.RestStopRequest;
import de.trodel.soundboard.restbase.s2c.RestActionResponse;
import de.trodel.soundboard.restbase.s2c.RestStopResponse;
import de.trodel.soundboard.server.rest.RestAuthService;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import javafx.application.Platform;

@Path("/action")
public class ActionRequest {
    private final MainModel                   mainModel;
    private final SoundboardExecutionProvider executionProvider;
    private final RestAuthService             authService;

    public ActionRequest(MainModel mainModel, SoundboardExecutionProvider executionProvider, RestAuthService authService) {
        this.mainModel = mainModel;
        this.executionProvider = executionProvider;
        this.authService = authService;
    }

    @POST
    @Path("/action")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response action(RestActionRequest request) {

        if (!authService.isTokenValid(request.getToken())) {
            return Response.status(401).build();
        }

        UUID target = UUID.fromString(request.getId());

        var sound = mainModel.getSounds().stream().filter(sm -> sm.getId().equals(target)).findFirst();
        var autoclick = mainModel.getAutoclicker().stream().filter(am -> am.getId().equals(target)).findFirst();

        Platform.runLater(() -> {
            sound.ifPresent(sm -> executionProvider.executeSound(sm));
            autoclick.ifPresent(am -> executionProvider.executeAutoclick(am));
        });

        return Response.ok(new RestActionResponse(sound.isPresent() || autoclick.isPresent())).build();
    }

    @POST
    @Path("/stop")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response stop(RestStopRequest request) {

        if (!authService.isTokenValid(request.getToken())) {
            return Response.status(401).build();
        }

        switch (request.getType()) {
            case StopSounds:
                stopSound();
                break;
            case StopAutoclicks:
                stopAutoClicks();
                break;
            case StopAll:
                stopSound();
                stopAutoClicks();
                break;
            default:
                throw new IllegalArgumentException("Unexpected value: " + request.getType());
        }

        return Response.ok(new RestStopResponse(true)).build();
    }

    private void stopAutoClicks() {
        mainModel.getAutoclicker().forEach(am -> executionProvider.stopAutoclick(am.getId()));
    }

    private void stopSound() {
        executionProvider.stopSound();
    }
}
