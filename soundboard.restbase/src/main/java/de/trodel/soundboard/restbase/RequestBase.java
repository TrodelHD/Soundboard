package de.trodel.soundboard.restbase;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class RequestBase<E extends ResponseBase> {
    
    @JsonIgnore
    private final String path;
    
    public RequestBase(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
    
    protected abstract Class<E> responceClass();
    
    public E responseJson(String json,  ObjectMapper objectMapper) throws JsonMappingException, JsonProcessingException {
        return objectMapper.readValue(json, responceClass());
    }
}
