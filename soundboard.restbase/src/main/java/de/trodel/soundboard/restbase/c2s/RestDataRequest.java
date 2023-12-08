package de.trodel.soundboard.restbase.c2s;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.trodel.soundboard.restbase.RequestBase;
import de.trodel.soundboard.restbase.s2c.RestDataResponse;

public class RestDataRequest extends RequestBase<RestDataResponse> {

    private final String token;

    @JsonCreator
    public RestDataRequest(@JsonProperty("token") String token) {
        super("/data/getdata");
        this.token = token;
    }

    @JsonProperty("token")
    public String getToken() {
        return token;
    }

    @Override
    protected Class<RestDataResponse> responceClass() {
        return RestDataResponse.class;
    }

}
