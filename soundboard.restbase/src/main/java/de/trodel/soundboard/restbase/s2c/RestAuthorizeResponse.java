package de.trodel.soundboard.restbase.s2c;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.trodel.soundboard.restbase.ResponseBase;

public class RestAuthorizeResponse extends ResponseBase{
    private final boolean success;
    private final String reason;
    private final String token;
    private final long validUnitEpochSeconds;
    
    @JsonCreator
    public RestAuthorizeResponse(
            @JsonProperty("success") boolean success,
            @JsonProperty("reason") String reason,
            @JsonProperty("token") String token,
            @JsonProperty("validUnitEpochSeconds") long validUnitEpochSeconds) {
        this.success = success;
        this.reason = reason;
        this.token = token;
        this.validUnitEpochSeconds = validUnitEpochSeconds;
    }

    @JsonProperty("success")
    public boolean isSuccess() {
        return success;
    }
    
    @JsonProperty("reason")
    public String getReason() {
        return reason;
    }
    
    @JsonProperty("token")
    public String getToken() {
        return token;
    }
    
    @JsonProperty("validUnitEpochSeconds")
    public long getValidUnitEpochSeconds() {
        return validUnitEpochSeconds;
    }
}
