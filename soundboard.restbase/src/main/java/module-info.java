module soundboard.restbase {
    exports de.trodel.soundboard.restbase;
    exports de.trodel.soundboard.restbase.s2c;
    exports de.trodel.soundboard.restbase.c2s;

    requires transitive com.fasterxml.jackson.core;
    requires transitive com.fasterxml.jackson.annotation;
    requires transitive com.fasterxml.jackson.databind;
    
    opens de.trodel.soundboard.restbase.c2s to com.fasterxml.jackson.databind;
    opens de.trodel.soundboard.restbase.s2c to com.fasterxml.jackson.databind;

}