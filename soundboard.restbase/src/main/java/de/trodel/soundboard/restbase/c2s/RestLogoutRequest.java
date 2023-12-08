package de.trodel.soundboard.restbase.c2s;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.trodel.soundboard.restbase.RequestBase;
import de.trodel.soundboard.restbase.s2c.RestLogoutResponse;

public class RestLogoutRequest extends RequestBase<RestLogoutResponse> {
    
    private final String token;
    
    @JsonCreator
    public RestLogoutRequest(@JsonProperty("token") String token) {
        super("/authorization/logout");
        this.token = token;
    }

    @JsonProperty("token")
    public String getToken() {
        return token;
    }

    @Override
    protected Class<RestLogoutResponse> responceClass() {
        return RestLogoutResponse.class;
    }

}
