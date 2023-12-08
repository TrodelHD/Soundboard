package de.trodel.soundboard.restbase.s2c;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.trodel.soundboard.restbase.ResponseBase;

public class RestEchoResponse extends ResponseBase{
    private final String message;

    @JsonCreator
    public RestEchoResponse(@JsonProperty("message") String message) {
        this.message = message;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }
}
