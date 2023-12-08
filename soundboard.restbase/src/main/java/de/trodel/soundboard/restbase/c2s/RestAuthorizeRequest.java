package de.trodel.soundboard.restbase.c2s;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.trodel.soundboard.restbase.RequestBase;
import de.trodel.soundboard.restbase.s2c.RestAuthorizeResponse;

public class RestAuthorizeRequest extends RequestBase<RestAuthorizeResponse>{

    private final String secred;
    
    @JsonCreator
    public RestAuthorizeRequest(@JsonProperty("secred") String secred) {
        super("/authorization/authorize");
        this.secred = secred;
    }

    @JsonProperty("secred")
    public String getSecred() {
        return secred;
    }

    @Override
    protected Class<RestAuthorizeResponse> responceClass() {
        return RestAuthorizeResponse.class;
    }
}
