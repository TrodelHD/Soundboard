package de.trodel.soundboard.server.rest.requests;

import static java.util.stream.Collectors.toList;

import de.trodel.soundboard.model.MainModel;
import de.trodel.soundboard.restbase.c2s.RestDataRequest;
import de.trodel.soundboard.restbase.s2c.RestDataResponse;
import de.trodel.soundboard.restbase.s2c.RestDataResponse.RestAutoclick;
import de.trodel.soundboard.restbase.s2c.RestDataResponse.RestSound;
import de.trodel.soundboard.server.rest.RestAuthService;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/data")
public class DataRequest {
    private final MainModel       mainModel;
    private final RestAuthService authService;

    public DataRequest(MainModel mainModel, RestAuthService authService) {
        this.mainModel = mainModel;
        this.authService = authService;
    }

    @POST
    @Path("/getdata")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(RestDataRequest request) {

        if (!authService.isTokenValid(request.getToken())) {
            return Response.status(401).build();
        }

        return Response.ok(
            new RestDataResponse(
                mainModel.getSounds().stream().map(sm -> new RestSound(sm.getId().toString(), sm.getName())).collect(toList()),
                mainModel.getAutoclicker().stream().map(am -> new RestAutoclick(am.getId().toString(), am.getName())).collect(toList())
            )
        ).build();
    }

}
