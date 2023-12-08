module soundboard {

    exports de.trodel.soundboard;

    //javafx
    requires javafx.controls;
    requires transitive javafx.media;
    requires transitive javafx.graphics;

    //Sound
    requires static minim;
    requires java.desktop;

    exports de.trodel.soundboard.execution to minim;
    exports de.trodel.soundboard.server.rest.requests to jersey.server;

    //Keyhook
    requires jnativehook;
    requires java.logging;

    //Libs
    requires org.apache.commons.lang3;
    requires org.json;

    //RestServer
    requires jakarta.ws.rs;
    requires jakarta.annotation;
    requires jakarta.xml.bind;

    requires jersey.server;
    requires jersey.container.grizzly2.http;
    requires jersey.media.json.jackson;

    requires grizzly.http.server;
    requires grizzly.framework;
    requires grizzly.http;

    //Other
    requires soundboard.restbase;
}