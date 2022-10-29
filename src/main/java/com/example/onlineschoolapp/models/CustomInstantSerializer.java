package com.example.onlineschoolapp.models;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.Instant;

/**
 * User to serialize the value of an Instant to a String in 'yyyy-MM-ddTHH:mm:ssZ' format.
 */
public class CustomInstantSerializer extends StdSerializer<Instant> {

    public CustomInstantSerializer(Class<Instant> t) {
        super(t);
    }

    public CustomInstantSerializer(){
        this(null);
    }

    @Override
    public void serialize(Instant value, JsonGenerator jgen, SerializerProvider provider) throws IOException{
        jgen.writeString(value.toString());
    }
}
