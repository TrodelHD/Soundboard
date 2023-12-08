package de.trodel.soundboard.restbase.s2c;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.trodel.soundboard.restbase.ResponseBase;

public class RestDataResponse extends ResponseBase {

    public static class RestSound {
        private final String id;
        private final String name;

        @JsonCreator
        public RestSound(@JsonProperty("id") String id, @JsonProperty("name") String name) {
            this.id = id;
            this.name = name;
        }

        @JsonProperty("id")
        public String getId() {
            return id;
        }

        @JsonProperty("name")
        public String getName() {
            return name;
        }
    }

    public static class RestAutoclick {
        private final String id;
        private final String name;

        @JsonCreator
        public RestAutoclick(@JsonProperty("id") String id, @JsonProperty("name") String name) {
            this.id = id;
            this.name = name;
        }

        @JsonProperty("id")
        public String getId() {
            return id;
        }

        @JsonProperty("name")
        public String getName() {
            return name;
        }
    }

    private final List<RestSound> sounds;
    private final List<RestAutoclick> autoclicks;

    public RestDataResponse(@JsonProperty("sounds") List<RestSound> sounds, @JsonProperty("autoclicks") List<RestAutoclick> autoclicks) {
        super();
        this.sounds = sounds;
        this.autoclicks = autoclicks;
    }

    @JsonProperty("sounds")
    public List<RestSound> getSounds() {
        return sounds;
    }

    @JsonProperty("autoclicks")
    public List<RestAutoclick> getAutoclicks() {
        return autoclicks;
    }
}
