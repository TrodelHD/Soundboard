package de.trodel.soundboard.restbase.c2s;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.trodel.soundboard.restbase.RequestBase;
import de.trodel.soundboard.restbase.s2c.RestActionResponse;

public class RestActionRequest extends RequestBase<RestActionResponse>{
    private final String token;
    private final String id;

    @JsonCreator
    public RestActionRequest(@JsonProperty("token") String token,@JsonProperty("id") String id) {
        super("/action/action");
        this.token = token;
        this.id = id;

    }

    @JsonProperty("token")
    public String getToken() {
        return token;
    } 
    
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @Override
    protected Class<RestActionResponse> responceClass() {
        return RestActionResponse.class;
    }
}
