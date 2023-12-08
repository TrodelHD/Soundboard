package de.trodel.soundboard.restbase.c2s;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.trodel.soundboard.restbase.RequestBase;
import de.trodel.soundboard.restbase.s2c.RestEchoResponse;

public class RestEchoRequest extends RequestBase<RestEchoResponse>{

    private final String message;

    @JsonCreator
    public RestEchoRequest(@JsonProperty("message") String message) {
        super("/echo/echo");
        this.message = message;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @Override
    protected Class<RestEchoResponse> responceClass() {
        return RestEchoResponse.class;
    }

}
