package de.trodel.soundboard.restbase.s2c;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.trodel.soundboard.restbase.ResponseBase;

public class RestLogoutResponse extends ResponseBase {
    private final boolean success;

    @JsonCreator
    public RestLogoutResponse(@JsonProperty("success") boolean success) {
        this.success = success;
    }

    @JsonProperty("success")
    public boolean isSuccess() {
        return success;
    }

}
