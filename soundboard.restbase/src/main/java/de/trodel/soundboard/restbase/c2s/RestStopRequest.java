package de.trodel.soundboard.restbase.c2s;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.trodel.soundboard.restbase.RequestBase;
import de.trodel.soundboard.restbase.s2c.RestStopResponse;

public class RestStopRequest extends RequestBase<RestStopResponse>{
    
    public static enum StopType{
        StopSounds,
        StopAutoclicks,
        StopAll
    }
    
    private final String token;
    private final StopType type;
    

    @JsonCreator
    public RestStopRequest(@JsonProperty("token") String token, @JsonProperty("type") StopType type) {
        super("/action/stop");
        this.token = token;
        this.type = type;
    }

    @JsonProperty("token")
    public String getToken() {
        return token;
    }
    
    @JsonProperty("type")
    public StopType getType() {
        return type;
    }

    @Override
    protected Class<RestStopResponse> responceClass() {
        return RestStopResponse.class;
    }
}
