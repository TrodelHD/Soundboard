package de.trodel.soundboard.server.rest.requests;

import de.trodel.soundboard.restbase.c2s.RestEchoRequest;
import de.trodel.soundboard.restbase.s2c.RestEchoResponse;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/echo")
public class EchoRequest {
    @POST
    @Path("/echo")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response echo(RestEchoRequest request) {
        return Response.ok(new RestEchoResponse(request.getMessage())).build();
    }
}
