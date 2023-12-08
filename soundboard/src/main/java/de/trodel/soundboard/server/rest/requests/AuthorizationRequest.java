package de.trodel.soundboard.server.rest.requests;

import org.glassfish.grizzly.http.server.Request;

import de.trodel.soundboard.restbase.c2s.RestAuthorizeRequest;
import de.trodel.soundboard.restbase.c2s.RestLogoutRequest;
import de.trodel.soundboard.restbase.s2c.RestAuthorizeResponse;
import de.trodel.soundboard.restbase.s2c.RestLogoutResponse;
import de.trodel.soundboard.server.rest.RestAuthService;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/authorization")
public class AuthorizationRequest {

    private final RestAuthService authService;

    public AuthorizationRequest(RestAuthService authService) {
        this.authService = authService;
    }

    @POST
    @Path("/authorize")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response authorize(@Context Request rawRequest, RestAuthorizeRequest request) {
        try {
            var token = authService.createToken(request.getSecred(), rawRequest.getRemoteAddr());
            return Response.ok(new RestAuthorizeResponse(true, "", token.token(), token.expireTime().getEpochSecond())).build();
        } catch (Exception e) {
            return Response.status(401).entity(new RestAuthorizeResponse(false, e.getMessage(), "", 0)).build();
        }
    }

    @POST
    @Path("/logout")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response authorize(RestLogoutRequest request) {
        return Response.ok(new RestLogoutResponse(authService.removeToken(request.getToken()))).build();
    }

}
